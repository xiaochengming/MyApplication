package com.example.administrator.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.ActivityChooserView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.Application.MyApplication;

import com.example.administrator.myapplication.CitySelectActivity;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.User;
import com.example.administrator.myapplication.fragment.ExitLoginFragment;
import com.example.administrator.myapplication.fragment.FuWuFragment;
import com.example.administrator.myapplication.fragment.MIMainShequFragment;
import com.example.administrator.myapplication.fragment.MainPageFragment;
import com.example.administrator.myapplication.fragment.MiMyTieFragment;
import com.example.administrator.myapplication.fragment.MiSheQuFragment;
import com.example.administrator.myapplication.fragment.OrderFragment;
import com.example.administrator.myapplication.fragment.PaiHangFragment;
import com.example.administrator.myapplication.fragment.RemindFragment;
import com.example.administrator.myapplication.util.StringUtil;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import c.b.BP;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    List<Fragment> fragmentList = new ArrayList<>();
    ViewPager vpMain;
   public RadioGroup radioGMain;
    TextView tvHeader;
    View headView;
    ImageView ivHeader;
    Integer getId = null;
    String userPhone = null;
    MyApplication myApplication;
    String userHang = null;
    String nowcity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化支付接口
        BP.init(this, "7a1eec4546fa4ab022fefc310d801643");
        setContentView(R.layout.activity_main);
        myApplication = (MyApplication) getApplication();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (myApplication.isFlag() == true) {
            getId = myApplication.getUser().getUserId();
            userPhone = myApplication.getUser().getNumber();
            if (myApplication.getUser().getPhoto() != null) {
                Log.i("MainActivity", "userPhone  :"+myApplication.getUser().getPhoto());
                userHang = StringUtil.ip + "/" + myApplication.getUser().getPhoto();
            }
        }

        Log.i("MainActivity", "onCreate  userPhone:" + userPhone);

        Log.i("TAG", "onCreate  getId=" + getId + ",userPhone=" + userPhone);

        vpMain = (ViewPager) findViewById(R.id.vp_main);
        radioGMain = (RadioGroup) findViewById(R.id.radioG_main);
        fragmentList.add(new MainPageFragment());
        fragmentList.add(new PaiHangFragment());
        fragmentList.add(new FuWuFragment());
        fragmentList.add(new MIMainShequFragment());

        vpMain.setAdapter(new MyFragmentAdapterextends(getSupportFragmentManager()));
        //页面切换
        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //改变radio选择状态
                RadioButton radioButton = (RadioButton) radioGMain.getChildAt(position);
                radioButton.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //radiogroup 点击事件
        radioGMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int nowIndex = 0;
                switch (checkedId) {
                    case R.id.rbtn_shou:
                        //首页
                        nowIndex = 0;
                        break;

                    case R.id.rbtn_paihang:
                        //排行
                        nowIndex = 1;
                        break;
                    case R.id.rbtn_fuwu:
                        //急救
                        nowIndex = 2;
                        break;
                    case R.id.rbtn_orde:
                        //订单
                        nowIndex = 3;
                        break;
                }

                vpMain.setCurrentItem(nowIndex);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //设置navigationView下面部分（menu）的item点击事件
        navigationView.setNavigationItemSelectedListener(this);

        //获取navigationView的头部布局（headerLayout）
        headView = navigationView.getHeaderView(0);
        //获取headerLayout布局中的控件
        //头像赋值
        ivHeader = (ImageView) headView.findViewById(R.id.imageView);
        if (userHang != null) {
            //头像赋值
            ImageOptions imageOptions = new ImageOptions.Builder()
                    //设置加载过程的图片
                    .setLoadingDrawableId(R.mipmap.touxiang)
                    //设置加载失败后的图片
                    .setFailureDrawableId(R.mipmap.touxiang)
                    //设置使用圆形图片
                    .setCircular(true)
                    //设置支持gif
                    .setIgnoreGif(true).build();
            x.image().bind(ivHeader, userHang, imageOptions);
        }
        //文字控件
        tvHeader = (TextView) headView.findViewById(R.id.tv_header);

        //设置TextView的点击事件


        Log.i("TAG", "MainActivity tvHeader" + tvHeader.getText().toString());
        //不需要再次登录
        Log.i("TAG", "不需要再次登录");
        tvHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getId != null) {
                } else {

                    Log.i("TAG", "跳转到登录界面");
                    //跳转到登录界面
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 111);
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    //菜单设计
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    //右菜单点击点击事件
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, CitySelectActivity.class);
            if (nowcity == null) {
                intent.putExtra("city", item.getTitle());
            } else {
                intent.putExtra("city", nowcity);
            }
            startActivityForResult(intent, 200);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    //item 点击事件
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_personal) {
            //需要判断是否已登录，没登录时点击后不能跳转（弹出一条请登录的消息）
            if (getId != null) {
                // Handle the camera action
                Log.i("TAG", "onNavigationItemSelected: 11");
                //跳转到个人信息界面，先从数据库取出对象的值
                //第一次登录个人信息就只有电话号码，其他信息完善
                //当用户已经注册，并且已经完善过个人信息，就需要把资料传过去
                //传值就只有电话号码
                //获取电话号码
                //获取控件值
                Intent intent = new Intent(MainActivity.this, PersonalInformationActivity.class);
                //跳转
                startActivity(intent);
            } else {
                Log.i("TAG", "再次跳转到登录界面");
                //跳转到登录界面
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, 111);
            }

        } else if (id == R.id.nav_address) {
            //需要判断是否已登录，没登录时点击后不能跳转（弹出一条请登录的消息）
            if (getId != null) {
                //跳转到地址界面
                //判断是否为首次添加地址，是：跳转到首次添加地址界面；否：跳转到添加地址界面（添加过的地址
                // 显示在listview上面，可以再次添加）
                //首次
                Intent intent = new Intent(MainActivity.this, AddressActivity.class);
                startActivity(intent);
                Log.i("ming", "onNavigationItemSelected: 22");
            } else {
                Log.i("TAG", "再次跳转到登录界面");
                //跳转到登录界面
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, 111);
            }
        } else if (id == R.id.nav_worker) {
            //需要判断是否已登录，没登录时点击后不能跳转（弹出一条请登录的消息）
            if (getId != null) {
                //跳转到常用阿姨界面（从数据库获取值显示在listview上面）
                Intent intent = new Intent(MainActivity.this, CommonAuntActivity.class);
                startActivity(intent);
                Log.i("ming", "onNavigationItemSelected: 33");
            } else {
                Log.i("TAG", "再次跳转到登录界面");
                //跳转到登录界面
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, 111);
            }
        } else if (id == R.id.nav_set) {
            Log.i("ming", "onNavigationItemSelected: 44");
            //跳转到设置界面
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_xiao_xi) {
            //跳转到消息通知
            Intent intent = new Intent(this, HuifuActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_dingdan) {
            //跳到订单界面
            Intent intent = new Intent(this, MyOrderActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class MyFragmentAdapterextends extends FragmentPagerAdapter {

        public MyFragmentAdapterextends(FragmentManager f) {
            super(f);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("MainActivity", "onActivityResult: " + requestCode + "-->" + resultCode);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            ActionMenuItemView menu = (ActionMenuItemView) findViewById(R.id.action_settings);
            nowcity = data.getStringExtra("city");
            menu.setTitle(data.getStringExtra("city"));
            Log.i("MainActivity", "onActivityResult: " + data.getStringExtra("city"));
            return;
        }
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //发表帖子回调
            MIMainShequFragment miMainShequFragment = (MIMainShequFragment) fragmentList.get(3);
            final MiSheQuFragment miSheQuFragment = (MiSheQuFragment) miMainShequFragment.fragment[0];
            miSheQuFragment.pageNum = 1;
            miSheQuFragment.initData();
            return;
        }
        if (requestCode == 300 && resultCode == RESULT_OK) {
            //我的帖子发表帖子回调
            MIMainShequFragment miMainShequFragment = (MIMainShequFragment) fragmentList.get(3);
            MiMyTieFragment miMyTieFragment = (MiMyTieFragment) miMainShequFragment.fragment[1];
            miMyTieFragment.pageNum = 1;
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            miMyTieFragment.initData();
            return;
        }
        //登录后回调
        if (resultCode == RESULT_OK && requestCode == 111) {
            if (myApplication.isFlag() == true) {
                if (data.getParcelableExtra("user") != null) {
                    User user = data.getParcelableExtra("user");
                    getId = user.getUserId();
                    if (user.getNumber() != null) {
                        tvHeader.setText(user.getNumber());
                    } else {
                        tvHeader.setText("请登录");
                    }
                    if (user.getPhoto() != null) {
                        //头像赋值
                        ImageOptions imageOptions = new ImageOptions.Builder()
                                //设置加载过程的图片
                                .setLoadingDrawableId(R.mipmap.touxiang)
                                //设置加载失败后的图片
                                .setFailureDrawableId(R.mipmap.touxiang)
                                //设置使用圆形图片
                                .setCircular(true)
                                //设置支持gif
                                .setIgnoreGif(true).build();
                        userHang = StringUtil.ip + "/" + user.getPhoto();
                        x.image().bind(ivHeader, userHang, imageOptions);
                    }
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            //注册后回调
            if (myApplication.isFlag() == true) {
                if (data != null && data.getParcelableExtra("user") != null) {
                    User user = data.getParcelableExtra("user");
                    getId = user.getUserId();
                    if (user.getNumber() != null) {
                        tvHeader.setText(user.getNumber());
                    } else {
                        tvHeader.setText("请登录");
                    }
                    if (user.getPhoto() != null) {
                        //头像赋值
                        ImageOptions imageOptions = new ImageOptions.Builder()
                                //设置加载过程的图片
                                .setLoadingDrawableId(R.mipmap.touxiang)
                                //设置加载失败后的图片
                                .setFailureDrawableId(R.mipmap.touxiang)
                                //设置使用圆形图片
                                .setCircular(true)
                                //设置支持gif
                                .setIgnoreGif(true).build();
                        userHang = StringUtil.ip + "/" + user.getPhoto();
                        x.image().bind(ivHeader, userHang, imageOptions);
                    }
                }
            }
        } else if (resultCode == RESULT_FIRST_USER) {
            if (myApplication.isFlag() == true) {
                //修改密码后回调
                if (data.getParcelableExtra("user") != null) {
                    User user = data.getParcelableExtra("user");
                    getId = user.getUserId();
                    if (user.getNumber() != null) {
                        tvHeader.setText(user.getNumber());
                    } else {
                        tvHeader.setText("请登录");
                    }
                    if (user.getPhoto() != null) {
                        //头像赋值
                        ImageOptions imageOptions = new ImageOptions.Builder()
                                //设置加载过程的图片
                                .setLoadingDrawableId(R.mipmap.touxiang)
                                //设置加载失败后的图片
                                .setFailureDrawableId(R.mipmap.touxiang)
                                //设置使用圆形图片
                                .setCircular(true)
                                //设置支持gif
                                .setIgnoreGif(true).build();
                        userHang = StringUtil.ip + "/" + user.getPhoto();
                        x.image().bind(ivHeader, userHang, imageOptions);
                    }
                }
            }

        }
    }
    private static final String TAG = "LifeCycleActivity";
    private int param = 1;
    //Activity创建或者从后台重新回到前台时被调用
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart called.");
    }

    //Activity从后台重新回到前台时被调用
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart called.");
    }

    //Activity创建或者从被覆盖、后台重新回到前台时被调用
    @Override
    protected void onResume() {
        super.onResume();
        userHang=StringUtil.ip + "/" + myApplication.getUser().getPhoto();
        if (userHang != null) {
            //头像赋值
            ImageOptions imageOptions = new ImageOptions.Builder()
                    //设置加载过程的图片
                    .setLoadingDrawableId(R.mipmap.touxiang)
                    //设置加载失败后的图片
                    .setFailureDrawableId(R.mipmap.touxiang)
                    //设置使用圆形图片
                    .setCircular(true)
                    //设置支持gif
                    .setIgnoreGif(true).build();
            x.image().bind(ivHeader, userHang, imageOptions);
        }
        userPhone=myApplication.getUser().getNumber();
        if (userPhone!=null&&!userPhone.equals("")){
            tvHeader.setText(userPhone);
        }
        if (myApplication.getUser().getUserId()>0){
            getId=myApplication.getUser().getUserId();
        }
        Log.i(TAG, "onResume called.userHang:"+userHang+",getId:"+myApplication.getUser().getUserId());
    }

    //Activity窗口获得或失去焦点时被调用,在onResume之后或onPause之后
    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i(TAG, "onWindowFocusChanged called.");
    }*/

    //Activity被覆盖到下面或者锁屏时被调用
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause called.");
        //有可能在执行完onPause或onStop后,系统资源紧张将Activity杀死,所以有必要在此保存持久数据
    }

    //退出当前Activity或者跳转到新Activity时被调用
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop called.");
    }

    //退出当前Activity时被调用,调用之后Activity就结束了
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestory called.");
    }

    /**
     * Activity被系统杀死时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死.
     * 另外,当跳转到其他Activity或者按Home键回到主屏时该方法也会被调用,系统是为了保存当前View组件的状态.
     * 在onPause之前被调用.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("param", param);
        Log.i(TAG, "onSaveInstanceState called. put param: " + param);
        super.onSaveInstanceState(outState);
        userHang=StringUtil.ip + "/" + myApplication.getUser().getPhoto();
        if (userHang != null) {
            //头像赋值
            ImageOptions imageOptions = new ImageOptions.Builder()
                    //设置加载过程的图片
                    .setLoadingDrawableId(R.mipmap.touxiang)
                    //设置加载失败后的图片
                    .setFailureDrawableId(R.mipmap.touxiang)
                    //设置使用圆形图片
                    .setCircular(true)
                    //设置支持gif
                    .setIgnoreGif(true).build();
            x.image().bind(ivHeader, userHang, imageOptions);
        }
        userPhone=myApplication.getUser().getNumber();
        if (userPhone!=null&&!userPhone.equals("")){
            tvHeader.setText(userPhone);
        }
        if (myApplication.getUser().getUserId()>0){
            getId=myApplication.getUser().getUserId();
        }
        //getId=myApplication.getUser().getUserId();
        Log.i(TAG, "onResume called.userHang:"+userHang+",getId:"+myApplication.getUser().getUserId());
    }
}
