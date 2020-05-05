package com.example.christina_v30;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class LoginCenter extends AppCompatActivity {
    private static final int login_sign = 4;//设置MainActivity--SearchCenter的RequestCode

    private TextView main_title;
    private EditText user_input, password_input;
    private ImageView login_image, sign_up_image, set_image;
    private MyDatabaseHelper databaseHelper;

    //
    private static final int ALBUM_REQUEST_CODE = 455;
    private static final int RESULT_REQUEST_CODE = 889;
    private static int output_X = 300;
    private static int output_Y = 300;
    private Bitmap photo;
    //

    private String id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        finds();

        databaseHelper = new MyDatabaseHelper(LoginCenter.this, "UserInfo", null, 4);

        main_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在返回值中改变该用户的名字
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                bundle.putString("user", id);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        login_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseHelper.getWritableDatabase();

                id = user_input.getText().toString();
                String password = password_input.getText().toString();
                System.out.println("hahahahahahahahahahahaha!!!!!!!!!!!" + id);
                System.out.println("hahahahahahahahahahahaha!!!!!!!!!!!" + password);

                if(isExist(id, password)){ //若返回true说明用户名和密码都存在且正确，返回登录成功
                    Toast.makeText(LoginCenter.this, "登录成功！", Toast.LENGTH_SHORT).show();

                    //登录成功的情况下可以显示头像
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    Cursor cursor = db.query("User", null, null, null, null, null, null);
                    if(cursor.moveToFirst()){
                        do{
                            if(cursor.getString(cursor.getColumnIndex("username")).equals(id)){
                                Glide.with(LoginCenter.this).load(cursor.getString(cursor.getColumnIndex("headimage"))).into(set_image);
                                System.out.println("iamgeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + cursor.getString(cursor.getColumnIndex("headimage")));
                            }
                        }while (cursor.moveToNext());
                    }
                    cursor.close();
                    //登陆成功的情况下可以更改头像
                    set_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //添加逻辑
                            FromAlbum();
                        }
                    });

                }
                else { //说明用户名不匹配或用户不存在，返回登录失败
                    Toast.makeText(LoginCenter.this, "用户不存在或输入错误！", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    Bundle bundle = intent.getExtras();
                    bundle.putString("user", "暂时不知道是谁呢");
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

                if(id.equals("christina") || id.equals("Christina")){
                    Toast.makeText(LoginCenter.this, "欢迎回家，Administrator！", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginCenter.this, "welcome: "  + id + " !", Toast.LENGTH_SHORT).show();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });

        sign_up_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginCenter.this, SignUpCenter.class);
                startActivityForResult(intent, login_sign);
            }
        });

        Intent intent = getIntent();
        final String name = (String) intent.getStringExtra("user_name");
        if(!name.equals("暂时不知道是谁呢")){
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            Cursor cursor = db.query("User", null, null, null, null, null, null);
            if(cursor.moveToFirst()){
                do{
                    if(cursor.getString(cursor.getColumnIndex("username")).equals(name)){
                        Glide.with(LoginCenter.this).load(cursor.getString(cursor.getColumnIndex("headimage"))).into(set_image);
                        System.out.println("iamgeaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + cursor.getString(cursor.getColumnIndex("headimage")));
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();
        }



//        try {
//            FileInputStream fileInputStream = new FileInputStream("/storage/emulated/0/wife.png");
//            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
//            set_image.setImageBitmap(bitmap);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == RESULT_CANCELED){
            Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode){
            case ALBUM_REQUEST_CODE:
                cropPhoto(data.getData());
                break;
            case RESULT_REQUEST_CODE:
                if(data != null){
                    setImage(data, id);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void cropPhoto(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);

        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    public void setImage(Intent intent, String id){
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            photo = bundle.getParcelable("data");
            set_image.setImageBitmap(photo);
            //添加逻辑
            try {
                File dir = new File(getExternalFilesDir(null).getAbsolutePath());
                String filepath = dir + String.valueOf(System.currentTimeMillis()) + ".png";
                File file = new File(filepath);
                FileOutputStream outputStream = new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                db.execSQL("update User set headimage=? where username=?", new Object[]{filepath, id});
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void finds(){
        main_title = (TextView) findViewById(R.id.main_title);
        user_input = (EditText) findViewById(R.id.user_input);
        password_input = (EditText) findViewById(R.id.password_input);
        login_image = (ImageView) findViewById(R.id.login_image);
        sign_up_image = (ImageView) findViewById(R.id.sign_up_image);
        set_image = (ImageView) findViewById(R.id.set_image);
    }

    public boolean isExist(String id, String password){
        //如果用户已经存在，返回true，如果用户不存在，返回false
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.query("User", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(cursor.getColumnIndex("username")).equals(id)
                        &&cursor.getString(cursor.getColumnIndex("password")).equals(password)){
                    return true;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }

    private void FromAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }
}
