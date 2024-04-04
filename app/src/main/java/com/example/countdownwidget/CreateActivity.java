package com.example.countdownwidget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.countdownwidget.ui.create.CreateFragment;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CreateFragment.newInstance())
                    .commitNow();
        }
    }
}