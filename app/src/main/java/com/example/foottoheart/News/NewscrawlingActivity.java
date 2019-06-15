package com.example.foottoheart.News;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.foottoheart.R;

import java.util.ArrayList;
import java.util.List;

public class NewscrawlingActivity extends AppCompatActivity implements NewsAdapter.NewNewsRecyclerViewClickListener {

    List<Newsform> form = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newscrawling);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.activitynewscrawling_recyclerview_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);



        form.add(new Newsform(R.drawable.news1,"[5분 건강 톡톡] 장시간 앉아있으면 혈액순환 치명적…이코노미석 증후군", "http://d.kbs.co.kr/news/view.do?ncd=3147859"));
        form.add(new Newsform(R.drawable.news2,"'매일 운동해도, 12시간 이상 앉아 있으면 빨리 죽는다' 컬럼비아 의대","http://news.chosun.com/site/data/html_dir/2017/09/12/2017091202353.html"));
        form.add(new Newsform(R.drawable.noimage,"하루 8시간 앉아 있는 직장인, 종아리 뒤쪽 살피세요","https://news.v.daum.net/v/20131128090904129"));


        NewsAdapter adapter = new NewsAdapter(form);
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(this);



    }


    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onNewsViewtextClicked(int position) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(form.get(position).getUrl()));
        startActivity(intent);

    }
}
