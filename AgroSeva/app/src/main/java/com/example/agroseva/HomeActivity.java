package com.example.agroseva;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.work.Configuration;

import com.example.agroseva.databinding.ActivityHomeBinding;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1001;
    ActivityHomeBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.green));

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Create Notification Channel (required for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        // Set up the toolbar
        setSupportActionBar(binding.toolbar);

        // Load the default fragment
        replaceFragment(new HomeFragment());

        // Set up the bottom navigation view
        binding.bottomnavbar.setBackground(null);
        binding.bottomnavbar.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.weather) {
                replaceFragment(new WeatherFragment());
                Toast.makeText(this, R.string.weather, Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.settings) {
                replaceFragment(new SettingsFragment());
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout, fragment) // Ensure this ID matches your layout
                .commit();
    }

    private void createNotificationChannel() {
        CharSequence name = "Watering Reminder Channel";
        String description = "Channel for watering reminders";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("watering_channel", name, importance);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setDescription(description);
        }

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.rate) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vegetablemarketprice.com/market/tamilnadu/today"));
            startActivity(intent);
        } else if (itemId == R.id.notification) {
            replaceFragment(new NotificationFragment());
            Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareMessage = "Check out this amazing app: https://play.google.com/store/apps/details?id=com.example.agroseva";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_translate) {
            toggleLanguage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleLanguage() {
        String currentLanguage = getResources().getConfiguration().locale.getLanguage();
        String newLanguage = currentLanguage.equals("en") ? "ta" : "en";

        // Update app's locale
        Locale locale = new Locale(newLanguage);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        // Notify user
        String languageChangedMessage = newLanguage.equals("en") ? "Language changed to English" : "மொழி தமிழாக மாற்றப்பட்டது";
        Toast.makeText(this, languageChangedMessage, Toast.LENGTH_SHORT).show();

        // Refresh UI
        recreate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
