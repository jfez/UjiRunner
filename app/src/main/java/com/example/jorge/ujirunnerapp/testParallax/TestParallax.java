package com.example.jorge.ujirunnerapp.testParallax;

import android.util.DisplayMetrics;

import com.example.jorge.ujirunnerapp.framework.GameActivity;
import com.example.jorge.ujirunnerapp.framework.IGameController;

public class TestParallax extends GameActivity {
    @Override
    protected IGameController buildGameController() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        TestParallaxController testParallaxController = new TestParallaxController(this, displayMetrics.widthPixels, displayMetrics.heightPixels);

        return testParallaxController;

    }
}
