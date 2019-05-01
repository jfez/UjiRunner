package com.example.jorge.ujirunnerapp.model;

import android.graphics.Bitmap;

public class TimedSprite extends Sprite {

    private float timer;

    public TimedSprite(Bitmap bitmapToRender, boolean hFlip, float x, float y, int speedX, int speedY, int sizeX, int sizeY, float timer) {
        super(bitmapToRender, hFlip, x, y, speedX, speedY, sizeX, sizeY);

        this.timer = timer;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }

    public void updateTimer(float time) {
        timer = timer - time;

    }

    public boolean timedOut (){

        return timer <= 0;
    }
}
