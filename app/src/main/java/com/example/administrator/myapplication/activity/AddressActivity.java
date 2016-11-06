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
import android.widget.ListView;
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

/**
 * 非首次添加地址
 */
public class AddressActivity extends AppCompatActivity {

    @InjectView(R.id.address_toolbar)
    Toolbar addressToolbar;
    @InjectView(R.id.but_address)
    Button butAddress;
    @InjectView(R.id.lv_address)
    ListView lvAddress;
    CommonAdapter<Address> addressAdapter;
    List<Address> addresses=new ArrayList<>();
    MyApplication myApplication;
    int positions;
    Address address;
   String[] str=new String[]{"首次使用时,请添加服务地址!"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.inject(this);
        myApplication= (MyApplication) getApplication();
        //设置导航图标
        addressToolbar.setNavigationIcon(R.mipmap.backs);
        //设置主标题
        addressToolbar.setTitle("");
        //设置actionBar为toolBar
        setSupportActionBar(addressToolbar);
        //设置toolBar的导航图标点击事件
        addressToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到首页;
                finish();
            }
        });
        getData();
    }
    @OnClick(R.id.but_address)
    public void onClick() {
        //进入到地址添加页面
        Intent intent=new Intent(AddressActivity.this,AddAddressActivity.class);
        startActivityForResult(intent,313);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==313){
            if (data!=null&&data.getParcelableExtra("address")!=null){
                //更新界面
                address=data.getParcelableExtra("address");
                addresses.add(address);
                if (addressAdapter == null) {
                    addressAdapter = new CommonAdapter<Address>(AddressActivity.this, addresses, R.layout.lh_address_liebiao) {
                        @Override
                        public void convert(ViewHolder viewHolder, Address address, int position) {
                            Log.i("AddressActivity", "convert  address:"+address);
                            //找控件赋值
                            //姓名
                            TextView tvAddressName = viewHolder.getViewById(R.id.tv_liebiao_username);
                            tvAddressName.setText(address.getUserName());
                            //电话
                            TextView tvAddressPhone = viewHolder.getViewById(R.id.tv_liebiao_phone);
                            tvAddressPhone.setText(address.getPhone());
                            //地址名称
                            TextView tvAddressAdds = viewHolder.getViewById(R.id.tv_address_adds);
                            tvAddressAdds.setText(address.getAddress());
                        }
                    };
                    lvAddress.setAdapter(addressAdapter);
                    //listview的item点击事件
                    lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.i("AddressActivity", "onItemClick  lvAddress:"+position);
                            //跳转到地址详情界面
                            Intent intent1 = new Intent(AddressActivity.this, AddressXQActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("address", addresses.get(position));
                            intent1.putExtra("position",position);
                            intent1.putExtras(bundle);
                            startActivityForResult(intent1, 515);
                        }
                    });
                } else {
                    addressAdapter.notifyDataSetChanged();
                }
            }
        }else if (resultCode==RESULT_FIRST_USER&&requestCode==313){
            //更新界面
            addressAdapter.notifyDataSetChanged();
            Log.i("AddressActivity", "onActivityResult  22");
        }else if (resultCode==RESULT_OK&&requestCode==515){
            if (data!=null){
                positions=data.getIntExtra("position",-1);
                if (data.getParcelableExtra("address")!=null){
                    addresses.remove(positions);
                    addresses.add(positions, (Address) data.getParcelableExtra("address"));
                    addressAdapter.notifyDataSetChanged();
                    Log.i("AddressActivity", "onActivityResult  33");
                }
            }
        }else if (resultCode==RESULT_FIRST_USER&&requestCode==515){
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
                    ArrayAdapter arrayAdapter=new ArrayAdapter(AddressActivity.this,R.layout.activity_first_address,R.id.tv_zanwu_address,str);
                    lvAddress.setAdapter(arrayAdapter);
                    Log.i("AddressActivity", "onSuccess arrayAdapter ");
                }else {
                    Log.i("AddressActivity", "onSuccess  addresses:"+addresses);
                    if (addressAdapter == null) {
                        addressAdapter = new CommonAdapter<Address>(AddressActivity.this, addresses, R.layout.lh_address_liebiao) {
                            @Override
                            public void convert(ViewHolder viewHolder, Address address, int position) {
                                Log.i("AddressActivity", "convert  address:"+address);
                                //找控件赋值
                                //姓名
                                TextView tvAddressName = viewHolder.getViewById(R.id.tv_liebiao_username);
                                tvAddressName.setText(address.getUserName());
                                //电话
                                TextView tvAddressPhone = viewHolder.getViewById(R.id.tv_liebiao_phone);
                                tvAddressPhone.setText(address.getPhone());
                                //地址名称
                                TextView tvAddressAdds = viewHolder.getViewById(R.id.tv_address_adds);
                                tvAddressAdds.setText(address.getAddress());
                            }
                        };
                        lvAddress.setAdapter(addressAdapter);
                        //listview的item点击事件
                        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Log.i("AddressActivity", "onItemClick  lvAddress:"+position);
                                //跳转到地址详情界面
                                Intent intent1 = new Intent(AddressActivity.this, AddressXQActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("address", addresses.get(position));
                                intent1.putExtra("position",position);
                                intent1.putExtras(bundle);
                                startActivityForResult(intent1, 515);
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
