package com.example.jorge.ujirunnerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jorge.ujirunnerapp.testFramework.TestFramework;

public class MainActivity extends AppCompatActivity {

    public Button framework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        framework = findViewById(R.id.frameworkBut);

        framework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testFramework(v);
            }
        });




    }

    public void testFramework(View view) {
        Intent intent = new Intent(this, TestFramework.class);
        startActivity(intent);
    }




}
