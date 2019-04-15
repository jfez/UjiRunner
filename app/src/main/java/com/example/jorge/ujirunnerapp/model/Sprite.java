package com.example.jorge.ujirunnerapp.model;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;
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

    private List<Animation> animationList;
    private Rect rect;
    private boolean isAnimated;

    public Sprite(Bitmap bitmapToRender, boolean hFlip, float x, float y, int speedX, int speedY, int sizeX, int sizeY) {
        this.bitmapToRender = bitmapToRender;
        this.hFlip = hFlip;
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        this.animationList = null;
        this.rect = null;
        this.isAnimated = false;
    }

    public Bitmap getBitmapToRender() {
        return bitmapToRender;
    }

    public boolean ishFlip() {
        return hFlip;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getSpeedX() {
        return speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public Rect getRect() {
        return rect;
    }

    public void setBitmapToRender(Bitmap bitmapToRender) {
        this.bitmapToRender = bitmapToRender;
    }

    public void sethFlip(boolean hFlip) {
        this.hFlip = hFlip;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void Move (float time){

        x = x + speedX*time;
        y = y + speedY*time;

    }

    public void addAnimation (Animation animation) {
        if (animationList == null){
            animationList = new ArrayList<Animation>();
        }
        if (!isAnimated){
            isAnimated = true;
        }
        animationList.add(animation);

    }

    public Animation getAnimation(){
        return animationList.get(0);

    }

    public Animation getAnimation(int id){
        return animationList.get(id);

    }

    public boolean isAnimated() {
        return isAnimated;
    }
}
