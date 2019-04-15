package com.example.jorge.ujirunnerapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Assets {

    public static final int CHARACTER_RUN_FRAME_WIDTH = 120;
    public static final int CHARACTER_RUN_FRAME_HEIGHT = 150;
    public static final int CHARACTER_JUMP_FRAME_WIDTH = 72;
    public static final int CHARACTER_JUMP_FRAME_HEIGHT = 150;
    public static final int CHARACTER_CROUCH_FRAME_WIDTH = 70;
    public static final int CHARACTER_CROUCH_FRAME_HEIGHT = 105;
    public static final int CHARACTER_RUN_NUMBER_OF_FRAMES = 8;
    public static final int CHARACTER_CROUCH_NUMBER_OF_FRAMES = 8;
    public static final int CHARACTER_JUMP_NUMBER_OF_FRAMES = 4;

    public static Bitmap[] bgLayers;

    public static Bitmap characterRunning;
    public static Bitmap characterCrouching;
    public static Bitmap characterJumping;

    public static int playerHeight;
    public static int runnerJumpsWidth;
    public static int runnerJumpsHeight;
    public static int runnerCrouchesWidth;
    public static int runnerCrouchesHeight;



    public static void createAssets(Context context, int playerWidth, int stageHeight,
                                    int parallaxWidth) {
        Resources resources = context.getResources();
        if (bgLayers != null){
            for (Bitmap bitmap: bgLayers){
                bitmap.recycle();
            }
        }

        if (characterCrouching != null){
            characterCrouching.recycle();
        }

        if (characterJumping != null){
            characterJumping.recycle();
        }

        if (characterRunning != null){
            characterRunning.recycle();
        }

        int[] bgLayersResources = {
                R.drawable.ground,
                R.drawable.foreground,
                R.drawable.decor_middle,
                R.drawable.decor_bg,
                R.drawable.sky
        };



        bgLayers = new Bitmap[bgLayersResources.length];
        for (int i = 0; i < bgLayers.length; i++) {
            bgLayers[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    resources, bgLayersResources[i]), parallaxWidth, stageHeight, true);
        }

        playerHeight = (CHARACTER_RUN_FRAME_HEIGHT * playerWidth) /
                CHARACTER_RUN_FRAME_WIDTH;
        characterRunning = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.runsp), playerWidth *
                CHARACTER_RUN_NUMBER_OF_FRAMES, playerHeight, true);


        runnerJumpsWidth = (playerWidth * CHARACTER_JUMP_FRAME_WIDTH) /
                CHARACTER_RUN_FRAME_WIDTH;
        runnerJumpsHeight = (CHARACTER_JUMP_FRAME_HEIGHT * runnerJumpsWidth) /
                CHARACTER_JUMP_FRAME_WIDTH;
        characterJumping = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.jumpsp), runnerJumpsWidth *
                CHARACTER_JUMP_NUMBER_OF_FRAMES, runnerJumpsHeight, true);


        runnerCrouchesWidth = (playerWidth * CHARACTER_CROUCH_FRAME_WIDTH) /
                CHARACTER_RUN_FRAME_WIDTH;
        runnerCrouchesHeight = (CHARACTER_CROUCH_FRAME_HEIGHT*runnerCrouchesWidth)
                / CHARACTER_CROUCH_FRAME_WIDTH;
        characterCrouching = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.crouchsp), runnerCrouchesWidth *
                CHARACTER_CROUCH_NUMBER_OF_FRAMES, runnerCrouchesHeight, true);


    }

}
