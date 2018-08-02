package com.example.emilykuo.worldcupnewsapp;


import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Story>> {

    private static final int LOADER_ID = 1;
    private static final String GUARDIAN_URL = "https://content.guardianapis.com/search?";
    private StoryAdapter storiesAdapter;
    private ProgressBar bar;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = con.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            bar = (ProgressBar) findViewById(R.id.progress);
            bar.setVisibility(View.GONE);
            empty = (TextView) findViewById(R.id.empty);
            empty.setText(R.string.no_internet_connection);

        }

        ListView list = (ListView) findViewById(R.id.soccer_news);
        empty = (TextView) findViewById(R.id.empty);
        list.setEmptyView(empty);
        storiesAdapter = new StoryAdapter(this, new ArrayList<Story>());
        list.setAdapter(storiesAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Story current = storiesAdapter.getItem(i);
                Uri storyUri = Uri.parse(current.getArticleURL());
                Intent web = new Intent(Intent.ACTION_VIEW, storyUri);
                startActivity(web);
            }
        });

    }

    @Override
    public Loader<List<Story>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String date = sharedPref.getString(getString(R.string.settings_date_key), getString(R.string.settings_date_default));
        String TOdate = sharedPref.getString(getString(R.string.settings_TOdate_key), getString(R.string.settings_TOdate_default));
        Uri baseUri = Uri.parse(GUARDIAN_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendQueryParameter("q", "World Cup");
        builder.appendQueryParameter("show-tags", "contributor");
        builder.appendQueryParameter("api-key", "feea0f8f-8d39-4bcc-8fe5-a7be23092d91");
        builder.appendQueryParameter("from-date", date);
        builder.appendQueryParameter("to-date", TOdate);


        return new StoryLoader(this, builder.toString());

        //return new StoryLoader(this, GUARDIAN_URL);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        storiesAdapter.clear();
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Story>> loader, List<Story> stories) {
        bar = (ProgressBar) findViewById(R.id.progress);
        empty = (TextView) findViewById(R.id.empty);
        empty.setText(R.string.no_new_stories);
        storiesAdapter.clear();
        if (stories != null && !stories.isEmpty()) {
            storiesAdapter.addAll(stories);
        }

        bar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
