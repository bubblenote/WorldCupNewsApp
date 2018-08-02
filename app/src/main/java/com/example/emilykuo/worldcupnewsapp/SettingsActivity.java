package com.example.emilykuo.worldcupnewsapp;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.prefs.PreferenceChangeListener;

public class SettingsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class WorldCupPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            Preference date = findPreference(getString(R.string.settings_date_key));
            bindPreferenceSummaryToValue(date);
            Preference TOdate = findPreference(getString(R.string.settings_TOdate_key));
            bindPreferenceSummaryToValue(TOdate);
        }
        /*
        Updates the UI when the user changes the preference value
         */
        @Override
        public boolean onPreferenceChange(Preference prefer, Object value){
            String stringVal = value.toString();
            prefer.setSummary(stringVal);
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference prefer){
            prefer.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(prefer.getContext());
            String prefString = preferences.getString(prefer.getKey(), "");
            onPreferenceChange(prefer, prefString);
        }


    }


}