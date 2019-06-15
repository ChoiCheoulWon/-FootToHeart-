package com.example.foottoheart;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    Button btSignin,btSignup;
    public static Activity mFirstActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mFirstActivity = FirstActivity.this;

        SharedPreferences preferences = getSharedPreferences("ID", MODE_PRIVATE);
        String  ID = preferences.getString("ID", null);

        if (ID != null) {
            Intent gomain_intent = new Intent(getApplicationContext(), MainActivity.class);
            gomain_intent.putExtra("UserId", ID);
            gomain_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(gomain_intent);
            finish();
        }

        //로그인 버튼 눌렀을 때 로그인 화면으로 이동
        btSignin = (Button)findViewById(R.id.firstactivity_button_signin);
        btSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin_intent = new Intent(getApplicationContext(),SigninActivity.class);
                signin_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(signin_intent);
            }
        });

        btSignup = (Button)findViewById(R.id.firstactivity_button_signup);
        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup_intet = new Intent(getApplicationContext(),SignupActivity.class);
                signup_intet.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(signup_intet);
            }
        });


    }
}
