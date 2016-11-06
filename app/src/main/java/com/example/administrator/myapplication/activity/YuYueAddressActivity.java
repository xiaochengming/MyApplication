package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.myapplication.Adapter.CommonAdapter;
import com.example.administrator.myapplication.Adapter.ViewHolder;
import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Address;
import com.example.administrator.myapplication.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class YuYueAddressActivity extends AppCompatActivity {

    @InjectView(R.id.yuyue_address_toolbar)
    Toolbar yuyueAddressToolbar;
    @InjectView(R.id.but_yuyue_address)
    Button butYuyueAddress;
    @InjectView(R.id.lv_yuyue_address)
    ListView lvYuyueAddress;
    CommonAdapter<Address> addressAdapter;
    List<Address> addresses=new ArrayList<>();
    MyApplication myApplication;
    int positions;
    Address address;
    String[] str=new String[]{"首次使用时,请添加服务地址!"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yu_yue_address);
        ButterKnife.inject(this);
        myApplication= (MyApplication) getApplication();
        //设置导航图标
        yuyueAddressToolbar.setNavigationIcon(R.mipmap.backs);
        //设置主标题
        yuyueAddressToolbar.setTitle("");
        //设置actionBar为toolBar
        setSupportActionBar(yuyueAddressToolbar);
        //设置toolBar的导航图标点击事件
        yuyueAddressToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到前一页;
                finish();
            }
        });
        getData();
    }
    @OnClick(R.id.but_yuyue_address)
    public void onClick() {
        //进入到地址添加页面
        Intent intent=new Intent(YuYueAddressActivity.this,AddAddressActivity.class);
        startActivityForResult(intent,818);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==818){
            if (data!=null&&data.getParcelableExtra("address")!=null){
                //更新界面
                address=data.getParcelableExtra("address");
                addresses.add(address);
                if (addressAdapter == null) {
                    addressAdapter = new CommonAdapter<Address>(YuYueAddressActivity.this, addresses, R.layout.lh_yuyue_address_liebiao) {
                        @Override
                        public void convert(ViewHolder viewHolder, final Address address, final int position) {
                            Log.i("AddressActivity", "convert  address:"+address);
                            //找控件赋值
                            //姓名
                            TextView tvAddressName = viewHolder.getViewById(R.id.tv_yu_yue_username);
                            tvAddressName.setText(address.getUserName());
                            //电话
                            TextView tvAddressPhone = viewHolder.getViewById(R.id.tv_yu_yue_phone);
                            tvAddressPhone.setText(address.getPhone());
                            //地址名称
                            TextView tvAddressAdds = viewHolder.getViewById(R.id.tv_address_yu_yue);
                            tvAddressAdds.setText(address.getAddress());

                            //编辑点击事件
                            ImageView ivYuyueXiuGai=viewHolder.getViewById(R.id.iv_yuyue_xiugai);
                            ivYuyueXiuGai.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //跳转到地址详情界面
                                    Intent intent1 = new Intent(YuYueAddressActivity.this, AddressXQActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("address", address);
                                    intent1.putExtra("position",position);
                                    intent1.putExtras(bundle);
                                    startActivityForResult(intent1, 919);
                                }
                            });
                        }
                    };
                    lvYuyueAddress.setAdapter(addressAdapter);
                   //listview的item点击事件
                    lvYuyueAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.i("AddressActivity", "onItemClick  lvAddress:"+position);
                            //回调到预约界面
                            Intent intent1 = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("address", addresses.get(position));
                            // intent1.putExtra("position",position);
                            intent1.putExtras(bundle);
                            setResult(RESULT_OK,intent1);
                            finish();
                        }
                    });
                } else {
                    addressAdapter.notifyDataSetChanged();
                }
            }
        }else if (resultCode==RESULT_FIRST_USER&&requestCode==818){
            //更新界面
            addressAdapter.notifyDataSetChanged();
            Log.i("AddressActivity", "onActivityResult  22");
        }else if (resultCode==RESULT_OK&&requestCode==919){
            if (data!=null){
                positions=data.getIntExtra("position",-1);
                if (data.getParcelableExtra("address")!=null){
                    addresses.remove(positions);
                    addresses.add(positions, (Address) data.getParcelableExtra("address"));
                    addressAdapter.notifyDataSetChanged();
                    Log.i("AddressActivity", "onActivityResult  33");
                }
            }
        }else if (resultCode==RESULT_FIRST_USER&&requestCode==919){
            if (data!=null){
                positions=data.getIntExtra("position",-1);
                addresses.remove(positions);
                addressAdapter.notifyDataSetChanged();
                Log.i("AddressActivity", "onActivityResult  44");
            }
        }
    }
    //连接服务器
    public void getData(){
        RequestParams requestParams=new RequestParams(StringUtil.ip+"/AddressByIdServlet");
        requestParams.addQueryStringParameter("userId",myApplication.getUser().getUserId()+"");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                Type type=new TypeToken<List<Address>>(){}.getType();
                addresses=gson.fromJson(result,type);
                if (addresses.size()==0){
                    ArrayAdapter arrayAdapter=new ArrayAdapter(YuYueAddressActivity.this,R.layout.activity_first_address,R.id.tv_zanwu_address,str);
                    lvYuyueAddress.setAdapter(arrayAdapter);
                    Log.i("AddressActivity", "onSuccess arrayAdapter ");

                }else {
                    Log.i("AddressActivity", "onSuccess  addresses:"+addresses);
                    if (addressAdapter == null) {
                        addressAdapter = new CommonAdapter<Address>(YuYueAddressActivity.this, addresses, R.layout.lh_yuyue_address_liebiao) {
                            @Override
                            public void convert(ViewHolder viewHolder, final Address address, final int position) {
                                Log.i("AddressActivity", "convert  address:"+address);
                                //找控件赋值
                                //姓名
                                TextView tvAddressName = viewHolder.getViewById(R.id.tv_yu_yue_username);
                                tvAddressName.setText(address.getUserName());
                                //电话
                                TextView tvAddressPhone = viewHolder.getViewById(R.id.tv_yu_yue_phone);
                                tvAddressPhone.setText(address.getPhone());
                                //地址名称
                                TextView tvAddressAdds = viewHolder.getViewById(R.id.tv_address_yu_yue);
                                tvAddressAdds.setText(address.getAddress());

                                //编辑点击事件
                                ImageView ivYuyueXiuGai=viewHolder.getViewById(R.id.iv_yuyue_xiugai);
                                ivYuyueXiuGai.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //跳转到地址详情界面
                                        Intent intent1 = new Intent(YuYueAddressActivity.this, AddressXQActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("address", address);
                                        intent1.putExtra("position",position);
                                        intent1.putExtras(bundle);
                                        startActivityForResult(intent1, 919);
                                    }
                                });

                            }
                        };
                        lvYuyueAddress.setAdapter(addressAdapter);

                        //listview的item点击事件
                        lvYuyueAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Log.i("AddressActivity", "onItemClick  lvAddress:"+position);
                                //回调到预约界面
                                Intent intent1 = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("address", addresses.get(position));
                               // intent1.putExtra("position",position);
                                intent1.putExtras(bundle);
                                setResult(RESULT_OK,intent1);
                                finish();
                            }
                        });
                    } else {
                        addressAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
