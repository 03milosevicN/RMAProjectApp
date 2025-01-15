package com.example.rmaprojectapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivityBarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main_activity_bar, container, false);

        initButtons(v);

        return v;
    }

    private void initButtons(View v) {

        Button buttonGoToEditorActivity = v.findViewById(R.id.buttonGoToEditorActivity);

        buttonGoToEditorActivity.setOnClickListener(view -> {

            Intent intent = new Intent(getActivity(), EditorActivity.class);
            startActivity(intent);

        });

    }


}