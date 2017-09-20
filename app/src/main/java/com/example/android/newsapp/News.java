package com.example.android.newsapp;

/**
 * Created by nikhil on 19-09-2017.
 */

public class News {

    private String mNewsTitle;

    private String mAuthorName;

    private String mSectionName;

    private String mWebUrl;

    private String mDate;


    public News(String newstitle, String authorname, String sectionname, String weburl, String date) {
        this.mNewsTitle = newstitle;
        this.mAuthorName = authorname;
        this.mSectionName = sectionname;
        this.mWebUrl = weburl;
        this.mDate = date;
    }

    public String getNewsTitle() {
        return mNewsTitle;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getDate() {
        return mDate;
    }
}
