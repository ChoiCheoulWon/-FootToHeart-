package com.example.foottoheart.News;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foottoheart.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final List<Newsform> mNewsform;


    public interface NewNewsRecyclerViewClickListener{
        void onItemClicked(int position);
        void onNewsViewtextClicked(int position);
    }

    private NewNewsRecyclerViewClickListener mListener;

    public void setOnClickListener(NewNewsRecyclerViewClickListener listener){
        mListener = listener;
    }


    public NewsAdapter(List<Newsform> newsform){
        mNewsform = newsform;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_news,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Newsform form = mNewsform.get(i);
        viewHolder.newsimage.setImageResource(form.getImagenumber()); // 이미지 넣는것...
        viewHolder.title.setText(form.getTitle());

        if(mListener != null){
            final int pos = i;
            viewHolder.newsview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onNewsViewtextClicked(pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mNewsform.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView newsimage;
        TextView title;
        TextView newsview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsimage = itemView.findViewById(R.id.activitynewscrawling_imageview_image);
            title = itemView.findViewById(R.id.activitynewscrawling_textview_title);
            newsview = itemView.findViewById(R.id.activitynewscrawling_textview_url);
        }
    }

}
