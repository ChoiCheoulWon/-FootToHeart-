package com.example.foottoheart.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.foottoheart.MainActivity;
import com.example.foottoheart.R;
import com.example.foottoheart.Esp8266communication;
import com.github.lzyzsd.circleprogress.ArcProgress;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeFragment extends Fragment {

    int count = 0;
    Button unocommunication;
    MainActivity mainActivity;

    /* 통신을 위한 변수*/
    TextView getText;
    Button btgetText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragmet_home,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button controller = (Button)getView().findViewById(R.id.controller);
        final ArcProgress arcProgress = (ArcProgress)getView().findViewById(R.id.fragmenthome_arcprogress);
        controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                arcProgress.setProgress(count);
                arcProgress.setBottomText("달성도 : "+ (int)(count/(float)arcProgress.getMax()*100) + "%");
            }
        });

        unocommunication = (Button)getView().findViewById(R.id.fragmenthome_button_communication);
        unocommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commu_intent =  new Intent(getActivity().getApplicationContext(), Esp8266communication.class);
                startActivity(commu_intent);

            }
        });


        /* nodejs와 통신 코드 연습 */

        getText = (TextView)getView().findViewById(R.id.fragmenthome_textviw_getnodejs);
        btgetText = (Button)getView().findViewById(R.id.fragmenthome_button_getnodejs);

        btgetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute("http://13.124.99.222:3000/users");
            }
        });




    }

    public class JSONTask extends AsyncTask<String, String, String>{

        @Override

        protected String doInBackground(String... urls) {


                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONObject jsonObject = new JSONObject();


            HttpURLConnection con = null;

                BufferedReader reader = null;


                    //URL url = new URL(“http://192.168.25.16:3000/users“);

            URL url = null;//url을 가져온다.
            try {
                url = new URL(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                con = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                con.connect();//연결 수행
            } catch (IOException e) {
                e.printStackTrace();
            }

            //입력 스트림 생성
             InputStream stream = null;
            try {
                stream = con.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
                    reader = new BufferedReader(new InputStreamReader(stream));

                    //실제 데이터를 받는곳

                    StringBuffer buffer = new StringBuffer();
                    //line별 스트링을 받기 위한 temp 변수

                    String line = "";

                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.

                    while(true){
                        try {
                            if (!((line = reader.readLine()) != null)) break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        buffer.append(line);
                    }
                    //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String… urls) 니까

                    return buffer.toString();

                    //아래는 예외처리 부분이다.




        }

        //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            getText.setText(result);
        }

    }
}
