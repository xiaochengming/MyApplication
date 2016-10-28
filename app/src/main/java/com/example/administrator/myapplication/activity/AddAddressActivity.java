package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Address;
import com.example.administrator.myapplication.util.StringUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 添加地址服务
 */
public class AddAddressActivity extends AppCompatActivity {

    @InjectView(R.id.add_address_toolbar)
    Toolbar addAddressToolbar;
    @InjectView(R.id.et_address_name)
    EditText etAddressName;
    @InjectView(R.id.et_address_phone)
    EditText etAddressPhone;
    @InjectView(R.id.iv_set_address)
    ImageView ivSetAddress;
    @InjectView(R.id.but_save_address)
    Button butSaveAddress;
    @InjectView(R.id.frame_layout_add_address)
    RelativeLayout frameLayoutAddress;
    @InjectView(R.id.tv_display_address)
    TextView tvDisplayAddress;
    MyApplication myApplication;
    @InjectView(R.id.tv_address_set)
    TextView tvAddressSet;
    String addressStr = null;
    @InjectView(R.id.et_add_di_zhi)
    EditText etAddDiZhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.inject(this);

        myApplication = (MyApplication) getApplication();
        if (myApplication.getUser().getNumber() != null) {
            etAddressPhone.setText(myApplication.getUser().getNumber());
        }
        //设置导航图标
        addAddressToolbar.setNavigationIcon(R.mipmap.backs);
        //设置主标题
        addAddressToolbar.setTitle("");
        //设置actionBar为toolBar
        setSupportActionBar(addAddressToolbar);
        //设置toolBar的导航图标点击事件
        addAddressToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到地址
//                Intent intent=new Intent();
//                setResult(RESULT_FIRST_USER,intent);
                finish();

            }
        });

    }

    @OnClick({R.id.tv_address_set, R.id.but_save_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_address_set:
                //点击进入地图界面(回调)
                Intent intent = new Intent(AddAddressActivity.this, MapLocationActivity.class);
                startActivityForResult(intent, 212);
                Log.i("AddAddressActivity", "onClick  11");
                break;
            case R.id.but_save_address:
                //点击保存地址
                //存到数据库

                if (etAddressName.getText().toString().length() < 2) {
                    Toast.makeText(AddAddressActivity.this, "联系人至少输入两个字", Toast.LENGTH_SHORT).show();
                    return;
                } else if (tvDisplayAddress.getText().toString().equals("显示地址")) {
                    Toast.makeText(AddAddressActivity.this, "请添加地址", Toast.LENGTH_SHORT).show();
                    return;
                } else if (etAddDiZhi.getText().toString().length() < 2) {
                    Toast.makeText(AddAddressActivity.this, "输入详细地址", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    getData();
                    Log.i("AddAddressActivity", "onClick  11");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 212) {
            if (data != null && data.getStringExtra("addressName") != null) {
                //显示位置
                tvDisplayAddress.setText(data.getStringExtra("addressName"));
                Log.i("AddAddressActivity", "onActivityResult  11");
            }
        } else if (resultCode == RESULT_FIRST_USER && requestCode == 212) {
            //显示位置
            tvDisplayAddress.setText(data.getStringExtra("addressName"));
            Log.i("AddAddressActivity", "onActivityResult  22");
        }
    }

    public void getData() {
        RequestParams requestParams = new RequestParams(StringUtil.ip + "/TianJiaDiZhiServlet");
        requestParams.addBodyParameter("userId", myApplication.getUser().getUserId() + "");
        requestParams.addBodyParameter("userName", etAddressName.getText().toString());
        requestParams.addBodyParameter("userPhone", etAddressPhone.getText().toString());
        addressStr = tvDisplayAddress.getText().toString();
        addressStr += etAddDiZhi.getText().toString();
        requestParams.addBodyParameter("address", addressStr);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                int addressId = Integer.parseInt(result);
                Log.i("AddAddressActivity", "onSuccess 1122: " + addressId);
                //成功执行跳转
                Intent intent = new Intent();
                Address address = new Address(addressId, addressStr, etAddressName.getText().toString(), etAddressPhone.getText().toString(), myApplication.getUser().getUserId());
                Bundle bundle = new Bundle();
                bundle.putParcelable("address", address);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();

                Log.i("AddAddressActivity", "onSuccess 2211 :" + result);
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
