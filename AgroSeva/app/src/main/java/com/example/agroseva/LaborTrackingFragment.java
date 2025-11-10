package com.example.agroseva;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;

public class LaborTrackingFragment extends Fragment {

    public LaborTrackingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_labor_tracking, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatePicker datePicker = view.findViewById(R.id.datePicker);
        Spinner taskSpinner = view.findViewById(R.id.taskSpinner);
        EditText numWorkersEditText = view.findViewById(R.id.numWorkersEditText);
        EditText salaryEditText = view.findViewById(R.id.salaryEditText);
        TextView totalSalaryTextView = view.findViewById(R.id.totalSalaryTextView);
        Button calculateButton = view.findViewById(R.id.calculateButton);

        calculateButton.setOnClickListener(v -> {
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();

            if (year < 1) {
                // Invalid date or time
                Toast.makeText(getContext(), "Please select a valid date and time", Toast.LENGTH_SHORT).show();
                return;
            }

            String taskName = taskSpinner.getSelectedItem().toString();
            String numWorkersStr = numWorkersEditText.getText().toString();
            String salaryStr = salaryEditText.getText().toString();

            if (numWorkersStr.isEmpty() || salaryStr.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int numWorkers = Integer.parseInt(numWorkersStr);
                float salaryPerWorker = Float.parseFloat(salaryStr);
                float totalSalary = numWorkers * salaryPerWorker;

                totalSalaryTextView.setText(String.format("₹%.2f", totalSalary));

//                Toast.makeText(getContext(), "Date: " + date + "\nTask: " + taskName +
//                        "\nTotal Salary: ₹" + totalSalary, Toast.LENGTH_LONG).show();
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid number format", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
