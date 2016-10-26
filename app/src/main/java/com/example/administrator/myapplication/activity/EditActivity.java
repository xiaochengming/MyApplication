package com.example.administrator.myapplication.activity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.myapplication.Application.MyApplication;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.User;
import com.example.administrator.myapplication.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 编辑个人信息
 */
public class EditActivity extends AppCompatActivity {

    @InjectView(R.id.iv_edit)
    ImageView ivEdit;
    @InjectView(R.id.tv_Head_portrait)
    TextView tvHeadPortrait;
    @InjectView(R.id.edit_framelayout)
    RelativeLayout editFramelayout;
    @InjectView(R.id.et_name)
    EditText etName;
    @InjectView(R.id.et_edit_phone)
    EditText etEditPhone;
    @InjectView(R.id.et_edit_age)
    EditText etEditAge;
    @InjectView(R.id.et_edit_sex)
    EditText etEditSex;
    @InjectView(R.id.edit_toolbar)
    Toolbar editToolbar;
    @InjectView(R.id.tv_edit_name)
    TextView tvEditName;
    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.tv_edit_phone)
    TextView tvEditPhone;
    @InjectView(R.id.textView2)
    TextView textView2;
    @InjectView(R.id.textView3)
    TextView textView3;
    @InjectView(R.id.tv_edit_age)
    TextView tvEditAge;
    @InjectView(R.id.textView5)
    TextView textView5;
    @InjectView(R.id.tv_edit_sex)
    TextView tvEditSex;
    @InjectView(R.id.textView4)
    TextView textView4;
    @InjectView(R.id.textView6)
    TextView textView6;
    @InjectView(R.id.textView7)
    TextView textView7;
    @InjectView(R.id.tv_edit_birthday)
    TextView tvEditBirthday;
    @InjectView(R.id.et_edit_birthday)
    EditText etEditBirthday;
    @InjectView(R.id.textView10)
    TextView textView10;
    @InjectView(R.id.tv_edit_emil)
    TextView tvEditEmil;
    @InjectView(R.id.et_edit_emil)
    EditText etEditEmil;
    @InjectView(R.id.textView11)
    TextView textView11;
    String userName = null;
    String userPhone = null;
    String userAge = null;
    //获取application对象
    MyApplication myApplication;
    RequestQueue requestQueue;
    String items[] = {"相册", "拍照"};
    public static final int SELECT_PIC = 11;
    public static final int TAKE_PHOTO = 12;
    public static final int CROP = 13;
    private File file;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.inject(this);
        myApplication = (MyApplication) getApplication();
        requestQueue = Volley.newRequestQueue(this);
        //设置主标题
        editToolbar.setTitle("");
        //设置导航图标
        editToolbar.setNavigationIcon(R.mipmap.backs);
        //设置actionBar为toolBar
        setSupportActionBar(editToolbar);
        //设置toolBar的导航图标点击事件
        editToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回到个人信息界面
                finish();
            }
        });

        Log.i("TAG", "onCreate   myApplication.getUser()=" + myApplication.getUser());
        if (myApplication.getUser() != null) {
            if (myApplication.getUser().getName()!=null){
                userName = myApplication.getUser().getName();
                etName.setText(userName);
            }
            if (myApplication.getUser().getNumber()!=null){
                userPhone = myApplication.getUser().getNumber();
                etEditPhone.setText(userPhone);
            }
            if (myApplication.getUser().getAge()>0){
                userAge = myApplication.getUser().getAge() + "";
                etEditAge.setText(userAge);
            }

            if (myApplication.getUser().getSex() == 0) {
                etEditSex.setText("女");
            } else if (myApplication.getUser().getSex() == 1){
                etEditSex.setText("男");
            }
            if (myApplication.getUser().getBirthday()!=null){
                if (dateToString(myApplication.getUser().getBirthday()).equals("1970-01-01")){
                    return;
                }else {
                    etEditBirthday.setText(dateToString(myApplication.getUser().getBirthday()));
                }
            }
            if (myApplication.getUser().getEmail()!=null){
                etEditEmil.setText(myApplication.getUser().getEmail());
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
                String photoUrl =StringUtil.ip+"/"+ myApplication.getUser().getPhoto();
                Log.i("EditActivity", "onCreate  photoUrl:"+photoUrl);
                x.image().bind(ivEdit,photoUrl,imageOptions);
            }
        }
        Log.i("EditActivity", "etEditBirthday: "+etEditBirthday.getText().toString());
        //日期选择
        etEditBirthday.setInputType(InputType.TYPE_NULL);
        final Calendar c = Calendar.getInstance();
        etEditBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        etEditBirthday.setText(DateFormat.format("yyyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        //判断是否有sd卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
            imageUri = Uri.fromFile(file);
        }
    }
    //date转换成String
    public String dateToString(Date date){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
    public String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(date) + ".png";
    }

    @OnClick({R.id.tv_Head_portrait, R.id.but_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_Head_portrait:
                //照片选择(修改头像)
                new AlertDialog.Builder(this).setTitle("请选择").setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //相册
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        "image/*");
                                startActivityForResult(intent, SELECT_PIC);
                                break;
                            case 1:
                                //拍照
                                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                startActivityForResult(intent2, TAKE_PHOTO);
                                break;
                        }
                    }
                }).show();
                break;
            case R.id.but_edit:
                //连接数据库
                //post提交方式
                StringRequest stringRequest = new StringRequest(Request.Method.POST,StringUtil.ip+"/EditServlet", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("EditActivity", "onResponse : "+etEditAge.getText().toString());

                        //个人信息完善成功
                        Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                        User user = gson.fromJson(response, User.class);
                        if (user != null) {
                            Log.i("TAG", "EditActivity user" + user);
                            //跳转回个人信息界面
                            Intent intent1 = new Intent();
                            myApplication.setUser(user);
                            intent1.putExtra("user",user);
                            setResult(RESULT_OK,intent1);
                            finish();
                            Toast.makeText(EditActivity.this, "完善成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(EditActivity.this, "完善失败", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("userId",myApplication.getUser().getUserId()+"");
                        map.put("name", etName.getText().toString());
                        map.put("number", etEditPhone.getText().toString());
                        map.put("age", etEditAge.getText().toString());
                        map.put("sex", etEditSex.getText().toString());
                        map.put("birthday", etEditBirthday.getText().toString());
                        map.put("emil", etEditEmil.getText().toString());
                        return map;
                    }
                };
                requestQueue.add(stringRequest);
                break;
        }
    }
    //显示图片，上传服务器
    public void showImage(Bitmap bitmap) {
        saveImage(bitmap);
        uploadImage();
    }
    //将bitmap保存在文件中
    public void saveImage(Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
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

    //上传图片
    public void uploadImage() {
        RequestParams requestParams = new RequestParams(StringUtil.ip + "/UploadImageServlet");
        requestParams.setMultipart(true);
        requestParams.addBodyParameter("userId",myApplication.getUser().getUserId()+"");
        requestParams.addBodyParameter("file", file);
        Log.i("EditActivity", "uploadImage : "+myApplication.getUser().getUserId());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("ModifyPersonI", "onSuccess  :" + result);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PIC:
                //相册中选择
                if (data != null) {
                    crop(data.getData());
                    Log.i("ModifyPerson", "onActivityResult  ");
                }
                break;
            case TAKE_PHOTO:
                //拍照选择
                crop(Uri.fromFile(file));
                break;
            case CROP:
                //裁剪
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bm = bundle.getParcelable("data");
                    //  bundle.putParceable("data",bm);
                    showImage(bm);
//				bm.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream());
                    ivEdit.setImageBitmap(bm);
                }
                break;
        }
    }
}
