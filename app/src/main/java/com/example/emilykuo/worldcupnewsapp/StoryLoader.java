package com.example.emilykuo.worldcupnewsapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.content.AsyncTaskLoader;
import java.util.List;

public class StoryLoader extends AsyncTaskLoader<List<Story>> {

    private static final String LOG_TAG = MainActivity.class.getName();
    private String mURL;

    public StoryLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Nullable
    @Override
    public List<Story> loadInBackground() {

        if (mURL == null) {
            return null;
        }
        List<Story> stories = QueryUtils.extractStories(mURL);
        return stories;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
