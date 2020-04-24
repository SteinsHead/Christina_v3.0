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

public class LoginCenter extends AppCompatActivity {

    private TextView main_title;
    private EditText user_input;
    private EditText password_input;
    private ImageView login_image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        finds();

        login_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    public void finds(){
        main_title = (TextView) findViewById(R.id.main_title);
        user_input = (EditText) findViewById(R.id.user_input);
        password_input = (EditText) findViewById(R.id.password_input);
        login_image = (ImageView) findViewById(R.id.login_image);
    }
}
