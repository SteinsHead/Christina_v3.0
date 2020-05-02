package com.example.christina_v30;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class LoginCenter extends AppCompatActivity {
    private static final int login_sign = 4;//设置MainActivity--SearchCenter的RequestCode

    private TextView main_title;
    private EditText user_input, password_input;
    private ImageView login_image, sign_up_image, set_image;
    private MyDatabaseHelper databaseHelper;
    private ImageUtils imageUtils;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        finds();

        databaseHelper = new MyDatabaseHelper(LoginCenter.this, "UserInfo", null, 3);

        login_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseHelper.getWritableDatabase();

                String id = user_input.getText().toString();
                String password = password_input.getText().toString();
                System.out.println("hahahahahahahahahahahaha!!!!!!!!!!!" + id);
                System.out.println("hahahahahahahahahahahaha!!!!!!!!!!!" + password);

                if(isExist(id, password)){ //若返回true说明用户名和密码都存在且正确，返回登录成功
                    Toast.makeText(LoginCenter.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    //登陆成功的情况下可以选取图片

                    //在返回值中改变该用户的名字
                    Intent intent = getIntent();
                    Bundle bundle = intent.getExtras();
                    bundle.putString("user", id);
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
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

        set_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加逻辑
                imageUtils.ByAlbum();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == ImageUtils.ALBUM_REQUEST_CODE){
            try {
                if(requestCode == -1){
                    Bitmap bitmap = imageUtils.decodeBitmap();
                    if(bitmap != null){
                        set_image.setImageBitmap(bitmap);
                    }
                }else {
                    if(imageUtils.ImageFile != null){
                        imageUtils.ImageFile.delete();
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
}
