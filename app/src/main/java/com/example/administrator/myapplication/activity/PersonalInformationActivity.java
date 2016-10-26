package com.example.administrator.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.User;
import com.example.administrator.myapplication.fragment.BirthdayFragment;
import com.example.administrator.myapplication.fragment.EmilFragment;
import com.example.administrator.myapplication.util.StringUtil;
import com.google.gson.Gson;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 个人信息
 */
public class PersonalInformationActivity extends AppCompatActivity {

    @InjectView(R.id.information_toolbar)
    Toolbar informationToolbar;
    @InjectView(R.id.iv_information)
    ImageView ivInformation;
    @InjectView(R.id.information_framelayout)
    FrameLayout informationFramelayout;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_dsname)
    TextView tvDsname;
    @InjectView(R.id.tv_phone)
    TextView tvPhone;
    @InjectView(R.id.tv_diphone)
    TextView tvDiphone;
    @InjectView(R.id.tv_age)
    TextView tvAge;
    @InjectView(R.id.tv_dsage)
    TextView tvDsage;
    @InjectView(R.id.tv_sex)
    TextView tvSex;
    @InjectView(R.id.tv_dssex)
    TextView tvDssex;
    String value = null;
    MyApplication myApplication;
    @InjectView(R.id.tv_edit_head)
    TextView tvEditHead;
    @InjectView(R.id.rl_edit_name)
    RelativeLayout rlEditName;
    @InjectView(R.id.rl_edit_phone)
    RelativeLayout rlEditPhone;
    @InjectView(R.id.rl_edit_aget)
    RelativeLayout rlEditAget;
    @InjectView(R.id.iv_edit_sex)
    ImageView ivEditSex;
    @InjectView(R.id.rl_edit_sex)
    RelativeLayout rlEditSex;
    @InjectView(R.id.cha_kan_more)
    TextView chaKanMore;
    @InjectView(R.id.rl_edit_other)
    RelativeLayout rlEditOther;
    @InjectView(R.id.fl_birthday)
    FrameLayout flBirthday;
    @InjectView(R.id.fl_emil)
    FrameLayout flEmil;
    BirthdayFragment birthdayFragment;
    EmilFragment emilFragment;
    String items[]={"生日","邮箱"};
    String photoUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        ButterKnife.inject(this);
        birthdayFragment=new BirthdayFragment();
        emilFragment=new EmilFragment();
        myApplication = (MyApplication) getApplication();
        //设置主标题
        informationToolbar.setTitle("");
        //设置导航图标
        informationToolbar.setNavigationIcon(R.mipmap.backs);
        //设置actionBar为toolBar
        setSupportActionBar(informationToolbar);
        Log.i("TAG", "onCreate myApplication.getUser()=" + myApplication.getUser());
        if (myApplication.getUser() != null) {
            if (myApplication.getUser().getNumber()!=null){
                value = myApplication.getUser().getNumber();
                tvDiphone.setText(value);
            }
            if (myApplication.getUser().getName()!=null){
                tvDsname.setText(myApplication.getUser().getName());
            }
            if (myApplication.getUser().getAge()>0){
                tvDsage.setText(myApplication.getUser().getAge() + "");
            }
            if (myApplication.getUser().getSex()==0){
                tvDssex.setText("女");
            }else if (myApplication.getUser().getSex()==1){
                tvDssex.setText("男");
            }else {
                tvDssex.setText("请编辑性别");
            }
            if (myApplication.getUser().getPhoto()!=null){
                //头像赋值
                ImageOptions imageOptions=new ImageOptions.Builder()
                        //设置加载过程的图片
                        .setLoadingDrawableId(R.mipmap.ic_launcher)
                        //设置加载失败后的图片
                        .setFailureDrawableId(R.mipmap.ic_launcher)
                        //设置使用圆形图片
                        .setCircular(true)
                        //设置支持gif
                        .setIgnoreGif(true).build();
                photoUrl = StringUtil.ip+"/" + myApplication.getUser().getPhoto();
                x.image().bind(ivInformation, photoUrl,imageOptions);
            }
        }
        Log.d("TAG", "PersonalInformationActivity value=" + value);
        //设置toolBar的导航图标点击事件
        informationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "PersonalInformationActivity 返回到MainActivity");
                //返回到MainActivity
                finish();
            }
        });
    }

    //设置toolbar显示内容
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    //点击事件(右上角图标)
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_information) {
            Log.i("TAG", "111111111");
            //跳转（电话号码传过去）
            Intent intent = new Intent(PersonalInformationActivity.this, EditActivity.class);
            startActivityForResult(intent,13);
        }
        return true;
    }

    @OnClick(R.id.rl_edit_other)
    public void onClick() {
        //弹出对话框
        new AlertDialog.Builder(this).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //显示出生日
                        FragmentManager fragmentManager=getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fl_birthday,birthdayFragment).commit();
                        break;
                    case 1:
                        //显示出邮箱
                        FragmentManager fragmentManager2=getSupportFragmentManager();
                        fragmentManager2.beginTransaction().replace(R.id.fl_emil,emilFragment).commit();
                        break;
                }
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==13){
            if (data.getParcelableExtra("user")!=null){
                User user=data.getParcelableExtra("user");
                Log.i("PersonalInformaty", "onActivityResult  :"+user);
                if (user.getNumber()!=null){
                    tvDiphone.setText(user.getNumber());
                }else {
                    tvDiphone.setText("请编辑号码");
                }
                if (user.getName()!=null){
                    tvDsname.setText(user.getName());
                }else {
                    tvDsname.setText("请编辑姓名");
                }
                if (user.getAge()>0){
                    tvDsage.setText(user.getAge() + "");
                }else {
                    tvDsage.setText("请编辑年龄");
                }
                if (user.getSex()==0){
                    tvDssex.setText("女");
                }else if (user.getSex()==1){
                    tvDssex.setText("男");
                }else {
                    tvDssex.setText("请编辑性别");
                }
                if (user.getPhoto()!=null){
                    //头像赋值
                    ImageOptions imageOptions=new ImageOptions.Builder()
                            //设置加载过程的图片
                            .setLoadingDrawableId(R.mipmap.ic_launcher)
                            //设置加载失败后的图片
                            .setFailureDrawableId(R.mipmap.ic_launcher)
                            //设置使用圆形图片
                            .setCircular(true)
                            //设置支持gif
                            .setIgnoreGif(true).build();
                    photoUrl = StringUtil.ip+"/"+user.getPhoto();
                    x.image().bind(ivInformation, photoUrl,imageOptions);
                }

            }
        }
    }
}
