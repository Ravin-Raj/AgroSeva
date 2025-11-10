package com.example.agroseva;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agroseva.ml.BestFertilizerModel;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SoilTestingFragment extends Fragment {

    // UI Components
    private EditText nitrogenInput, phosphorusInput, potassiumInput, soilMoisture, Humidity, Temperature;
    private Spinner soilTypeSpinner;
    private Button predictButton;
    private TextView result;

    // Variables to store fetched data
    private float soilmoisture, humidity, temperature;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_soil_testing, container, false);

        // Initialize UI components
        nitrogenInput = root.findViewById(R.id.nitrogenInput);
        phosphorusInput = root.findViewById(R.id.phosphorusInput);
        potassiumInput = root.findViewById(R.id.potassiumInput);
        soilTypeSpinner = root.findViewById(R.id.soilTypeSpinner);
        predictButton = root.findViewById(R.id.checkSuitabilityButton);
        result = root.findViewById(R.id.resultText);

        soilMoisture = root.findViewById(R.id.soilmoistureInput);
        Humidity = root.findViewById(R.id.humidityInput);
        Temperature = root.findViewById(R.id.temperatureInput);

        // Set onClickListener for Predict button
        predictButton.setOnClickListener(v -> validateAndRunModel());

        return root;
    }

    private void validateAndRunModel() {
        if (areInputsValid()) {
            runModel();
        } else {
            Toast.makeText(getContext(),"Please fill all fields with valid numbers.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean areInputsValid() {
        return !TextUtils.isEmpty(nitrogenInput.getText()) &&
                !TextUtils.isEmpty(phosphorusInput.getText()) &&
                !TextUtils.isEmpty(potassiumInput.getText()) &&
                !TextUtils.isEmpty(soilMoisture.getText()) &&
                !TextUtils.isEmpty(Temperature.getText()) &&
                !TextUtils.isEmpty(Humidity.getText());
    }

    private void runModel() {
        try {
            // Get manually entered input values for nitrogen, phosphorus, and potassium
            float nitrogen = Float.parseFloat(nitrogenInput.getText().toString());
            float phosphorus = Float.parseFloat(phosphorusInput.getText().toString());
            float potassium = Float.parseFloat(potassiumInput.getText().toString());
            soilmoisture = Float.parseFloat(soilMoisture.getText().toString().trim());
            temperature = Float.parseFloat(Temperature.getText().toString().trim());
            humidity = Float.parseFloat(Humidity.getText().toString().trim());

            // Map spinner selections to numerical values
            float soilType = soilTypeSpinner.getSelectedItemPosition();

            // Prepare input array for model
            float[] inputValues = new float[22];
            inputValues[0] = soilmoisture;  // Fetched from Firebase
            inputValues[1] = humidity;      // Fetched from Firebase
            inputValues[2] = temperature;   // Fetched from Firebase
            inputValues[3] = nitrogen;      // Manually input
            inputValues[4] = phosphorus;    // Manually input
            inputValues[5] = potassium;     // Manually input
            inputValues[6] = soilType;      // From spinner
            inputValues[7] = 2 ;      // From spinner

            // Fill remaining inputValues with default values if necessary (e.g., 0)
            for (int i = 8; i < inputValues.length; i++) {
                inputValues[i] = 0.0f;
            }

            // Convert input array to ByteBuffer
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(inputValues.length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            for (float value : inputValues) {
                byteBuffer.putFloat(value);
            }

            // Load and run the TensorFlow Lite model
            BestFertilizerModel model = BestFertilizerModel.newInstance(requireContext());

            // Create TensorBuffer input
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, inputValues.length}, org.tensorflow.lite.DataType.FLOAT32);
            inputFeature0.loadBuffer(byteBuffer);

            // Run inference and get output
            BestFertilizerModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            // Extract predictions
            float[] predictions = outputFeature0.getFloatArray();

            // Display the result
            String resultt = "Predicted Fertilizer Levels:\n\n" +
                    "Nitrogen: " + String.format("%.2f", predictions[0]) + " %\n" +
                    "Phosphorus: " + String.format("%.2f", predictions[1]) + " %\n" +
                    "Potassium: " + String.format("%.2f", predictions[2]) + " %\n\n";

            // Suggest the best fertilizer based on predicted NPK values
            String fertilizerSuggestion = suggestFertilizer(predictions[0], predictions[1], predictions[2]);
            resultt += "Suggested Fertilizer: \n" + fertilizerSuggestion;

            // Display results to user
            result.setText(resultt);

            // Close the model
            model.close();

        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid input format. Please enter numeric values.", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Error loading model. Please try again later.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Something went wrong: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String suggestFertilizer(float nitrogen, float phosphorus, float potassium) {
        // NPK values for common fertilizers
        float[] ureaNPK = {46.0f, 0.0f, 0.0f};  // Urea NPK (46-0-0)
        float[] dapNPK = {18.0f, 46.0f, 0.0f};  // DAP NPK (18-46-0)
        float[][] complexFertilizers = { // Array of available complex fertilizers
                {10.0f, 26.0f, 26.0f}, // Complex 10-26-26
                {12.0f, 32.0f, 16.0f}, // Complex 12-32-16
                {20.0f, 20.0f, 0.0f},  // Complex 20-20-0
                {17.0f, 17.0f, 17.0f},  // Complex 17-17-17
                {14.0f, 35.0f, 14.0f},  // Complex 14-35-14
                {28.0f, 28.0f, 0.0f} // Complex 28-28-0
        };

        String[] complexNames = { // Names corresponding to the complex fertilizers
                "Complex 10-26-26",
                "Complex 12-32-16",
                "Complex 20-20-0",
                "Complex 17-17-17",
                "Complex 14-35-14",
                "Complex 28-28-0"
        };

        // Calculate distances
        float ureaDistance = calculateDistance(nitrogen, phosphorus, potassium, ureaNPK);
        float dapDistance = calculateDistance(nitrogen, phosphorus, potassium, dapNPK);

        float closestComplexDistance = Float.MAX_VALUE;
        String closestComplexName = "";

        // Find the closest match among complex fertilizers
        for (int i = 0; i < complexFertilizers.length; i++) {
            float distance = calculateDistance(nitrogen, phosphorus, potassium, complexFertilizers[i]);
            if (distance < closestComplexDistance) {
                closestComplexDistance = distance;
                closestComplexName = complexNames[i];
            }
        }

        // Determine the closest fertilizer overall
        if (ureaDistance <= dapDistance && ureaDistance <= closestComplexDistance) {
            return "Urea (46-0-0)";
        } else if (dapDistance <= ureaDistance && dapDistance <= closestComplexDistance) {
            return "DAP (18-46-0)";
        } else {
            return closestComplexName; // Return the closest complex fertilizer
        }
    }

    private float calculateDistance(float nitrogen, float phosphorus, float potassium, float[] fertilizerNPK) {
        float nitrogenDiff = nitrogen - fertilizerNPK[0];
        float phosphorusDiff = phosphorus - fertilizerNPK[1];
        float potassiumDiff = potassium - fertilizerNPK[2];
        return (float) Math.sqrt(nitrogenDiff * nitrogenDiff + phosphorusDiff * phosphorusDiff + potassiumDiff * potassiumDiff);
    }
}