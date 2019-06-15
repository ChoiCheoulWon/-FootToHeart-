package com.example.foottoheart;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class FriendCumulativeActivity extends AppCompatActivity {

    String friendid;
    TextView mfriendid;

    MaterialCalendarView materialCalendarView;
    ArcProgress arcProgress;
    TextView datetextview;

    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendcumulative);

        Intent intent = getIntent();
        friendid = intent.getExtras().getString("friendid");

        mfriendid = (TextView)findViewById(R.id.activityfriendcumulative_textview_friendid);
        mfriendid.setText("친구 " + friendid + "의 운동량");

        arcProgress = (ArcProgress)findViewById(R.id.activityfriendcumulative_arcprogress);
        datetextview = (TextView)findViewById(R.id.activityfriendcumulative_textview_date);

        materialCalendarView = (MaterialCalendarView)findViewById(R.id.activityfriendcumulative_calendar);
        String date = String.format("%04d-%02d-%02d", CalendarDay.today().getYear(),CalendarDay.today().getMonth()+1,CalendarDay.today().getDay());
        datetextview.setText(String.format("%04d년 %02d월 %02d일",CalendarDay.today().getYear(),CalendarDay.today().getMonth()+1,CalendarDay.today().getDay()));
        String url = "http://34.216.194.87:3000/month"+ "/" + date + "/" + friendid;

        new JSONTask().execute(url);
        arcProgress.setProgress(count);
        arcProgress.setBottomText("달성도 : "+ (int)(count/(float)arcProgress.getMax()*100) + "%");

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
        );

        materialCalendarView.setDateSelected(CalendarDay.today(),true);

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //임시
                datetextview.setText(String.format("%04d년 %02d월 %02d일",date.getYear(),date.getMonth()+1,date.getDay()));
                String Date = String.format("%04d-%02d-%02d",date.getYear(),date.getMonth()+1,date.getDay());
                String url = "http://34.216.194.87:3000/month"+ "/" + Date + "/" + friendid;
                new JSONTask().execute(url);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        arcProgress.setProgress(count);
                        arcProgress.setBottomText("달성도 : "+ (int)(count/(float)arcProgress.getMax()*100) + "%");
                    }
                },500);

            }
        });


    }

    public class SundayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SundayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    public class SaturdayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
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
                        count = Integer.parseInt(line);
                        if(count == -1) count = 0;
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
