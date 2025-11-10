package com.example.agroseva;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OnionFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onion, container, false);

        View instruction = view.findViewById(R.id.instruction);
        View soil = view.findViewById(R.id.soil_test_linear);
        View water = view.findViewById(R.id.water_management_linear);
        View p_l = view.findViewById(R.id.profit_loss_linear);
        View labtracking = view.findViewById(R.id.labor_tracking_linear);


        instruction.setOnClickListener(v -> openFragment(new OnionInstructionFragment()));
        soil.setOnClickListener(v -> openFragment(new SoilTestingFragment()));
        water.setOnClickListener(v -> openFragment(new WateringManagementFragment()));
        labtracking.setOnClickListener(v -> openFragment(new LaborTrackingFragment()));
        p_l.setOnClickListener(v -> openFragment(new ProfitLossFragment()));


        return view;
    }

    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }
}