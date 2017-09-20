package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nikhil on 19-09-2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private Context context;

    public NewsAdapter(Context context, ArrayList<News> booksArrayList) {
        super(context, 0, booksArrayList);
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        News News = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title_textview_lv.setText(News.getNewsTitle());
        viewHolder.author_textview_lv.setText(News.getAuthorName());
        viewHolder.section_textview_lv.setText(News.getSectionName());
        viewHolder.date_section_lv.setText(News.getDate());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.news_title)
        TextView title_textview_lv;
        @BindView(R.id.news_author)
        TextView author_textview_lv;
        @BindView(R.id.section_name)
        TextView section_textview_lv;
        @BindView(R.id.publication_date)
        TextView date_section_lv;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
