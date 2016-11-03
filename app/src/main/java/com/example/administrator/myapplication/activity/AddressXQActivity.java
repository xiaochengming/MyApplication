package com.example.administrator.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Address;
import com.example.administrator.myapplication.util.StringUtil;
import com.example.administrator.myapplication.widget.CustomDialog;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddressXQActivity extends AppCompatActivity {
    @InjectView(R.id.textView12)
    TextView textView12;
    @InjectView(R.id.iv_bian_ji_address)
    ImageView ivBianJiAddress;
    @InjectView(R.id.tv_shan_chu)
    TextView tvShanChu;
    @InjectView(R.id.rl_bianji_address)
    RelativeLayout rlBianjiAddress;
    @InjectView(R.id.tv_address_name)
    TextView tvAddressName;
    @InjectView(R.id.et_address_xq_name)
    EditText etAddressXqName;
    @InjectView(R.id.textView8)
    TextView textView8;
    @InjectView(R.id.tv_address_xq_phone)
    TextView tvAddressXqPhone;
    @InjectView(R.id.et_address_phone)
    EditText etAddressPhone;
    @InjectView(R.id.textView9)
    TextView textView9;
    @InjectView(R.id.tv_address_xq_set)
    TextView tvAddressXqSet;
    @InjectView(R.id.iv_set_address)
    ImageView ivSetAddress;
    @InjectView(R.id.but_save_address)
    Button butSaveAddress;
    @InjectView(R.id.tv_xq_di_zhi)
    TextView tvXqDiZhi;
    @InjectView(R.id.et_xq_di_zhi)
    EditText etXqDiZhi;
    @InjectView(R.id.frame_layout_address)
    RelativeLayout frameLayoutAddress;
    @InjectView(R.id.tv_display_xq_address)
    TextView tvDisplayXqAddress;
    Address address;
    String addressStr=null;
    int position;
    int addressId;
    double latitude;
    double lontitude;
    // boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_xq);
        ButterKnife.inject(this);
        Intent intent=getIntent();
        position=intent.getIntExtra("position",-1);

        if (intent.getParcelableExtra("address")!=null){
            address=intent.getParcelableExtra("address");

            addressId=address.getAddressId();
            if (address.getUserName()!=null){
                etAddressXqName.setText(address.getUserName());
            }
            if (address.getPhone()!=null){
                etAddressPhone.setText(address.getPhone());
            }
            if (address.getAddress()!=null){
                tvDisplayXqAddress.setText(address.getAddress());
            }
            Log.i("AddressXQActivity", "onCreate addressId :"+addressId);
        }
//        if (flag==true){
//
//        }
    }

    @OnClick({R.id.iv_bian_ji_address, R.id.tv_shan_chu, R.id.tv_address_xq_set,R.id.but_save_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_bian_ji_address:
                //返回前一个界面
                finish();
                Log.i("AddressXQActivity", "onClick  11");
                break;
            case R.id.tv_shan_chu:
                //删除当前地址
                //显示对话框
                CustomDialog.Builder builder = new CustomDialog.Builder(this);
                builder.setMessage("您确定要删除这个地址吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项
                        deleteData();
                    }
                });
                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
                Log.i("AddressXQActivity", "onClick  22");
                break;
            case R.id.tv_address_xq_set:
                //进入地图界面
                //点击进入地图界面(回调)
                Intent intent = new Intent(AddressXQActivity.this, MapLocationActivity.class);
                startActivityForResult(intent,616);
                Log.i("AddressXQActivity", "onClick  33");
                break;
            case R.id.but_save_address:
                //确定
                //点击保存地址
                //存到数据库
                Log.i("AddressXQActivity", "onClick  点击保存地址");
                if (etAddressXqName.getText().toString().length()<2){
                    Toast.makeText(AddressXQActivity.this,"联系人至少输入两个字",Toast.LENGTH_SHORT).show();
                    return;
                }else if (tvDisplayXqAddress.getText().toString().equals("显示地址")){
                    Toast.makeText(AddressXQActivity.this,"请添加地址",Toast.LENGTH_SHORT).show();
                    return;
                }else if (etXqDiZhi.getText().toString().length()<2){
                    Toast.makeText(AddressXQActivity.this,"输入详细地址",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    getData();
                    Log.i("AddressXQActivity", "onClick  33");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //显示
        frameLayoutAddress.setVisibility(View.VISIBLE);
        //  flag=true;
        if (resultCode==RESULT_OK&&requestCode==616){
            if (data!=null&&data.getStringExtra("addressName")!=null){
                //显示位置
                tvDisplayXqAddress.setText(data.getStringExtra("addressName"));
                latitude=data.getDoubleExtra("latitude",0);
                lontitude=data.getDoubleExtra("lontitude",0);
                Log.i("AddressXQActivity", "onActivityResult 11 ");
            }
        }else if (resultCode==RESULT_FIRST_USER&&requestCode==616){
            //显示位置
            tvDisplayXqAddress.setText(data.getStringExtra("addressName"));
            latitude=data.getDoubleExtra("latitude",0);
            lontitude=data.getDoubleExtra("lontitude",0);
            Log.i("AddressXQActivity", "onActivityResult 22 ");
        }
    }
    public void getData(){
        RequestParams requestParams=new RequestParams(StringUtil.ip+"/XiuGaiDiZhiServlet");
        requestParams.addBodyParameter("userId",address.getUserId()+"");
        requestParams.addBodyParameter("addressId",addressId+"");
        requestParams.addBodyParameter("userName",etAddressXqName.getText().toString());
        requestParams.addBodyParameter("userPhone",etAddressPhone.getText().toString());
        requestParams.addBodyParameter("latitude", latitude+"");
        requestParams.addBodyParameter("lontitude", lontitude+"");
        addressStr=tvDisplayXqAddress.getText().toString();
        addressStr+=etXqDiZhi.getText().toString();
        requestParams.addBodyParameter("address",addressStr);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("AddressXQActivity", "onSuccess  12345");
                Address address2=new Address(addressId,addressStr,etAddressXqName.getText().toString(),etAddressPhone.getText().toString(),address.getUserId(),latitude,lontitude);
                //成功执行跳转
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putParcelable("address",address2);
                intent.putExtras(bundle);
                intent.putExtra("position",position);
                setResult(RESULT_OK,intent);
                finish();

                Log.i("AddAddressActivity", "onSuccess  :"+result);
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
    public void deleteData(){
        RequestParams requestParams=new RequestParams(StringUtil.ip+"/ShanChuDiZhiServlet");
        requestParams.addBodyParameter("userId",address.getUserId()+"");
        requestParams.addBodyParameter("addressId",address.getAddressId()+"");
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Intent intent=new Intent();
                intent.putExtra("position",position);
                setResult(RESULT_FIRST_USER,intent);
                finish();
                Log.i("AddAddressActivity", "onSuccess  :"+result);
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
