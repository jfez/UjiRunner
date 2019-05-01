package com.example.jorge.ujirunnerapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Assets {

    public static final int CHARACTER_RUN_FRAME_WIDTH = 100;
    public static final int CHARACTER_RUN_FRAME_HEIGHT = 125;
    public static final int CHARACTER_JUMP_FRAME_WIDTH = 100;    //72
    public static final int CHARACTER_JUMP_FRAME_HEIGHT = 125;
    public static final int CHARACTER_CROUCH_FRAME_WIDTH = 100;
    public static final int CHARACTER_CROUCH_FRAME_HEIGHT = 100;    //105
    public static final int CHARACTER_RUN_NUMBER_OF_FRAMES = 8;
    public static final int CHARACTER_CROUCH_NUMBER_OF_FRAMES = 8;
    public static final int CHARACTER_JUMP_NUMBER_OF_FRAMES = 4;

    public static final int BOB_NUMBER_OF_FRAMES = 9;
    public static final int CHOMP_NUMBER_OF_FRAMES = 2;
    public static final int GOOMBA_NUMBER_OF_FRAMES = 10;
    public static final int PLANTA_NUMBER_OF_FRAMES = 2;
    public static final int GOOMBA_VOLADOR_NUMBER_OF_FRAMES = 2;
    public static final int LAKITU_NUMBER_OF_FRAMES = 4;
    public static final int DEMISE_GROUND_NUMBER_OF_FRAMES = 6;
    public static final int DEMISE_FLYING_NUMBER_OF_FRAMES = 5;

    public static final int BOO_NUMBER_OF_FRAMES = 8;
    public static final int BEATLE_NUMBER_OF_FRAMES = 8;

    public static final int COINS_NUMBER_OF_FRAMES = 6;

    public static final int BOB_FRAME_HEIGHT = 30;
    public static final int BOB_FRAME_WIDTH = 28;
    public static final int CHOMP_FRAME_HEIGHT = 162;
    public static final int CHOMP_FRAME_WIDTH = 163;
    public static final int GOOMBA_FRAME_HEIGHT = 17;
    public static final int GOOMBA_FRAME_WIDTH = 19;
    public static final int GOOMBA_VOLADOR_FRAME_HEIGHT = 24;
    public static final int GOOMBA_VOLADOR_FRAME_WIDTH = 27;
    public static final int LAKITU_FRAME_HEIGHT = 93;
    public static final int LAKITU_FRAME_WIDTH = 46;
    public static final int PLANTA_FRAME_HEIGHT = 37;
    public static final int PLANTA_FRAME_WIDTH = 36;
    public static final int DEMISE_GROUND_FRAME_HEIGHT = 100;
    public static final int DEMISE_GROUND_FRAME_WIDTH = 143;
    public static final int DEMISE_FLYING_FRAME_HEIGHT = 86;
    public static final int DEMISE_FLYING_FRAME_WIDTH = 85;
    public static final int COIN_FRAME_HEIGHT = 512;
    public static final int COIN_FRAME_WIDTH = 512;
    public static final int COINS_FRAME_HEIGHT = 157;
    public static final int COINS_FRAME_WIDTH = 112;
    public static final int HEART_FRAME_HEIGHT = 41;
    public static final int HEART_FRAME_WIDTH = 39;
    public static final int BOO_FRAME_HEIGHT = 34;
    public static final int BOO_FRAME_WIDTH = 30;
    public static final int BEATLE_FRAME_HEIGHT = 44;
    public static final int BEATLE_FRAME_WIDTH = 46;
    public static final int LIFECONTAINERWIDTH = 120;



    public static final float HEIGHT_RATIO_GROUND_OBSTACLE = 0.6f;
    public static final float HEIGHT_RATIO_FLYING_OBSTACLE = 0.85f;
    public static final float HEIGHT_RATIO_HUD = 0.2f;
    public static final float HEIGHT_RATIO_COINS = 0.4f;


    public static Bitmap[] bgLayers;

    public static Bitmap characterRunning;
    public static Bitmap characterCrouching;
    public static Bitmap characterJumping;

    public static Bitmap bobObstacle;
    public static Bitmap chompObstacle;
    public static Bitmap goombaObstacle;
    public static Bitmap goombaVoladorObstacle;
    public static Bitmap lakituObstacle;
    public static Bitmap plantaObstacle;
    public static Bitmap booObstacle;
    public static Bitmap beatleObstacle;
    public static Bitmap demiseGroundObstacle;
    public static Bitmap demiseFlyingObstacle;

    public static Bitmap coin;
    public static Bitmap coins;
    public static Bitmap heart;
    public static Bitmap lifeContainer;

    public static int playerHeight;
    public static int runnerJumpsWidth;
    public static int runnerJumpsHeight;
    public static int runnerCrouchesWidth;
    public static int runnerCrouchesHeight;

    public static int heightForGroundObstacles;
    public static int heightForFlyingObstacles;
    //no hace falta altura de los demises porque son el mismo que el de los obstáculos??

    public static int bobObstacleWidth;
    public static int chompObstacleWidth;
    public static int goombaObstacleWidth;
    public static int goombaVoladorObstacleWidth;
    public static int lakituObstacleWidth;
    public static int plantaObstacleWidth;
    public static int booObstacleWidth;
    public static int beatleObstacleWidth;
    public static int demiseGroundObstacleWidth;
    public static int demiseFlyingObstacleWidth;

    public static int hudHeight;
    public static int coinsHeight;
    public static int coinHudWidth;
    public static int coinsWidth;
    public static int heartHudWidth;
    public static int lifecontainerHudWidth;



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

        if (bobObstacle != null){
            bobObstacle.recycle();
        }

        if (chompObstacle != null){
            chompObstacle.recycle();
        }

        if (goombaObstacle != null){
            goombaObstacle.recycle();
        }

        if (plantaObstacle != null){
            plantaObstacle.recycle();
        }

        if (goombaVoladorObstacle != null){
            goombaVoladorObstacle.recycle();
        }

        if (lakituObstacle != null){
            lakituObstacle.recycle();
        }

        if (demiseGroundObstacle != null){
            demiseGroundObstacle.recycle();
        }

        if (demiseFlyingObstacle != null){
            demiseFlyingObstacle.recycle();
        }

        if (coin != null){
            coin.recycle();
        }

        if (coins != null){
            coins.recycle();
        }

        if (heart != null){
            heart.recycle();
        }

        if (lifeContainer != null){
            lifeContainer.recycle();
        }

        if (booObstacle != null){
            booObstacle.recycle();
        }

        if (beatleObstacle != null){
            beatleObstacle.recycle();
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




        // All the ground obstacles have the same height
        heightForGroundObstacles = (int) (playerHeight * HEIGHT_RATIO_GROUND_OBSTACLE);
        heightForFlyingObstacles = (int) (playerHeight * HEIGHT_RATIO_FLYING_OBSTACLE);

        bobObstacleWidth = (heightForGroundObstacles * BOB_FRAME_WIDTH) /
                BOB_FRAME_HEIGHT;
        bobObstacle = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
                R.drawable.bobrun), bobObstacleWidth * BOB_NUMBER_OF_FRAMES, heightForGroundObstacles, true);

        chompObstacleWidth = (heightForGroundObstacles * CHOMP_FRAME_WIDTH) /
                CHOMP_FRAME_HEIGHT;
        chompObstacle = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.chomprun), chompObstacleWidth * CHOMP_NUMBER_OF_FRAMES,
                heightForGroundObstacles, true);

        goombaObstacleWidth = (heightForGroundObstacles * GOOMBA_FRAME_WIDTH) /
                GOOMBA_FRAME_HEIGHT;
        goombaObstacle = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.goombarun), goombaObstacleWidth * GOOMBA_NUMBER_OF_FRAMES,
                heightForGroundObstacles, true);

        goombaVoladorObstacleWidth = (heightForFlyingObstacles * GOOMBA_VOLADOR_FRAME_WIDTH) /
                GOOMBA_VOLADOR_FRAME_HEIGHT;
        goombaVoladorObstacle = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.goombavolador), goombaVoladorObstacleWidth * GOOMBA_VOLADOR_NUMBER_OF_FRAMES,
                heightForFlyingObstacles, true);

        lakituObstacleWidth = (heightForFlyingObstacles * LAKITU_FRAME_WIDTH) /
                LAKITU_FRAME_HEIGHT;
        lakituObstacle = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.lakiturun), lakituObstacleWidth * LAKITU_NUMBER_OF_FRAMES,
                heightForFlyingObstacles, true);

        plantaObstacleWidth = (heightForGroundObstacles * PLANTA_FRAME_WIDTH) /
                PLANTA_FRAME_HEIGHT;
        plantaObstacle = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.plantarun), plantaObstacleWidth * PLANTA_NUMBER_OF_FRAMES,
                heightForGroundObstacles, true);

        demiseGroundObstacleWidth = (heightForGroundObstacles * DEMISE_GROUND_FRAME_WIDTH) /
                DEMISE_GROUND_FRAME_HEIGHT;
        demiseGroundObstacle = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.demisegroundsp), demiseGroundObstacleWidth * DEMISE_GROUND_NUMBER_OF_FRAMES,
                heightForGroundObstacles, true);

        demiseFlyingObstacleWidth = (heightForFlyingObstacles * DEMISE_FLYING_FRAME_WIDTH) /
                DEMISE_FLYING_FRAME_HEIGHT;
        demiseFlyingObstacle = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.demiseflyingsp), demiseFlyingObstacleWidth * DEMISE_FLYING_NUMBER_OF_FRAMES,
                heightForFlyingObstacles, true);



        beatleObstacleWidth = (heightForGroundObstacles * BEATLE_FRAME_WIDTH) /
                BEATLE_FRAME_HEIGHT;
        beatleObstacle = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
                R.drawable.beatle), beatleObstacleWidth * BEATLE_NUMBER_OF_FRAMES, heightForGroundObstacles, true);

        booObstacleWidth = (heightForFlyingObstacles * BOO_FRAME_WIDTH) /
                BOO_FRAME_HEIGHT;
        booObstacle = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.boo), booObstacleWidth * BOO_NUMBER_OF_FRAMES,
                heightForFlyingObstacles, true);


        hudHeight = (int) (playerHeight * HEIGHT_RATIO_HUD);

        coinHudWidth = (hudHeight * COIN_FRAME_WIDTH) /
                COIN_FRAME_HEIGHT;
        coin = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.coin), coinHudWidth, hudHeight, true);


        heartHudWidth = (hudHeight * HEART_FRAME_WIDTH) /
                HEART_FRAME_HEIGHT;
        heart = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.corazon), heartHudWidth, hudHeight, true);

        //lifecontainerHudWidth = (hudHeight * LIFECONTAINER_FRAME_WIDTH) / LIFECONTAINER_FRAME_HEIGHT;
        lifecontainerHudWidth = LIFECONTAINERWIDTH;

        lifeContainer = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.pipelife), lifecontainerHudWidth, hudHeight + LIFECONTAINERWIDTH/6, true);



        coinsHeight = (int) (playerHeight * HEIGHT_RATIO_COINS);

        coinsWidth = (coinsHeight * COINS_FRAME_WIDTH) /
                COINS_FRAME_HEIGHT;
        coins = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
                R.drawable.monedas), coinsWidth * COINS_NUMBER_OF_FRAMES, coinsHeight, true);

    }

}
