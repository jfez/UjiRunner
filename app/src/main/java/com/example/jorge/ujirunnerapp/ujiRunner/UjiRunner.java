package com.example.jorge.ujirunnerapp.ujiRunner;

import android.media.AudioManager;
import android.util.DisplayMetrics;

import com.example.jorge.ujirunnerapp.framework.GameActivity;
import com.example.jorge.ujirunnerapp.framework.IGameController;

public class UjiRunner extends GameActivity {
    @Override
    protected IGameController buildGameController() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        UjiRunnerController ujiRunnerController = new UjiRunnerController(this, displayMetrics.widthPixels, displayMetrics.heightPixels);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        return ujiRunnerController;

    }
}
