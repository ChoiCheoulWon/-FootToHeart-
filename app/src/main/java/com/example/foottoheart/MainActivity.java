package com.example.foottoheart;

/*
* 풀 리퀘스트 테스트용 주석
* 이전에 풀 리퀘스트가 있고, 그 이후에 커밋을 하였을 때 이전 풀리퀘스트와 이후 커밋한 프로젝트가 합병이 가능한지 테스트
*
* */
/*
    master에서의 작업 커밋
 */
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.foottoheart.Fragment.CumulativeFragment;
import com.example.foottoheart.Fragment.FriendFragment;
import com.example.foottoheart.Fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fm = getSupportFragmentManager();
    private HomeFragment mHomeFragment = new HomeFragment();
    private CumulativeFragment mCumulativeFragment = new CumulativeFragment();
    private FriendFragment mFriendFragment = new FriendFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.mainactivity_bottomnavigationview);

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
                // 상품
            case R.id.mainactivity_menu_item_present:
                Toast.makeText(getApplicationContext(),"상품 클릭",Toast.LENGTH_LONG).show();
                break;
                // 건강 정보
            case R.id.mainactivity_menu_item_healthinfo:
                Toast.makeText(getApplicationContext(),"건강 정보 클릭",Toast.LENGTH_LONG).show();

                break;
                // 수신함
            case R.id.mainactivity_menu_item_inbox:
                Toast.makeText(getApplicationContext(),"수신함 클릭",Toast.LENGTH_LONG).show();

                break;
                // 설정
            case R.id.mainactivity_menu_item_setting:
                Toast.makeText(getApplicationContext(),"설정 클릭",Toast.LENGTH_LONG).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


}
