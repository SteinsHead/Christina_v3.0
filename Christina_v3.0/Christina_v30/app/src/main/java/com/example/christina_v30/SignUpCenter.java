package com.example.christina_v30;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpCenter extends AppCompatActivity {
    private TextView sign_title;
    private EditText user_sign, password_sign;
    private ImageView sign_back_image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);

        find_views();

        sign_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    public void find_views(){
        sign_title = (TextView) findViewById(R.id.sign_title);
        user_sign = (EditText) findViewById(R.id.user_sign);
        password_sign = (EditText) findViewById(R.id.password_sign);
        sign_back_image = (ImageView) findViewById(R.id.sign_back_image);
    }
}
