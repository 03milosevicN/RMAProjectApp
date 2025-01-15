package com.example.rmaprojectapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EditorActivity extends AppCompatActivity {

    public EditText editTextEditor;
    private TextView textViewLineNumbers;

    private ImageButton buttonSaveFile;
    private ImageButton buttonOpenFile;

    private ImageButton buttonCompileCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextEditor = findViewById(R.id.editTextEditor);
        textViewLineNumbers = findViewById(R.id.textViewLineNumbers);

        initTextViewLineNumbers(editTextEditor);
        updateTextViewLineNumbers();

        initButtons();
    }

    private void initButtons() {
        buttonSaveFile = findViewById(R.id.buttonSaveFile);
        buttonSaveFile.setOnClickListener(v -> createFileDialog());

        buttonOpenFile = findViewById(R.id.buttonOpenFile);
        buttonOpenFile.setOnClickListener(v -> openFileDialog());

        buttonCompileCode = findViewById(R.id.buttonCompileCode);
        buttonCompileCode.setOnClickListener(v -> {
            compileCode();
        });
    }

    private void compileCode() {

        String codeToCompile = editTextEditor.getText().toString();

        Intent intent = new Intent(EditorActivity.this, CompiledCodeActivity.class);
        intent.putExtra("code", codeToCompile);
        startActivity(intent);
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

    private final ActivityResultLauncher<Intent> createFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri fileURI = result.getData().getData();

                    if (fileURI == null) {
                        Log.e("EditorActivity", "fileURI might be null.");
                    } else {
                        saveFileToURI(fileURI);
                    }
                }
            });

    private final ActivityResultLauncher<Intent> openFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Uri fileURI = result.getData().getData();

                    if (fileURI == null) {
                        Log.e("EditorActivity", "fileURI might be null.");
                    } else {
                        readFileFromURI(fileURI);
                    }
                }
            });

    private void saveFileToURI(Uri fileURI) {
        try {
            String data = editTextEditor.getText().toString();
            ContentResolver contentResolver = getContentResolver();  // Using Activity's context

            try (OutputStream outputStream = contentResolver.openOutputStream(fileURI)) {
                if (outputStream != null) {
                    outputStream.write(data.getBytes());
                    outputStream.flush();
                }
            } catch (IOException e) {
                Log.e("EditorActivity", "Error saving file", e);
            }

        } catch (Exception e) {
            Log.e("EditorActivity", "Unexpected error in saveFileToURI()", e);
        }
    }

    private void readFileFromURI(Uri fileURI) {
        try {
            ContentResolver contentResolver = getContentResolver();  // Using Activity's context

            try (InputStream inputStream = contentResolver.openInputStream(fileURI)) {
                if (inputStream != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    int character;
                    while ((character = inputStream.read()) != -1) {
                        stringBuilder.append((char) character);
                    }
                    // Set the data into the EditText
                    editTextEditor.setText(stringBuilder.toString());
                }
            } catch (IOException e) {
                Log.e("EditorActivity", "Error reading file", e);
            }

        } catch (Exception e) {
            Log.e("EditorActivity", "Unexpected error in readFileFromURI()", e);
        }
    }

    private void initTextViewLineNumbers(EditText et) {

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                updateTextViewLineNumbers();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateTextViewLineNumbers();
    }

    private void updateTextViewLineNumbers() {

        String text = editTextEditor.getText().toString();

        int totalLines = text.split("\n").length;

        StringBuilder lineNumberText = new StringBuilder();
        for (int i = 1; i <= totalLines; i++) {
            lineNumberText.append(i).append("\n");
        }

        textViewLineNumbers.setText(lineNumberText.toString());
    }


}
