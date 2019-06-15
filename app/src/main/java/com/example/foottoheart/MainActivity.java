package com.example.foottoheart;

import android.content.Intent;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.foottoheart.Fragment.CumulativeFragment;
import com.example.foottoheart.Fragment.FriendFragment;
import com.example.foottoheart.Fragment.HomeFragment;
import com.example.foottoheart.News.NewscrawlingActivity;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fm = getSupportFragmentManager();
    private HomeFragment mHomeFragment = new HomeFragment();
    private CumulativeFragment mCumulativeFragment = new CumulativeFragment();
    private FriendFragment mFriendFragment = new FriendFragment();

    public String UserId;
    TextView mUserid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.mainactivity_bottomnavigationview);


        Intent intent = getIntent();
        UserId = intent.getStringExtra("UserId");

        mUserid = (TextView)findViewById(R.id.mainactivity_UserId);
        mUserid.setText(UserId + " 님");

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.mainactivity_framelayout,mHomeFragment).commit();



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = fm.beginTransaction();

                switch(menuItem.getItemId()){

                    case R.id.action_home:
                        transaction.replace(R.id.mainactivity_framelayout,mHomeFragment).commit();
                        break;

                    case R.id.action_Cumulative:
                        transaction.replace(R.id.mainactivity_framelayout,mCumulativeFragment).commit();
                        break;

                    case R.id.action_friend:
                        transaction.replace(R.id.mainactivity_framelayout,mFriendFragment).commit();
                        break;
                }

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            // 건강 정보
            case R.id.mainactivity_menu_item_healthinfo:
                Intent intent = new Intent(getApplicationContext(), NewscrawlingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            // 로그아웃
            case R.id.mainactivity_menu_item_logout:
                SharedPreferences preferences = getSharedPreferences("ID", MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("ID", null);
                editor.commit();
                Intent intent_first = new Intent(getApplicationContext(),FirstActivity.class);
                intent_first.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_first);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
