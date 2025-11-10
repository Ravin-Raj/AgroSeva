package com.example.agroseva;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SettingsAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] options;

    public SettingsAdapter(Context context, String[] options) {
        super(context, 0, options);
        this.context = context;
        this.options = options;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView optionTextView = convertView.findViewById(android.R.id.text1);
        optionTextView.setText(options[position]);

        convertView.setOnClickListener(v -> {
            String option = options[position];
            handleItemClick(option);
        });

        return convertView;
    }

    private void handleItemClick(String option) {
        Intent intent;
        switch (option) {
            case "FAQ":
                // Open FAQ (replace URL with actual link)
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com/faq"));
                context.startActivity(intent);
                break;

            case "Privacy Policy":
                // Open Privacy Policy (replace URL with actual link)
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com/privacy-policy"));
                context.startActivity(intent);
                break;

            case "Terms and Conditions":
                // Open Terms and Conditions (replace URL with actual link)
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com/terms"));
                context.startActivity(intent);
                break;

            case "Return Policy":
                // Open Return Policy (replace URL with actual link)
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com/return-policy"));
                context.startActivity(intent);
                break;

            case "Rate Us":
                // Open Google Play Store to Rate the app
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
                context.startActivity(intent);
                break;

            case "Logout":
                // Handle logout action
                logout();
                break;

            default:
                break;
        }
    }

    private void logout() {
        // Clear user session or authentication tokens here
        // For example, clear shared preferences or session data
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all stored data
        editor.apply();

        // Redirect user to the Login Activity
        Intent intent = new Intent(context, LoginActivity.class); // Replace with your LoginActivity class
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
        context.startActivity(intent);
    }
}