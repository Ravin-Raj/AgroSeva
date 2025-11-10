package com.example.agroseva;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfitLossFragment extends Fragment {
    private EditText landCost, depreciation, taxes, seeds, fertilizers, labor, water, fuel, machineryMaintenance, transportation, storage, revenueCropSales, revenueLivestock, revenueByProducts, revenueValueAdded;
    private TextView resultText;
    private Button calculateButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profit_loss, container, false);

        // Initialize UI components
        landCost = view.findViewById(R.id.edit_land_cost);
        depreciation = view.findViewById(R.id.edit_depreciation);
        taxes = view.findViewById(R.id.edit_taxes);
        seeds = view.findViewById(R.id.edit_seeds);
        fertilizers = view.findViewById(R.id.edit_fertilizers);
        labor = view.findViewById(R.id.edit_labor);
        water = view.findViewById(R.id.edit_water);
        fuel = view.findViewById(R.id.edit_fuel);
        machineryMaintenance = view.findViewById(R.id.edit_machinery_maintenance);
        transportation = view.findViewById(R.id.edit_transportation);
        storage = view.findViewById(R.id.edit_storage);
        revenueCropSales = view.findViewById(R.id.edit_revenue_crop_sales);
        revenueLivestock = view.findViewById(R.id.edit_revenue_livestock);
        revenueByProducts = view.findViewById(R.id.edit_revenue_by_products);
        revenueValueAdded = view.findViewById(R.id.edit_revenue_value_added);
        resultText = view.findViewById(R.id.text_result);
        calculateButton = view.findViewById(R.id.button_calculate);

        // Set calculate button listener
        calculateButton.setOnClickListener(v -> calculateProfitLoss());

        return view;
    }

    private void calculateProfitLoss() {
        try {
            // Get inputs for expenditures
            double land = Double.parseDouble(landCost.getText().toString());
            double dep = Double.parseDouble(depreciation.getText().toString());
            double tax = Double.parseDouble(taxes.getText().toString());
            double seed = Double.parseDouble(seeds.getText().toString());
            double fert = Double.parseDouble(fertilizers.getText().toString());
            double lab = Double.parseDouble(labor.getText().toString());
            double wat = Double.parseDouble(water.getText().toString());
            double fu = Double.parseDouble(fuel.getText().toString());
            double machinery = Double.parseDouble(machineryMaintenance.getText().toString());
            double trans = Double.parseDouble(transportation.getText().toString());
            double stor = Double.parseDouble(storage.getText().toString());

            // Get inputs for income
            double cropSales = Double.parseDouble(revenueCropSales.getText().toString());
            double livestock = Double.parseDouble(revenueLivestock.getText().toString());
            double byProducts = Double.parseDouble(revenueByProducts.getText().toString());
            double valueAdded = Double.parseDouble(revenueValueAdded.getText().toString());

            // Calculate total expenditure and revenue
            double totalExpenditure = land + dep + tax + seed + fert + lab + wat + fu + machinery + trans + stor;
            double totalRevenue = cropSales + livestock + byProducts + valueAdded;

            // Calculate profit or loss
            double profitLoss = totalRevenue - totalExpenditure;

            // Display result
            if (profitLoss > 0) {
                resultText.setText("Profit: ₹" + profitLoss);
            } else if (profitLoss < 0) {
                resultText.setText("Loss: ₹" + (-profitLoss));
            } else {
                resultText.setText("Break-even: No Profit or Loss");
            }
        } catch (NumberFormatException e) {
            resultText.setText("Please enter valid inputs.");
        }
    }
}
