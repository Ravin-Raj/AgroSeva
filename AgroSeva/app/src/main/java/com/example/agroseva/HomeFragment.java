package com.example.agroseva;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize all LinearLayouts and set click listeners
        setupFragmentNavigation(view.findViewById(R.id.vegetables_linear));
        setupFragmentNavigation(view.findViewById(R.id.fruits_linear));


        return view;
    }

    private void setupFragmentNavigation(LinearLayout linearLayout) {
        linearLayout.setOnClickListener(v -> {
            // Get the TextView inside the LinearLayout
            TextView textView = (TextView) linearLayout.getChildAt(1); // Assuming TextView is the second child
            String fragmentName = textView.getText().toString();

            // Open the corresponding fragment
            openFragment(fragmentName);
        });
    }

    private void openFragment(String fragmentName) {
        Fragment fragment;

        // Match fragment name to corresponding Fragment class
        if (fragmentName.equals("Vegetables")) {
            fragment = new VegetablesFragment();
        } else if (fragmentName.equals("Fruits")) {
            fragment = new FruitsFragment();
        } else {
            throw new IllegalArgumentException("Unknown fragment name: " + fragmentName);
        }

        // Perform the fragment transaction
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout, fragment); // Replace with your container ID
        transaction.addToBackStack(null); // Optional: add to backstack
        transaction.commit();
    }
}
