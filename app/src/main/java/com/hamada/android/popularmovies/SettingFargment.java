package com.hamada.android.popularmovies;

import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingFargment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_movies);
        //to set summery for list preference
        ListPreference listPreference= (ListPreference)
                findPreference(getString(R.string.pref_sort_order_key));
        if (listPreference.getValue()==null){
            listPreference.setValueIndex(0);
        }

        listPreference.setSummary(listPreference.getValue().toString());
        //set listener when change list summery will be changed
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString());
                return true;
            }
        });
    }
}
