package com.example.rmaprojectapp;


import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity {

    private boolean isBarFragmentVisible = false;
    private ImageButton imageButtonHamburgerMenu;
    private FrameLayout frameLayoutBarFragmentContainer;
    private TextView textViewSplashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initBarFragment();

    }

    private void initBarFragment() {

        frameLayoutBarFragmentContainer = findViewById(R.id.frameLayoutBarFragmentContainer);

        textViewSplashText = findViewById(R.id.textViewSplashText);

        int orientation = getResources().getConfiguration().orientation;

        imageButtonHamburgerMenu = findViewById(R.id.imageButtonHamburgerMenu);
        imageButtonHamburgerMenu.setOnClickListener(v -> {

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {

                if (!isBarFragmentVisible) {

                    MainActivityBarFragment barFragment = new MainActivityBarFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayoutBarFragmentContainer, barFragment)
                            .commit();

                    textViewSplashText.setVisibility(View.GONE);
                    frameLayoutBarFragmentContainer.setVisibility(View.VISIBLE);
                    isBarFragmentVisible = true;

                } else {

                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(getSupportFragmentManager().findFragmentById(R.id.frameLayoutBarFragmentContainer))
                            .commit();

                    textViewSplashText.setVisibility(View.VISIBLE);
                    frameLayoutBarFragmentContainer.setVisibility(View.GONE);
                    isBarFragmentVisible = false;

                }

            } else {

                if (!isBarFragmentVisible) {

                    MainActivityBarFragment barFragment = new MainActivityBarFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayoutBarFragmentContainer, barFragment)
                            .commit();

                    frameLayoutBarFragmentContainer.setVisibility(View.VISIBLE);
                    isBarFragmentVisible = true;

                } else {

                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(getSupportFragmentManager().findFragmentById(R.id.frameLayoutBarFragmentContainer))
                            .commit();

                    frameLayoutBarFragmentContainer.setVisibility(View.GONE);
                    isBarFragmentVisible = false;

                }

            }


        });
    }

}