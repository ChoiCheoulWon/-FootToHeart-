package com.example.foottoheart.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.foottoheart.MainActivity;
import com.example.foottoheart.R;
import com.example.foottoheart.SigninActivity;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    static int count = 0;

    String UserId;

    int Set = 0;

    TimerTask timerTask;
    Timer timer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragmet_home,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        count = 0;
        UserId = ((MainActivity)getActivity()).UserId;

        Log.i("UserIdTest2",UserId);


        final ArcProgress arcProgress = (ArcProgress)getView().findViewById(R.id.fragmenthome_arcprogress);

/*
        String line = "{\"nickname\":\",\"gucheol\",\"time\":\"2019-06-01\",\"Total\":4}";
        // String 분석
        String nickname = line.split("\"")[4];
        String time = line.split("\"")[8];
        String total_s = line.split(":")[3];
        int temp = total_s.length();
        total_s = total_s.split("\\}")[0];
        int total = Integer.parseInt(total_s);
*/
        /* nodejs와 통신 코드 연습 */




        timerTask = new TimerTask() {
            @Override
            public void run() {
                //String Date = String.format("%04d-%02d-%02d", CalendarDay.today().getYear(),CalendarDay.today().getMonth()+1,CalendarDay.today().getDay());
                String url = "http://34.216.194.87:3000/users"+ "/" + UserId;
                Log.i("Test", "URL = " + url);
                new JSONTask().execute(url);
                Log.i("Test","1초마다 db에서 가져온다!" + Set++ + "Count = " + count);
                if(count == -1) count = 0;
                arcProgress.setProgress(count);
                arcProgress.setBottomText("달성도 : "+ (int)(count/(float)arcProgress.getMax()*100) + "%");
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,1000);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        timer.cancel();
    }

    public class JSONTask extends AsyncTask<String, String, String>{

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
                        count = Integer.parseInt(line);
                        Log.i("Count", "Count = " + count);
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
