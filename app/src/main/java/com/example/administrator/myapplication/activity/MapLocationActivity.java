package com.example.administrator.myapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 地图定位
 */
public class MapLocationActivity extends AppCompatActivity {

    @InjectView(R.id.map_toolbar)
    Toolbar mapToolbar;
    @InjectView(R.id.et_map)
    EditText etMap;
    @InjectView(R.id.but_map)
    Button butMap;
    @InjectView(R.id.map_view)
    MapView mapView;
    @InjectView(R.id.lv_map)
    ListView lvMap;
    Button butDianJi;
    private final int REQUESTCODE = 1;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListener myListener = new MyLocationListener();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map_location);
        butDianJi= (Button) findViewById(R.id.but_dian_ji);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        butDianJi.setText("普通");
        ButterKnife.inject(this);
        //设置导航图标
        mapToolbar.setNavigationIcon(R.mipmap.backs);
        //设置主标题
        mapToolbar.setTitle("");
        //设置actionBar为toolBar
        setSupportActionBar(mapToolbar);
        //设置toolBar的导航图标点击事件
        mapToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到添加服务地址
                finish();

            }
        });
        QuanXian();
    }

    //权限申请
    public void QuanXian() {
        // String weizhi = Manifest.permission.ACCESS_COARSE_LOCATION;
        String weizhi2 = Manifest.permission.ACCESS_FINE_LOCATION;
        String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        //用来适配不同的系统版本
        // int checkSelfPermission = ActivityCompat.checkSelfPermission(this, weizhi);
        int checkSelfPermission2 = ActivityCompat.checkSelfPermission(this, weizhi2);
        //this.checkSelfPermission(callPhonePer);
        if (checkSelfPermission2 != PackageManager.PERMISSION_GRANTED) {
            //权限没有申请过
            ActivityCompat.requestPermissions(this, permission, REQUESTCODE);
        } else {
            //权限申请通过了
            initLocation();
        }
    }

    //权限申请回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTCODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                initLocation();
            } else {
                Toast.makeText(MapLocationActivity.this, "禁止定位...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initLocation() {
        // 地图初始化
        mBaiduMap = mapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(myListener);
        mLocClient.start();

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocClient.setLocOption(option);
    }
    @OnClick({R.id.but_map, R.id.but_dian_ji})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_map:
                //返回到前一个界面
                Intent intent = new Intent();
                intent.putExtra("addressName",etMap.getText().toString());
                setResult(RESULT_FIRST_USER, intent);
                finish();
                break;
            case R.id.but_dian_ji:
                switch (mCurrentMode) {
                    case NORMAL:
                        butDianJi.setText("跟随");
                        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        break;
                    case COMPASS:
                        butDianJi.setText("普通");
                        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        break;
                    case FOLLOWING:
                        butDianJi.setText("罗盘");
                        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        break;
                    default:
                        break;
                }
                break;
        }
    }
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mapView == null) {
                return;
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            Log.i("MyLocationListenner", "onReceiveLocation  :" + location.getAddrStr());

            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                etMap.setText(location.getAddrStr());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            final List<String> ls = new ArrayList<String>();//给listView设置数据源
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    ls.add(p.getName());
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(MapLocationActivity.this, R.layout.lh_poi_dizhi,R.id.tv_dian,ls);
            lvMap.setAdapter(arrayAdapter);
            //listview的Item点击事件
            lvMap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //返回到前一个界面
                    Intent intent = new Intent();
                    intent.putExtra("addressName", ls.get(position));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            Log.i("BaiduLocationApiDem", sb.toString());
        }

    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }
}
