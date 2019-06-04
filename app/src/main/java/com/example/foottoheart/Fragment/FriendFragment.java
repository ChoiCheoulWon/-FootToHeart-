package com.example.foottoheart.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.foottoheart.FriendCumulativeActivity;
import com.example.foottoheart.MainActivity;
import com.example.foottoheart.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FriendFragment extends Fragment {

    List<String> mFriendList;
    ArrayAdapter<String> mArrayAdapter;
    ListView mListView;

    String line = "";
    FloatingActionButton mAddfriend;
    private String UserId;
    String friendid;
    private int count = 0;
    String[] friend;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_friend,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mFriendList = new ArrayList<>();
        UserId = ((MainActivity)getActivity()).UserId;
        String url = "http://34.216.194.87:3000/getfriend"+ "/" + UserId;
        Log.i("Test", "URL = " + url);
        new JSONTask().execute(url);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initAdapter();
        initListView();




        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Test",""+mFriendList.get(position));
                Log.i("click","클릭");
                Intent intent = new Intent(getContext(), FriendCumulativeActivity.class);
                intent.putExtra("friendid",mFriendList.get(position));
                startActivity(intent);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("click","롱클릭");
                return true;
            }
        });

        mAddfriend = (FloatingActionButton)getView().findViewById(R.id.fragmentfriend_floatingactionbutton_addfriend);
        mAddfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog를 띄워서 친구 아이디를 검색해준다.
                addfrienddialog();
                Log.i("test","버튼클릭");
            }
        });
    }

    private void addfrienddialog() {
        final EditText edittext = new EditText(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("친구추가");
        builder.setMessage("추가할 친구의 ID를 입력해주세요.");
        builder.setView(edittext);

        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        friendid = edittext.getText().toString();
                        Log.i("dialog",friendid);
                        //Toast.makeText(getApplicationContext(),edittext.getText().toString() ,Toast.LENGTH_LONG).show();
                        // db에서 이 사용자 아이디가 있으면 리스트에 추가해준다.

                        String url = "http://34.216.194.87:3000/friend"+ "/" + UserId + "/" + friendid;
                        Log.i("Test", "URL = " + url);
                        new JSONTask2().execute(url);

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // db에 없을경우 등록취소 dialog를 띄워준다.
                                Log.i("Count2",""+count);
                                if(count == -1){
                                    // 등록된 아이디가 없는경우
                                    faildialog();
                                }
                                else{
                                    boolean flag_success = true;
                                    // 등록된 아이디가 있는경우 , 이미 친구로 등록되어있는경우, 친구로 등록되어있지 않은 경우.
                                    for(String temp : mFriendList){
                                        if(temp.equals(friendid)){
                                            // 이미 추가된 친구에 같은 이름이 있음
                                            // 중복 추가 다이얼로그 띄우기
                                            flag_success = false;
                                            dulpicatedialog();
                                            break;
                                        }
                                    }
                                    if(flag_success){
                                        mFriendList.add(friendid);
                                        mArrayAdapter.notifyDataSetChanged();
                                        successdialog();
                                    }

                                }
                    }
                });
        builder.show();
    }

    private void successdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("친구등록 성공");
        builder.setMessage("새로운 친구를 추가했습니다.");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
    private void dulpicatedialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("친구등록 실패");
        builder.setMessage("이미 등록된 친구입니다.");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addfrienddialog();
                    }
                });
        builder.show();
    }
    private void faildialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("친구추가에 실패했습니다.");
        builder.setMessage("정확한 친구의 아이디를 입력해주세요. (본인의 아이디 추가 불가능)");
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("dialog","예를 선택했습니다.");
                    }
                });
        builder.show();
    }

    private void initListView() {
        mListView = (ListView)getView().findViewById(R.id.fragmentfriend_listview_friendlist);
        mListView.setAdapter(mArrayAdapter);
    }

    private void initAdapter() {
        mArrayAdapter = new ArrayAdapter<>(getContext(),R.layout.list_friend,mFriendList);
    }



    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL(urls[0]);//url을 가져온다.
                    con = (HttpURLConnection) url.openConnection();
                    con.connect();//연결 수행

                    //입력 스트림 생성
                    InputStream stream = con.getInputStream();

                    //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
                    reader = new BufferedReader(new InputStreamReader(stream));

                    //실제 데이터를 받는곳
                    StringBuilder buffer = new StringBuilder();


                    //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.
                    while((line = reader.readLine()) != null){
                        // 각 친구 list가 올것임.
                        line = line.substring(1,line.length()-1);
                        mFriendList.add(line);
                        buffer.append(line);
                        buffer.append(System.getProperty("line.separator"));
                    }


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

    public class JSONTask2 extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... urls) {
            try {

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
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
