package com.example.jorge.ujirunnerapp.testFramework;

import android.util.DisplayMetrics;

import com.example.jorge.ujirunnerapp.framework.GameActivity;
import com.example.jorge.ujirunnerapp.framework.IGameController;

public class TestFramework extends GameActivity {
    @Override
    protected IGameController buildGameController() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        TestFrameworkController testFrameworkController = new TestFrameworkController(this, 200, displayMetrics.widthPixels, displayMetrics.heightPixels);

        return testFrameworkController;

    }
}
