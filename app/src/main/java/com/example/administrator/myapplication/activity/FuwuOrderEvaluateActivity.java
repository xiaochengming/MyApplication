package com.example.administrator.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Evaluate;
import com.example.administrator.myapplication.entity.Order;
import com.example.administrator.myapplication.util.StringUtil;
import com.example.administrator.myapplication.util.TimesTypeAdapter;
import com.example.administrator.myapplication.util.UrlAddress;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//评价页面
public class FuwuOrderEvaluateActivity extends AppCompatActivity {
    //上传图片
    public static final int SELECT_PIC = 11;
    public static final int TAKE_PHOTO = 12;
    public static final int CROP = 13;
    //
    int temp = 0;
    public static final Integer BACK = 2;
    Button button;
    Order order;
    //星级
    int numStar;
    //评价
    String eval;
    @InjectView(R.id.id_prod_list_iv_left)
    ImageView idProdListIvLeft;
    @InjectView(R.id.id_prod_list_tv)
    TextView idProdListTv;
    @InjectView(R.id.line1)
    LinearLayout line1;
    @InjectView(R.id.prod_list_item_iv)
    ImageView prodListItemIv;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.housekeeper_name)
    TextView housekeeperName;
    @InjectView(R.id.sex)
    TextView sex;
    @InjectView(R.id.housekeeper_sex)
    TextView housekeeperSex;
    @InjectView(R.id.age)
    TextView age;
    @InjectView(R.id.housekeeper_age)
    TextView housekeeperAge;
    @InjectView(R.id.serviceplevel)
    TextView serviceplevel;
    @InjectView(R.id.star_num)
    TextView starNum;
    @InjectView(R.id.imageView6)
    ImageView imageView6;
    @InjectView(R.id.rela1)
    RelativeLayout rela1;
    @InjectView(R.id.textView2)
    TextView textView2;
    @InjectView(R.id.rat_star)
    RatingBar ratStar;
    @InjectView(R.id.image_id_1)
    ImageView imageId1;
    @InjectView(R.id.image_id_2)
    ImageView imageId2;
    @InjectView(R.id.image_id_3)
    ImageView imageId3;
    @InjectView(R.id.textView13)
    TextView textView13;
    @InjectView(R.id.linear)
    LinearLayout linear;
    @InjectView(R.id.more_applyagent_content)
    EditText moreApplyagentContent;
    @InjectView(R.id.button_evaluate)
    Button buttonEvaluate;
    @InjectView(R.id.rela)
    RelativeLayout rela;
    private File file;
    private File file1;
    private File file2;

    private Uri imageUri;
    String items[] = {"相册选择", "拍照"};
    //图片
    Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_evaluate);
        ButterKnife.inject(this);
        //获取数据
        getData();
        //初始化控件
        initView();


    }

    //获取数据
    public void getData() {
        Intent intent1 = getIntent();
        String or = intent1.getStringExtra("Order");
        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        order = gson.fromJson(or, Order.class);
    }

    //初始化控件
    public void initView() {
        if (order.getHousekeeper() != null) {
            x.image().bind(prodListItemIv, StringUtil.ip + "/"+order.getHousekeeper().getHousePhoto());
            housekeeperName.setText(order.getHousekeeper().getName());
            if (order.getHousekeeper().getSex() == 1) {
                housekeeperSex.setText("男");
            } else {
                housekeeperSex.setText("女");
            }
            housekeeperAge.setText(order.getHousekeeper().getAge() + "岁");
            starNum.setText(order.getHousekeeper().getServiceplevel() + "");

        }

    }

    //file创建
    public void fileEstablish() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            switch (temp) {
                case 1:
                    file = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
                    imageUri = Uri.fromFile(file);
                    break;
                case 2:
                    file1 = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
                    imageUri = Uri.fromFile(file1);
                    break;
                case 3:
                    file2 = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());

                    imageUri = Uri.fromFile(file2);
                    break;
            }


        }

    }


    //传回订单页信息
    public void back() {

        Intent intent2 = new Intent();
        order.setState(4);
        intent2.putExtra("orderId", order.getOrderId() + "");
        intent2.putExtra("orderState", order.getState() + "");
        setResult(BACK, intent2);
        finish();//销毁当前activity (返回键)
    }

    //修改图片的宽度--》不修改imageView有可能会出现不显示
    public void checkImage(ImageView imageView, File file) {
        Bitmap bm = BitmapFactory.decodeFile(file + "");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        if (bm.getWidth() <= screenWidth) {
            imageView.setImageBitmap(bm);
        } else {
            Bitmap bmp = Bitmap.createScaledBitmap(bm, screenWidth, bm.getHeight() * screenWidth / bm.getWidth(), true);
            imageView.setImageBitmap(bmp);
        }
    }

    //上传图片
    public void uploadImage() {
        //返回星数
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //得到星级
        numStar = (int) ratStar.getRating();
        //评价的内容
        eval = moreApplyagentContent.getText().toString();
        Evaluate evaluate = new Evaluate(order, numStar, eval, timestamp, 0, 0);
        String ur1 = StringUtil.ip + "/EvaluateServlet";
        //评价内容发送到数据库
        final RequestParams requestParams = new RequestParams(ur1);
        Gson gson = new GsonBuilder().registerTypeAdapter(Time.class, new TimesTypeAdapter())
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String evaluateJson = gson.toJson(evaluate);
        requestParams.setMultipart(true);
        if (file != null) {
            requestParams.addBodyParameter("file", file);
        }
        if (file1 != null) {
            requestParams.addBodyParameter("file", file1);
        }
        if (file2 != null) {
            requestParams.addBodyParameter("file", file2);
        }
        requestParams.addBodyParameter("evaluateJson", evaluateJson);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("true")) {
                    back();
                } else {
                    Toast.makeText(FuwuOrderEvaluateActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
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

    //给图片命名
    public String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(date) + ".png";
    }

    public void photoOnListener() {
        //修改头像
        new AlertDialog.Builder(this).setTitle("请选择").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //相册选择
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image/*");

                        startActivityForResult(intent, SELECT_PIC);

                        break;
                    case 1:
                        //拍照获取
                        // api guide: cemera

                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        switch (temp) {
                            case 1:
                                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                break;
                            case 2:
                                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file1));
                                break;
                            case 3:
                                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file2));
                                break;
                        }

                        startActivityForResult(intent2, TAKE_PHOTO);
                        break;
                }
            }
        }).show();
    }

    //显示图片，上传服务器
    public void showImage(Bitmap bitmap) {
        saveImage(bitmap);
        switch (temp) {
            case 1:
                checkImage(imageId1, file);
                break;
            case 2:
                checkImage(imageId2, file1);
                break;

            case 3:
                checkImage(imageId3, file2);
                break;


        }


    }

    //将bitmap保存在文件中
    public void saveImage(Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            switch (temp) {
                case 1:
                    fos = new FileOutputStream(file);
                    break;
                case 2:
                    fos = new FileOutputStream(file1);
                    break;
                case 3:
                    fos = new FileOutputStream(file2);
                    break;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
    }

    //裁剪
    public void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case SELECT_PIC:
                //相册中选择
                if (data != null) {
                    crop(data.getData());

                }
                break;
            case TAKE_PHOTO:
                //拍照选择
                switch (temp) {
                    case 1:
                        crop(Uri.fromFile(file));
                        break;
                    case 2:
                        crop(Uri.fromFile(file1));
                        break;
                    case 3:
                        crop(Uri.fromFile(file2));
                        break;
                }

                break;
            case CROP:
                //裁剪
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    bm = bundle.getParcelable("data");
                    if (bm != null) {
                        showImage(bm);
                    }

                }
                break;
        }
    }

    @OnClick({R.id.image_id_1, R.id.image_id_2, R.id.image_id_3, R.id.button_evaluate, R.id.id_prod_list_iv_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_id_1:
                temp = 1;
                fileEstablish();
                photoOnListener();


                break;
            case R.id.image_id_2:
                temp = 2;
                fileEstablish();
                photoOnListener();
                //  im3.setImageBitmap(bm);
                break;
            case R.id.image_id_3:
                temp = 3;
                fileEstablish();
                photoOnListener();
                // im4.setImageBitmap(bm);
                break;
            case R.id.button_evaluate:
                uploadImage();

                break;
            case R.id.id_prod_list_iv_left:
                //后退
                finish();
                break;

        }
    }


}
