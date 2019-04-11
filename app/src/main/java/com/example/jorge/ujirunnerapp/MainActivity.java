package com.example.jorge.ujirunnerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jorge.ujirunnerapp.testCharacter.TestCharacter;
import com.example.jorge.ujirunnerapp.testFramework.TestFramework;
import com.example.jorge.ujirunnerapp.testParallax.TestParallax;

public class MainActivity extends AppCompatActivity {

    public Button framework;
    public Button parallax;
    public Button runner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        framework = findViewById(R.id.frameworkBut);
        parallax = findViewById(R.id.parallaxBut);
        runner = findViewById(R.id.characterBut);


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

        runner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testCharacter(v);
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

    public void testCharacter(View view){
        Intent intent = new Intent(this, TestCharacter.class);
        startActivity(intent);
    }




}
