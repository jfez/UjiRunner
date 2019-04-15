package com.example.jorge.ujirunnerapp.testCharacter;

import android.util.DisplayMetrics;

import com.example.jorge.ujirunnerapp.framework.GameActivity;
import com.example.jorge.ujirunnerapp.framework.IGameController;

public class TestCharacter extends GameActivity {
    @Override
    protected IGameController buildGameController() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        TestCharacterController testCharacterController = new TestCharacterController(this, displayMetrics.widthPixels, displayMetrics.heightPixels);

        return testCharacterController;

    }
}
