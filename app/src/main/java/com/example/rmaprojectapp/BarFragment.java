package com.example.rmaprojectapp;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class BarFragment extends Fragment {


    private EditText editTextEditor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        EditorActivity editorActivity = (EditorActivity) getActivity();

        if (editorActivity != null) {
            editTextEditor = editorActivity.editTextEditor;
        }


        View v = inflater.inflate(R.layout.fragment_bar, container, false);

        initButtons(v);


        return v;
    }

    private void initButtons(View v) {

        Button buttonCreate = v.findViewById(R.id.buttonCreate);
        Button buttonOpen = v.findViewById(R.id.buttonOpen);

        buttonCreate.setOnClickListener(view -> createFileDialog());

        buttonOpen.setOnClickListener(view -> openFileDialog());

    }

    private void createFileDialog() {

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/x-java");
        intent.putExtra(Intent.EXTRA_TITLE, "untitled.java");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        createFileLauncher.launch(intent);

    }


    private void openFileDialog() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/x-java");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        openFileLauncher.launch(intent);

    }

    private final ActivityResultLauncher<Intent> createFileLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if (result.getResultCode() == RESULT_OK ) {

            Uri fileURI = result.getData().getData();

            if (fileURI == null) {

                try {

                    throw new IOException(String.valueOf(Log.d("BarFragment", "fileURI might be null.")));

                } catch (IOException e) {

                    throw new RuntimeException(e);

                }
            } else {

                saveFileToURI(fileURI);

            }

        }

    });


    private final ActivityResultLauncher<Intent> openFileLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if (result.getResultCode() == RESULT_OK) {
            Uri fileURI = result.getData().getData();

            if (fileURI == null) {

                try {

                    throw new IOException(String.valueOf(Log.d("BarFragment", "fileURI might be null.")));

                } catch (IOException e) {

                    throw new RuntimeException(e);

                }
            } else {

                readFileFromURI(fileURI);

            }

        }

    });


    private void saveFileToURI(Uri fileURI) {

        try {

            String data = editTextEditor.getText().toString();
            ContentResolver contentResolver = requireContext().getContentResolver();

            try (OutputStream outputStream = contentResolver.openOutputStream(fileURI)) {

                if (outputStream != null) {

                    outputStream.write(data.getBytes());
                    outputStream.flush();

                }

            } catch (IOException e) {
                Log.e("BarFragment", "Error saving file");
            }

        } catch (Exception e) {
            Log.e("BarFragment", "Unexpected error in saveFileToURI()");
        }


    }


    private void readFileFromURI(Uri fileURI) {

        try {
            // Use ContentResolver to open an InputStream for the URI
            ContentResolver contentResolver = requireContext().getContentResolver();
            try (InputStream inputStream = contentResolver.openInputStream(fileURI)) {
                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    // Set the text content into the EditText
                    editTextEditor.setText(stringBuilder.toString().trim());
                }
            } catch (IOException e) {
                Log.e("BarFragment", "Error reading file!");
            }
        } catch (Exception e) {
            Log.e("BarFragment", "Unexpected error in readFileFromURI()");
        }

    }


}