package com.example.foottoheart;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class SignupActivity extends AppCompatActivity {

    int isexist = 0;

    EditText msignupid;
    Button mSignup;



    public String UserId;

    FirstActivity mFirstActivitiy = (FirstActivity)FirstActivity.mFirstActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        msignupid = (EditText)findViewById(R.id.activitysignup_edittext_id);
        mSignup = (Button)findViewById(R.id.activitysignup_button_signup);
        mSignup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                UserId = msignupid.getText().toString();

                String url = "http://34.220.25.253:3000/add"+ "/" + UserId;
                new JSONTask().execute(url);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isexist == -1) {
                            Toast.makeText(getApplicationContext(),"이미 동일한 아이디가 존재합니다.",Toast.LENGTH_LONG).show();
                        }
                        else{

                            // 로그인 화면에서 한번 더 거쳐서 로그인하는 코드
                            Toast.makeText(getApplicationContext(),"회원가입 완료, 로그인을 해주세요.",Toast.LENGTH_LONG).show();
                            Intent intent_gosignin = new Intent(getApplicationContext(),SigninActivity.class);
                            startActivity(intent_gosignin);
                            mFirstActivitiy.finish();
                            finish();

                            //바로 로그인이 되어서 메인화면으로 들어가지는 코드
                            /*
                            SharedPreferences preferences = getSharedPreferences("ID", MODE_PRIVATE);

                            SharedPreferences.Editor editor = preferences.edit();

                            editor.putString("ID", UserId);

                            if (editor.commit()) {
                                Toast.makeText(getApplicationContext(),"회원가입 완료, 로그인을 해주세요.",Toast.LENGTH_LONG).show();
                                Intent gomain_intent = new Intent(getApplicationContext(), MainActivity.class);
                                gomain_intent.putExtra("UserId", UserId);
                                gomain_intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(gomain_intent);
                                mFirstActivitiy.finish();
                                finish();
                            }
                            */

                        }
                    }
                },1000);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    public class JSONTask extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {
            try {

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.connect();//연결 수행

                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();

                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
                    reader = new BufferedReader(new InputStreamReader(stream));

                    //실제 데이터를 받는곳
                    StringBuilder buffer = new StringBuilder();

                    //line별 스트링을 받기 위한 temp 변수
                    String line = "";

                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.
                    while((line = reader.readLine()) != null){
                        isexist = Integer.parseInt(line);
                        buffer.append(line);
                        buffer.append(System.getProperty("line.separator"));

                    }

                    return buffer.toString();

                    //아래는 예외처리 부분이다.
                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //종료가 되면 disconnect메소드를 호출한다.
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        //버퍼를 닫아준다.
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//finally 부분
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);
        }

    }

}
