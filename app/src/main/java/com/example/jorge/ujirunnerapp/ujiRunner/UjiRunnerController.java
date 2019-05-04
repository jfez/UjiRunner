package com.example.jorge.ujirunnerapp.ujiRunner;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.jorge.ujirunnerapp.Assets;
import com.example.jorge.ujirunnerapp.framework.Graphics;
import com.example.jorge.ujirunnerapp.framework.IGameController;
import com.example.jorge.ujirunnerapp.framework.TouchHandler;
import com.example.jorge.ujirunnerapp.model.Sprite;

import java.util.List;

import static com.example.jorge.ujirunnerapp.framework.TouchHandler.TouchType.TOUCH_DOWN;
import static com.example.jorge.ujirunnerapp.framework.TouchHandler.TouchType.TOUCH_UP;
import static com.example.jorge.ujirunnerapp.ujiRunner.UjiRunnerModel.PARALLAX_LAYERS;
import static com.example.jorge.ujirunnerapp.ujiRunner.UjiRunnerModel.PARALLAX_WIDTH;
import static com.example.jorge.ujirunnerapp.ujiRunner.UjiRunnerModel.STAGE_HEIGHT;
import static com.example.jorge.ujirunnerapp.ujiRunner.UjiRunnerModel.STAGE_WIDTH;

public class UjiRunnerController implements IGameController {

    private static final int BASELINE = 255;
    private static final int TOPLINE = BASELINE - 100;
    private static final int THRESHOLD = 150;
    //private static final int SQUARE_SIZE = 40;
    private static final float PLAYER_WIDTH_PERCENT = 0.15f;
    private static final int COIN_MARGIN_X = 30;
    private static final int FONT_SIZE = 25;
    private static final int MARGIN_PIPE_X = 13;
    private static final int MARGIN_PIPE_Y = 13;
    private static final int MARGIN_PIPE_SIZEX = 40;
    private static final int MARGIN_PIPE_SIZEY = 4;
    private static final int MARGIN_LEVEL_X = 10;

    public static final int START_TEXT_X = (STAGE_WIDTH ) / 8;
    public static final int START_TEXT_Y = (STAGE_HEIGHT ) / 3;
    public static final int MARGIN_START_TEXT_X = 30;
    public static final int MARGIN_START_TEXT_Y = 20;

    private int playerWidth;

    private float scaleX;
    private float scaleY;
    Graphics graphics;
    UjiRunnerModel model;

    private Sprite[] bgParallax;
    private Sprite[] shiftedBGParallax;

    private Sprite runner;
    private RectF whereToDraw;
    private Rect frameToDraw;

    private List<Sprite> groundObstacles;
    private List<Sprite> flyingObstacles;
    private List<Sprite> beatles;
    private List<Sprite> boos;
    private List<Sprite> demises;
    private List<Sprite> coins;

    private Sprite coin;
    private Sprite heart;
    private Sprite lifeContainer;
    private Sprite restart;
    private Sprite idleMario;

    private float xPlayAgain;
    private float yPlayAgain;
    private float xEndPlayAgain;
    private float yEndPlayAgain;




    public UjiRunnerController(UjiRunner testParallax, int screenWidth, int screenHeight) {
        graphics = new Graphics(testParallax, STAGE_WIDTH, STAGE_HEIGHT);
        scaleX = (float) STAGE_WIDTH / screenWidth;
        scaleY = (float) STAGE_HEIGHT / screenHeight;
        playerWidth =(int) (STAGE_WIDTH * PLAYER_WIDTH_PERCENT);
        Assets.createAssets(testParallax.getApplicationContext(), playerWidth, STAGE_HEIGHT, PARALLAX_WIDTH);
        model = new UjiRunnerModel(playerWidth, BASELINE, TOPLINE, THRESHOLD);

        model.setSoundPlayer(new UjiRunnerModel.SoundPlayer() {
            @Override
            public void characterJumps() {
                Assets.characterJumpsSound.play(1.0f);
            }

            @Override
            public void characterCrouches() {
                Assets.characterCrouchesSound.play(1.0f);
            }

            @Override
            public void characterCollision() {
                Assets.characterCollisionSound.play(1.0f);

            }

            @Override
            public void coinCollected() {
                Assets.coinCollectedSound.play(1.0f);

            }

            @Override
            public void characterDies() {
                Assets.characterDiesSound.play(1.0f);

            }

            @Override
            public void coinAppears() {
                Assets.coinAppearsSound.play(1.0f);

            }

            @Override
            public void groundedEnemyAppears() {
                Assets.groundedEnemyAppearsSound.play(1.0f);

            }

            @Override
            public void flyingEnemyAppears() {
                Assets.flyingEnemyAppearsSound.play(1.0f);

            }

            @Override
            public void recoverLife() {
                Assets.recoverLifeSound.play(1.0f);
            }


        });

        restart = model.getRestart();
        xPlayAgain = restart.getX();
        yPlayAgain = restart.getY();
        xEndPlayAgain = restart.getX() + restart.getSizeX();
        yEndPlayAgain = restart.getY() + restart.getSizeY();
    }

    @Override
    public void onUpdate(float deltaTime, List<TouchHandler.TouchEvent> touchEvents) {

        for (TouchHandler.TouchEvent event: touchEvents) {
            float xScaled = event.x * scaleX;
            float yScaled = event.y * scaleY;
            if (event.type == TOUCH_UP) {
                if (model.isOver() && xPlayAgain <= xScaled && xScaled <=
                        xEndPlayAgain && yPlayAgain <= yScaled && yScaled <=
                        yEndPlayAgain) {
                    model.restartGame();
                    break;
                }
            }
            else if (event.type == TOUCH_DOWN)
                model.onTouch(xScaled, yScaled);
        }


        model.update(deltaTime);


    }

    @Override
    public Bitmap onDrawingRequested() {
        graphics.clear(0xFFFFFFFF);
        bgParallax = model.getBgParallax();
        shiftedBGParallax = model.getShiftedBgParallax();

        for (int i = PARALLAX_LAYERS-1; i > -1; i--){
            graphics.drawBitmap(bgParallax[i].getBitmapToRender(), bgParallax[i].getX(), bgParallax[i].getY(), false);
            graphics.drawBitmap(shiftedBGParallax[i].getBitmapToRender(), shiftedBGParallax[i].getX(), shiftedBGParallax[i].getY(), false);
        }

        //No se pinta ninguna línea

        //graphics.drawLine(0,BASELINE, STAGE_WIDTH, BASELINE, 0xFF0F00FF, 7);
        //graphics.drawLine(0,TOPLINE, STAGE_WIDTH, TOPLINE, 0xFFFF0000, 7);


        runner = model.getRunner();
        groundObstacles = model.getGroundObstacles();
        flyingObstacles = model.getFlyingObstacles();
        demises = model.getDemises();
        coins = model.getCoins();
        coin = model.getCoin();
        heart = model.getHeart();
        lifeContainer = model.getLifeContainer();
        idleMario = model.getIdleMario();


        beatles = model.getBeatles();
        boos = model.getBoos();


        //graphics.drawRect(runner.getX(),runner.getY(),runner.getSizeX(), runner.getSizeY(), 0xFF0FB40F);

        synchronized (groundObstacles){
            for (Sprite sprite : groundObstacles)
            {
                frameToDraw = sprite.getRect();

                whereToDraw = new RectF(sprite.getX(), sprite.getY(), sprite.getX() + sprite.getSizeX(), sprite.getY() + sprite.getSizeY());

                graphics.drawAnimatedBitmap(sprite.getBitmapToRender(), frameToDraw, whereToDraw, false);

            }

        }


        synchronized (flyingObstacles){
            for (Sprite sprite : flyingObstacles)
            {
                frameToDraw = sprite.getRect();

                whereToDraw = new RectF(sprite.getX(), sprite.getY(), sprite.getX() + sprite.getSizeX(), sprite.getY() + sprite.getSizeY());

                graphics.drawAnimatedBitmap(sprite.getBitmapToRender(), frameToDraw, whereToDraw, false);

            }

        }

        synchronized (beatles){
            for (Sprite sprite : beatles)
            {
                frameToDraw = sprite.getRect();

                whereToDraw = new RectF(sprite.getX(), sprite.getY(), sprite.getX() + sprite.getSizeX(), sprite.getY() + sprite.getSizeY());

                graphics.drawAnimatedBitmap(sprite.getBitmapToRender(), frameToDraw, whereToDraw, false);

            }

        }


        synchronized (boos){
            for (Sprite sprite : boos)
            {
                frameToDraw = sprite.getRect();

                whereToDraw = new RectF(sprite.getX(), sprite.getY(), sprite.getX() + sprite.getSizeX(), sprite.getY() + sprite.getSizeY());

                graphics.drawAnimatedBitmap(sprite.getBitmapToRender(), frameToDraw, whereToDraw, false);

            }

        }

        synchronized (coins){
            for (Sprite sprite : coins)
            {
                frameToDraw = sprite.getRect();

                whereToDraw = new RectF(sprite.getX(), sprite.getY(), sprite.getX() + sprite.getSizeX(), sprite.getY() + sprite.getSizeY());

                graphics.drawAnimatedBitmap(sprite.getBitmapToRender(), frameToDraw, whereToDraw, false);

            }

        }


        if (model.isPlaying()){
            frameToDraw = runner.getRect();

            whereToDraw = new RectF(runner.getX(), runner.getY(), runner.getX() + runner.getSizeX(), runner.getY() + runner.getSizeY());

            graphics.drawAnimatedBitmap(runner.getBitmapToRender(), frameToDraw, whereToDraw, false);
        }

        if (model.isWaiting()){
            graphics.drawBitmap(idleMario.getBitmapToRender(), idleMario.getX(), idleMario.getY(), false);
        }



        synchronized (demises){
            for (Sprite sprite : demises)
            {
                frameToDraw = sprite.getRect();

                whereToDraw = new RectF(sprite.getX(), sprite.getY(), sprite.getX() + sprite.getSizeX(), sprite.getY() + sprite.getSizeY());

                graphics.drawAnimatedBitmap(sprite.getBitmapToRender(), frameToDraw, whereToDraw, false);

            }
        }

        if (model.isWaiting() || model.isPlaying()){
            graphics.drawBitmap(heart.getBitmapToRender(), heart.getX(), heart.getY(), false);
            graphics.drawBitmap(coin.getBitmapToRender(), coin.getX(), coin.getY(), false);
            graphics.drawText(model.getCoinsCollected()+"", coin.getX() + COIN_MARGIN_X, coin.getY() + coin.getSizeY(), 0xFFffe65d, FONT_SIZE);
            graphics.drawText(model.getMetresTravelled()+" METERS", lifeContainer.getX() + lifeContainer.getSizeX(), coin.getY() + coin.getSizeY(), 0xFFffe65d, FONT_SIZE-7);
            graphics.drawRect(lifeContainer.getX()+MARGIN_PIPE_X, lifeContainer.getY()+MARGIN_PIPE_Y, (lifeContainer.getSizeX() - MARGIN_PIPE_SIZEX) * model.getCurrentLife() / model.MAXLIFE, lifeContainer.getSizeY() - MARGIN_PIPE_SIZEY, 0xFF0FB40F);
            graphics.drawBitmap(lifeContainer.getBitmapToRender(), lifeContainer.getX(), lifeContainer.getY(), false);

            if (model.isLevelEasy()){
                graphics.drawText("EASY", coin.getX() + COIN_MARGIN_X*3, coin.getY() + coin.getSizeY(), 0xFFFFFFFF, FONT_SIZE);
            }

            else if (model.isLevelMedium()){
                graphics.drawText("MEDIUM", coin.getX() + COIN_MARGIN_X*2 + MARGIN_LEVEL_X, coin.getY() + coin.getSizeY(), 0xFFFFFFFF, FONT_SIZE);
            }

            else{
                graphics.drawText("HARD", coin.getX() + COIN_MARGIN_X*3, coin.getY() + coin.getSizeY(), 0xFFFFFFFF, FONT_SIZE);
            }
        }



        if (model.isOver()){
            graphics.drawText("YOU HAVE RUN " + model.getMetresTravelled() + " METERS", START_TEXT_X, START_TEXT_Y, 0xFFFFFFFF, FONT_SIZE);
            if (model.getCoinsCollected() == 1){
                graphics.drawText("AND COLLECTED " + model.getCoinsCollected() + " COIN!!!", START_TEXT_X, START_TEXT_Y + MARGIN_START_TEXT_Y, 0xFFFFFFFF, FONT_SIZE);
            }

            else{
                graphics.drawText("AND COLLECTED " + model.getCoinsCollected() + " COINS!!!", START_TEXT_X, START_TEXT_Y + MARGIN_START_TEXT_Y, 0xFFFFFFFF, FONT_SIZE);
            }

            graphics.drawBitmap(restart.getBitmapToRender(), restart.getX(), restart.getY(), false);
        }

        if (model.isWaiting()){
            graphics.drawText("TOUCH THE SCREEN TO START", START_TEXT_X, START_TEXT_Y, 0xFFFFFFFF, FONT_SIZE);
        }




        return graphics.getFrameBuffer();


    }
}
