package com.example.agroseva;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NotificationFragment extends Fragment {

    private TextView notificationText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notificationText = view.findViewById(R.id.notificationText);
        return view;
    }

    public void updateNotification(String message) {
        if (notificationText != null) {
            notificationText.setText(message);
        }
    }
}
