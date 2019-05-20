package com.example.foottoheart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    Button btSignin,btSignup,btGomain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //로그인 버튼 눌렀을 때 로그인 화면으로 이동
        btSignin = (Button)findViewById(R.id.firstactivity_button_signin);
        btSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin_intent = new Intent(getApplicationContext(),SigninActivity.class);
                startActivity(signin_intent);

            }
        });

        btSignup = (Button)findViewById(R.id.firstactivity_button_signup);
        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup_intet = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(signup_intet);
            }
        });


        //임시버튼
        btGomain = (Button)findViewById(R.id.firstactivity_button_gomain);
        btGomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gomain_intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(gomain_intent);

            }
        });

    }
}
