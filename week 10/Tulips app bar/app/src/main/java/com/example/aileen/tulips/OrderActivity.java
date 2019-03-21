package com.example.aileen.tulips;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //get toolbar and set it as the app bar
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        //enable the up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
