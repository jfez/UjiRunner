package com.example.jorge.ujirunnerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jorge.ujirunnerapp.testCharacter.TestCharacter;
import com.example.jorge.ujirunnerapp.testFramework.TestFramework;
import com.example.jorge.ujirunnerapp.testLevelsHUD.TestLevelsHud;
import com.example.jorge.ujirunnerapp.testObstacles.TestObstacles;
import com.example.jorge.ujirunnerapp.testParallax.TestParallax;
import com.example.jorge.ujirunnerapp.ujiRunner.UjiRunner;

public class MainActivity extends AppCompatActivity {

    public Button framework;
    public Button parallax;
    public Button runner;
    public Button obstacles;
    public Button levelsHud;
    public Button game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Poner el icono en el action Bar

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        framework = findViewById(R.id.frameworkBut);
        parallax = findViewById(R.id.parallaxBut);
        runner = findViewById(R.id.characterBut);
        obstacles = findViewById(R.id.obstaclesBut);
        levelsHud = findViewById(R.id.levelsBut);
        game = findViewById(R.id.gameBut);


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

        obstacles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testObstacles();
            }
        });

        levelsHud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testLevelsHud();

            }
        });

        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testGame();
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

    private void testObstacles() {
        Intent intent = new Intent(this, TestObstacles.class);
        startActivity(intent);
    }

    private void testLevelsHud() {
        Intent intent = new Intent(this, TestLevelsHud.class);
        startActivity(intent);
    }

    private void testGame() {
        Intent intent = new Intent(this, UjiRunner.class);
        startActivity(intent);
    }




}
