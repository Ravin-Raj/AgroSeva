package com.example.agroseva;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WateringManagementFragment extends Fragment {

    private Spinner categorySpinner;
    private Spinner cropSpinner;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button setAlarmButton;

    private Map<String, List<String>> categoryCropsMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watering_management, container, false);

        // Initialize UI elements
        categorySpinner = view.findViewById(R.id.categorySpinner);
        cropSpinner = view.findViewById(R.id.cropSpinner);
        datePicker = view.findViewById(R.id.datePicker);
        timePicker = view.findViewById(R.id.timePicker);
        setAlarmButton = view.findViewById(R.id.setAlarmButton);

        // Initialize category and crop data
        initializeCategoryCropsMap();

        // Set up the category spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>(categoryCropsMap.keySet())
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Handle category selection
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                updateCropSpinner(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected
                cropSpinner.setAdapter(null);
            }
        });

        // Set the OnClickListener for the Set Reminder button
        setAlarmButton.setOnClickListener(v -> setReminder());

        return view;
    }

    private void initializeCategoryCropsMap() {
        categoryCropsMap = new HashMap<>();
        categoryCropsMap.put("Vegetables", List.of("Tomato", "Onion", "Carrot"));
        categoryCropsMap.put("Fruits", List.of("Mango", "Apple", "Banana"));
    }

    private void updateCropSpinner(String category) {
        List<String> crops = categoryCropsMap.getOrDefault(category, new ArrayList<>());

        ArrayAdapter<String> cropAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                crops
        );
        cropAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cropSpinner.setAdapter(cropAdapter);
    }

    private void setReminder() {
        // Get the selected crop from the Spinner
        String selectedCrop = cropSpinner.getSelectedItem() != null
                ? cropSpinner.getSelectedItem().toString()
                : null;

        if (selectedCrop == null || selectedCrop.isEmpty()) {
            // Show a warning toast if no crop is selected
            Toast.makeText(getContext(), "Please select a crop.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the date and time selected by the user
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // Create a calendar object to set the reminder time
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, 0);

        // Check if the selected time is in the past
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            // Show a warning toast if the selected time is in the past
            Toast.makeText(getContext(), "Please select a future date and time.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set up an AlarmManager to trigger the reminder
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), WateringNotificationReceiver.class);
        intent.putExtra("selected_crop", selectedCrop); // Pass the selected crop to the receiver
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the alarm to go off at the specified time
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        // Show a success toast confirming the reminder has been set
        Toast.makeText(getContext(), "Watering reminder set successfully for " + selectedCrop, Toast.LENGTH_SHORT).show();
    }
}
