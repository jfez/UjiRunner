package com.example.jorge.ujirunnerapp.testObstacles;

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
import static com.example.jorge.ujirunnerapp.testObstacles.TestObstaclesModel.PARALLAX_LAYERS;
import static com.example.jorge.ujirunnerapp.testObstacles.TestObstaclesModel.PARALLAX_WIDTH;
import static com.example.jorge.ujirunnerapp.testObstacles.TestObstaclesModel.STAGE_HEIGHT;
import static com.example.jorge.ujirunnerapp.testObstacles.TestObstaclesModel.STAGE_WIDTH;

public class TestObstaclesController implements IGameController {

    private static final int BASELINE = 255;
    private static final int TOPLINE = BASELINE - 100;
    private static final int THRESHOLD = 150;
    //private static final int SQUARE_SIZE = 40;
    private static final float PLAYER_WIDTH_PERCENT = 0.15f;

    private int playerWidth;

    private float scaleX;
    private float scaleY;
    Graphics graphics;
    TestObstaclesModel model;

    private Sprite[] bgParallax;
    private Sprite[] shiftedBGParallax;

    private Sprite runner;
    private RectF whereToDraw;
    private Rect frameToDraw;

    private List<Sprite> groundObstacles;
    private List<Sprite> flyingObstacles;
    private List<Sprite> demises;




    public TestObstaclesController(TestObstacles testParallax, int screenWidth, int screenHeight) {
        graphics = new Graphics(testParallax, STAGE_WIDTH, STAGE_HEIGHT);
        scaleX = (float) STAGE_WIDTH / screenWidth;
        scaleY = (float) STAGE_HEIGHT / screenHeight;
        playerWidth =(int) (STAGE_WIDTH * PLAYER_WIDTH_PERCENT);
        Assets.createAssets(testParallax.getApplicationContext(), playerWidth, STAGE_HEIGHT, PARALLAX_WIDTH);
        model = new TestObstaclesModel(playerWidth, BASELINE, TOPLINE, THRESHOLD);
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


        //graphics.drawRect(runner.getX(),runner.getY(),runner.getSizeX(), runner.getSizeY(), 0xFF0FB40F);

        for (Sprite sprite : groundObstacles)
        {
            frameToDraw = sprite.getRect();

            whereToDraw = new RectF(sprite.getX(), sprite.getY(), sprite.getX() + sprite.getSizeX(), sprite.getY() + sprite.getSizeY());

            graphics.drawAnimatedBitmap(sprite.getBitmapToRender(), frameToDraw, whereToDraw, false);

        }

        for (Sprite sprite : flyingObstacles)
        {
            frameToDraw = sprite.getRect();

            whereToDraw = new RectF(sprite.getX(), sprite.getY(), sprite.getX() + sprite.getSizeX(), sprite.getY() + sprite.getSizeY());

            graphics.drawAnimatedBitmap(sprite.getBitmapToRender(), frameToDraw, whereToDraw, false);

        }

        frameToDraw = runner.getRect();

        whereToDraw = new RectF(runner.getX(), runner.getY(), runner.getX() + runner.getSizeX(), runner.getY() + runner.getSizeY());

        graphics.drawAnimatedBitmap(runner.getBitmapToRender(), frameToDraw, whereToDraw, false);

        for (Sprite sprite : demises)
        {
            frameToDraw = sprite.getRect();

            whereToDraw = new RectF(sprite.getX(), sprite.getY(), sprite.getX() + sprite.getSizeX(), sprite.getY() + sprite.getSizeY());

            graphics.drawAnimatedBitmap(sprite.getBitmapToRender(), frameToDraw, whereToDraw, false);

        }




        return graphics.getFrameBuffer();


    }
}
