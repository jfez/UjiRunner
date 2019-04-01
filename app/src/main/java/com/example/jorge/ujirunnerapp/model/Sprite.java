package com.example.jorge.ujirunnerapp.model;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.List;

public class Sprite {
    private Bitmap bitmapToRender;
    private boolean hFlip;
    private float x;
    private float y;
    private int speedX;
    private int speedY;
    private int sizeX;
    private int sizeY;

    private List<Animation> animationLists;
    private Rect rect;
    private boolean isAnimated;

}
