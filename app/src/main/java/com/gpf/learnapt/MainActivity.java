package com.gpf.learnapt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gpf.annotation.GPFBindLayout;

@GPFBindLayout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}