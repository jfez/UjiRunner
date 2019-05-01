package com.example.jorge.ujirunnerapp.testParallax;

import android.app.Application;
import android.graphics.Bitmap;

import com.example.jorge.ujirunnerapp.Assets;
import com.example.jorge.ujirunnerapp.framework.Graphics;
import com.example.jorge.ujirunnerapp.framework.IGameController;
import com.example.jorge.ujirunnerapp.framework.TouchHandler;
import com.example.jorge.ujirunnerapp.model.Sprite;

import java.util.List;

import static com.example.jorge.ujirunnerapp.framework.TouchHandler.TouchType.TOUCH_DOWN;
import static com.example.jorge.ujirunnerapp.testParallax.TestParallaxModel.PARALLAX_LAYERS;
import static com.example.jorge.ujirunnerapp.testParallax.TestParallaxModel.PARALLAX_WIDTH;
import static com.example.jorge.ujirunnerapp.testParallax.TestParallaxModel.STAGE_HEIGHT;
import static com.example.jorge.ujirunnerapp.testParallax.TestParallaxModel.STAGE_WIDTH;

public class TestParallaxController implements IGameController {

    private static final int BASELINE = 275;
    private static final int TOPLINE = BASELINE - 55;
    private static final int THRESHOLD = 200;
    private static final int SQUARE_SIZE = 40;

    private float scaleX;
    private float scaleY;
    Graphics graphics;
    TestParallaxModel model;

    private Sprite[] bgParallax;
    private Sprite[] shiftedBGParallax;




    public TestParallaxController(TestParallax testParallax, int screenWidth, int screenHeight) {
        graphics = new Graphics(testParallax, STAGE_WIDTH, STAGE_HEIGHT);
        scaleX = (float) STAGE_WIDTH / screenWidth;
        scaleY = (float) STAGE_HEIGHT / screenHeight;
        Assets.createAssets(testParallax.getApplicationContext(), SQUARE_SIZE, STAGE_HEIGHT, PARALLAX_WIDTH);
        model = new TestParallaxModel(SQUARE_SIZE, BASELINE, TOPLINE, THRESHOLD);
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

        graphics.drawLine(0,BASELINE, STAGE_WIDTH, BASELINE, 0xFF0F00FF, 7);
        graphics.drawLine(0,TOPLINE, STAGE_WIDTH, TOPLINE, 0xFFFF0000, 7);

        graphics.drawRect(model.getSquareX(),model.getSquareY(),SQUARE_SIZE, SQUARE_SIZE, 0xFF0FB40F);

        return graphics.getFrameBuffer();
    }
}
