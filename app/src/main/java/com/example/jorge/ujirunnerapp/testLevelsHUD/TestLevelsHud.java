package com.example.jorge.ujirunnerapp.testLevelsHUD;

import android.util.DisplayMetrics;

import com.example.jorge.ujirunnerapp.framework.GameActivity;
import com.example.jorge.ujirunnerapp.framework.IGameController;

public class TestLevelsHud extends GameActivity {
    @Override
    protected IGameController buildGameController() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        TestLevelsHudController testLevelsHudController = new TestLevelsHudController(this, displayMetrics.widthPixels, displayMetrics.heightPixels);

        return testLevelsHudController;

    }
}
