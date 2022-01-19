package com.gpf.learnapt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gpf.annotation.GPFBindLayout;
import com.gpf.sdk.GPFApi;

@GPFBindLayout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GPFApi.bindLayout(this);
        setContentView(R.layout.activity_main);
    }
}