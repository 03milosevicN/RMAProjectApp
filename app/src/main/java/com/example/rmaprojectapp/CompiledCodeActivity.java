package com.example.rmaprojectapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.codehaus.janino.*;

public class CompiledCodeActivity extends AppCompatActivity {

    private TextView textViewCompiledCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_compiled_code);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initComponents();

        String code = getIntent().getStringExtra("code");
        if (code != null) {
            compileAndDisplay(code);
        }

    }

    private void compileAndDisplay(String code) {

        try {
            String compiledOutput = compileJavaCode(code);
            textViewCompiledCode.setText(compiledOutput);
        } catch (Exception e) {
            Log.e("CompiledCodeActivity", "Compilation error.");
            textViewCompiledCode.setText("Compilation error: " + e.getMessage());
        }

    }

    private String compileJavaCode(String code) throws Exception {
        org.codehaus.janino.SimpleCompiler compiler = new org.codehaus.janino.SimpleCompiler();
        compiler.cook(code);
        return "Hello, World!";
    }

    private void initComponents() {

        textViewCompiledCode = findViewById(R.id.textViewCompiledCode);


    }


}