package com.example.agroseva;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private ListView settingsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for the Settings fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsListView = view.findViewById(R.id.settings_list);

        // Define the options to display in the ListView
        String[] settingsOptions = {
                "FAQ",
                "Privacy Policy",
                "Terms and Conditions",
                "Return Policy",
                "Rate Us",
                "Logout"  // Add Logout option here
        };

        // Create an adapter and set it to the ListView
        SettingsAdapter adapter = new SettingsAdapter(getContext(), settingsOptions);
        settingsListView.setAdapter(adapter);

        return view;
    }
}
