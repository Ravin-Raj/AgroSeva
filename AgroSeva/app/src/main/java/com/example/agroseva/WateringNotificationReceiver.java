package com.example.agroseva;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class WateringNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Extract the crop name from the Intent
        String crop = intent.getStringExtra("selected_crop");

        // Intent to open the app when notification is clicked
        Intent notificationIntent = new Intent(context, HomeActivity.class); // Replace with your main activity
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Create notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "watering_channel";

        // Create NotificationChannel (for API 26 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Watering Reminder",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Use a default icon
                .setContentTitle("Watering Reminder")
                .setContentText("Time to water your " + crop)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Show the notification
        notificationManager.notify(0, notificationBuilder.build());
    }
}
