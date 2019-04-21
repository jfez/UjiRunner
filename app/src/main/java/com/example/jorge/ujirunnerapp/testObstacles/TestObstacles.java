package com.example.jorge.ujirunnerapp.testObstacles;

import android.util.DisplayMetrics;

import com.example.jorge.ujirunnerapp.framework.GameActivity;
import com.example.jorge.ujirunnerapp.framework.IGameController;

public class TestObstacles extends GameActivity {
    @Override
    protected IGameController buildGameController() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        TestObstaclesController testObstaclesController = new TestObstaclesController(this, displayMetrics.widthPixels, displayMetrics.heightPixels);

        return testObstaclesController;

    }
}
