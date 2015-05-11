package cse403.homesafe.Settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import cse403.homesafe.R;

/**
 * Created by Alex on 5/10/15.
 * Acts as the single PreferencesFragment for the HomeSafe app.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
