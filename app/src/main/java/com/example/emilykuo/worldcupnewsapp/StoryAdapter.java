package com.example.emilykuo.worldcupnewsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StoryAdapter extends ArrayAdapter<Story> {

    public static final String LOG_TAG = MainActivity.class.getName();

    /*
     *Custom constructor
     * @param context The current context to inflate the layout file
     * @param stories A list of Stories to display in list
     */

    public StoryAdapter(Activity context, ArrayList<Story> stories) {
        super(context, 0, stories);
    }

    /*
     *Provides a view for the ListView
     * @param position in the list of data to display in the view
     * @param convertView the recylced view to populate
     * @param parent The parent ViewGroup for inflation
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Story currentStory = getItem(position);

        String dateS = currentStory.getArticleDate();

        viewHolder.nameTextView.setText(currentStory.getArticleTitle());
        viewHolder.authorTextView.setText(currentStory.getArticleAuthor());
        viewHolder.sectionTextView.setText(currentStory.getArticleSection());
        viewHolder.dateTextView.setText(convertDate(dateS));

        return convertView;
    }

    /*
    Helper method to create date to Jan 18, 2018 for example
     */

    private String convertDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date d = format.parse(date);
            SimpleDateFormat serverFormat = new SimpleDateFormat("MMM dd yyyy");
            return serverFormat.format(d);
        } catch (Exception e) {
            Log.e(LOG_TAG, "ERROR WITH PARSING");
        }
        return "";
    }

    class ViewHolder {
        private TextView nameTextView;
        private TextView authorTextView;
        private TextView sectionTextView;
        private TextView dateTextView;

        public ViewHolder(@NonNull View view) {
            this.nameTextView = (TextView) view.findViewById(R.id.article_title);
            this.authorTextView = (TextView) view.findViewById(R.id.article_author);
            this.sectionTextView = (TextView) view.findViewById(R.id.article_section);
            this.dateTextView = (TextView) view.findViewById(R.id.article_date);
        }
    }
}

