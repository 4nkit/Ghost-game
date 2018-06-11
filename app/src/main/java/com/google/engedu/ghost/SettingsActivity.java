package com.google.engedu.ghost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;

public class SettingsActivity extends AppCompatPreferenceActivity {

    Intent intent;
    int count=0;
    ListPreference lp;
    SharedPreferences sp;
    private PreferenceChangeListener preferenceChangeListener=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_screen);
        lp = (ListPreference)findPreference("score");
        lp.setDefaultValue("Without score");
        lp.setValueIndex(0);
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        preferenceChangeListener=new PreferenceChangeListener();
        sp.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        ApplySettings();

    }

    private class PreferenceChangeListener implements
            SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs,
                                              String key) {
            //  Toast.makeText(SettingsActivity.this, "hello", Toast.LENGTH_SHORT).show();
            ApplySettings();
        }
    }

    public void ApplySettings() {
        count++;
        String words = sp.getString("score", "Without score");
        //Toast.makeText(this, Integer.toString(count), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, dice, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, player, Toast.LENGTH_SHORT).show();

        if (words.equals("Without score")) {
            intent = new Intent(this, Ghost1Activity.class);
            lp.setSummary("Without score");

        } else {
            intent = new Intent(this, Ghost2Activity.class);
            lp.setSummary("With score");
        }


    }

    @Override
    public void onBackPressed() {
        startActivity(intent);
    }
}

