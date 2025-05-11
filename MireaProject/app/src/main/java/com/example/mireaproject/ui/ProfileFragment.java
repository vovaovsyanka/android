package com.example.mireaproject.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mireaproject.R;

public class ProfileFragment extends Fragment {
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sharedPref = requireActivity().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE);

        EditText nameEditText = view.findViewById(R.id.nameEditText);
        EditText ageEditText = view.findViewById(R.id.ageEditText);
        EditText dishEditText = view.findViewById(R.id.dishEditText);
        Button saveButton = view.findViewById(R.id.saveButton);

        nameEditText.setText(sharedPref.getString("NAME", ""));
        ageEditText.setText(String.valueOf(sharedPref.getInt("AGE", 0)));
        dishEditText.setText(sharedPref.getString("FAVOURITE_DISH", ""));

        saveButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("NAME", nameEditText.getText().toString());
            editor.putInt("AGE", Integer.parseInt(ageEditText.getText().toString()));
            editor.putString("FAVOURITE_DISH", dishEditText.getText().toString());
            editor.apply();
            Toast.makeText(getContext(), "Профиль сохранен", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}