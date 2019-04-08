package com.example.jorge.ujirunnerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jorge.ujirunnerapp.testFramework.TestFramework;
import com.example.jorge.ujirunnerapp.testParallax.TestParallax;

public class MainActivity extends AppCompatActivity {

    public Button framework;
    public Button parallax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        framework = findViewById(R.id.frameworkBut);
        parallax = findViewById(R.id.parallaxBut);


        framework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testFramework(v);
            }
        });
        parallax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testParallax(v);
            }
        });




    }

    public void testFramework(View view) {
        Intent intent = new Intent(this, TestFramework.class);
        startActivity(intent);
    }

    public void testParallax(View view){
        Intent intent = new Intent(this, TestParallax.class);
        startActivity(intent);
    }




}
