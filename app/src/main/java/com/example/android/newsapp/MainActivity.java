package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {

    ListView listView;
    ProgressBar mProgressBar;
    NewsAdapter mAdapter;
    EditText queryEditText;
    TextView emptyTextView;
    private static final int NEWS_LOADER_ID = 1;

    public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_items);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        listView.setAdapter(mAdapter);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        queryEditText = (EditText) findViewById(R.id.edit_text);

        emptyTextView = (TextView) findViewById(R.id.no_news_or_internet_found);

        listView.setEmptyView(emptyTextView);

        // Setup loader
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        // Set a click listener to open the news website when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News earthquake = mAdapter.getItem(position);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(earthquake.getWebUrl()));
                startActivity(i);
            }
        });


        Button search = (Button) findViewById(R.id.seach_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                mAdapter.clear();
                if (queryEditText.getText().toString().trim().matches("")) { //if search box is empty and button is clicked
                    emptyTextView.setVisibility(View.VISIBLE);
                    emptyTextView.setText(R.string.please_enter);
                } else {
                    if (networkInfo != null && networkInfo.isConnected()) {                     //if search box is NOT empty and network is connected
                        getLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
                    } else {                                        ////if search box is NOT empty and network is disconnected
                        emptyTextView.setVisibility(View.VISIBLE);
                        emptyTextView.setText(R.string.no_internet_found);
                    }
                }
            }
        });
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {

        String query = queryEditText.getText().toString();
        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error url encoding: ", e);
            query = "";
        }
        if (queryEditText.getText().toString().trim().matches("")) {
            emptyTextView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            emptyTextView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        String url = "https://content.guardianapis.com/search?q=" + query + "&order-by=newest&api-key=test&page-size=30&show-tags=contributor";
        return new NewsLoader(MainActivity.this, url);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> newsList) {
        mAdapter.clear();

        if (newsList != null && !newsList.isEmpty()) {
            mAdapter.addAll(newsList);
            emptyTextView.setVisibility(View.GONE);
            listView.setAdapter(mAdapter);
            listView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            if (queryEditText.getText().toString().trim().matches("")) {
                emptyTextView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            } else {
                emptyTextView.setText(getString(R.string.nothing_found));
                emptyTextView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.GONE);

            }
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        mAdapter.clear();
    }

}
