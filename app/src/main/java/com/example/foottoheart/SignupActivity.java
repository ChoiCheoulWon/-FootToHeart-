package com.example.foottoheart;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignupActivity extends AppCompatActivity {

    int isexist = 0;

    EditText msignupid;
    Button mSignup;
    public String UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        msignupid = (EditText)findViewById(R.id.activitysignup_edittext_id);
        Button mSignup = (Button)findViewById(R.id.activitysignup_button_signup);
        mSignup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                UserId = msignupid.getText().toString();

                String url = "http://34.216.194.87:3000/add"+ "/" + UserId;
                Log.i("Test", "URL = " + url);
                new JSONTask().execute(url);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Test","isexist = " + isexist);
                        if(isexist == -1) {
                            Toast.makeText(getApplicationContext(),"이미 동일한 아이디가 존재합니다.",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Intent gomain_intent = new Intent(getApplicationContext(), MainActivity.class);
                            gomain_intent.putExtra("UserId", UserId);
                            startActivity(gomain_intent);
                        }
                    }
                },1000);
            }
        });



        //new Thread(new ConnectThread("192.168.43.117", 8888)).start();

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
                        /*
                            {"id":0,"nickname':"abc","left_num:1,"right_num:0,"time":"2019-05-30T14:13:09.000Z","todal":null}
                            과 같은 식의 데이터 들어옴.

                         */
                    }
                    Log.i("line",buffer.toString());
                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String... urls) 니까


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
