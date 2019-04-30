package com.example.jorge.ujirunnerapp.testLevelsHUD;

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
import static com.example.jorge.ujirunnerapp.testLevelsHUD.TestLevelsHudModel.PARALLAX_LAYERS;
import static com.example.jorge.ujirunnerapp.testLevelsHUD.TestLevelsHudModel.PARALLAX_WIDTH;
import static com.example.jorge.ujirunnerapp.testLevelsHUD.TestLevelsHudModel.STAGE_HEIGHT;
import static com.example.jorge.ujirunnerapp.testLevelsHUD.TestLevelsHudModel.STAGE_WIDTH;

public class TestLevelsHudController implements IGameController {

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

    private int playerWidth;

    private float scaleX;
    private float scaleY;
    Graphics graphics;
    TestLevelsHudModel model;

    private Sprite[] bgParallax;
    private Sprite[] shiftedBGParallax;

    private Sprite runner;
    private RectF whereToDraw;
    private Rect frameToDraw;

    private List<Sprite> groundObstacles;
    private List<Sprite> flyingObstacles;
    private List<Sprite> demises;

    private Sprite coin;
    private Sprite heart;
    private Sprite lifeContainer;




    public TestLevelsHudController(TestLevelsHud testParallax, int screenWidth, int screenHeight) {
        graphics = new Graphics(testParallax, STAGE_WIDTH, STAGE_HEIGHT);
        scaleX = (float) STAGE_WIDTH / screenWidth;
        scaleY = (float) STAGE_HEIGHT / screenHeight;
        playerWidth =(int) (STAGE_WIDTH * PLAYER_WIDTH_PERCENT);
        Assets.createAssets(testParallax.getApplicationContext(), playerWidth, STAGE_HEIGHT, PARALLAX_WIDTH);
        model = new TestLevelsHudModel(playerWidth, BASELINE, TOPLINE, THRESHOLD);
    }

    @Override
    public void onUpdate(float deltaTime, List<TouchHandler.TouchEvent> touchEvents) {

        for (TouchHandler.TouchEvent event : touchEvents){
            if (event.type == TOUCH_DOWN) {
                model.onTouch(event.x * scaleX, event.y * scaleY);
            }
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
        coin = model.getCoin();
        heart = model.getHeart();
        lifeContainer = model.getLifeContainer();


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


        frameToDraw = runner.getRect();

        whereToDraw = new RectF(runner.getX(), runner.getY(), runner.getX() + runner.getSizeX(), runner.getY() + runner.getSizeY());

        graphics.drawAnimatedBitmap(runner.getBitmapToRender(), frameToDraw, whereToDraw, false);

        synchronized (demises){
            for (Sprite sprite : demises)
            {
                frameToDraw = sprite.getRect();

                whereToDraw = new RectF(sprite.getX(), sprite.getY(), sprite.getX() + sprite.getSizeX(), sprite.getY() + sprite.getSizeY());

                graphics.drawAnimatedBitmap(sprite.getBitmapToRender(), frameToDraw, whereToDraw, false);

            }
        }

        graphics.drawBitmap(heart.getBitmapToRender(), heart.getX(), heart.getY(), false);
        graphics.drawBitmap(coin.getBitmapToRender(), coin.getX(), coin.getY(), false);
        graphics.drawText(model.getCoinsCollected()+"", coin.getX() + COIN_MARGIN_X, coin.getY() + coin.getSizeY(), 0xFFffe65d, FONT_SIZE);
        graphics.drawText(model.getMetresTravelled()+" METRES", lifeContainer.getX() + lifeContainer.getSizeX(), coin.getY() + coin.getSizeY(), 0xFFffe65d, FONT_SIZE-7);
        graphics.drawRect(lifeContainer.getX()+MARGIN_PIPE_X, lifeContainer.getY()+MARGIN_PIPE_Y, lifeContainer.getSizeX() - MARGIN_PIPE_SIZEX, lifeContainer.getSizeY() - MARGIN_PIPE_SIZEY, 0xFF0FB40F);
        graphics.drawBitmap(lifeContainer.getBitmapToRender(), lifeContainer.getX(), lifeContainer.getY(), false);


        return graphics.getFrameBuffer();


    }
}
