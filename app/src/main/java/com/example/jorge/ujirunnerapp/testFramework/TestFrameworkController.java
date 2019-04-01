package com.example.jorge.ujirunnerapp.testFramework;

import android.graphics.Bitmap;

import com.example.jorge.ujirunnerapp.framework.Graphics;
import com.example.jorge.ujirunnerapp.framework.IGameController;
import com.example.jorge.ujirunnerapp.framework.TouchHandler;

import java.util.List;

import static com.example.jorge.ujirunnerapp.framework.TouchHandler.TouchType.TOUCH_DOWN;

public class TestFrameworkController implements IGameController {

    private static final int BASELINE = 700;
    private static final int TOPLINE = BASELINE - 200;
    private static final int THRESHOLD = 700;

    int squareSize;
    int screenWidth;
    int screenHeight;
    int squareX;
    int squareY;
    Graphics graphics;



    public TestFrameworkController(TestFramework testFramework, int size, int width, int height) {
        this.squareSize = size;
        this.screenWidth = width;
        this.screenHeight = height;

        squareX = this.screenWidth/5;
        squareY = BASELINE - this.squareSize;

        graphics = new Graphics(testFramework, width, height);


    }

    @Override
    public void onUpdate(float deltaTime, List<TouchHandler.TouchEvent> touchEvents) {
        for (TouchHandler.TouchEvent event : touchEvents){
            if (event.type == TOUCH_DOWN) {
                if (event.x > THRESHOLD + squareSize && squareX != screenWidth*3/5){
                    squareX = screenWidth*3/5;
                }

                else if (event.x < THRESHOLD && squareX != screenWidth/5){
                    squareX = screenWidth/5;
                }

                if (event.y < TOPLINE && squareY == BASELINE){
                    squareY = BASELINE - squareSize;
                }

                else if (event.y < TOPLINE && squareY == BASELINE - squareSize){
                    squareY = TOPLINE - squareSize;

                }

                if (event.y > TOPLINE && event.y < BASELINE && squareY != BASELINE - squareSize){
                    squareY = BASELINE - squareSize;
                }

                if (event.y > BASELINE && squareY == TOPLINE - squareSize){
                    squareY = BASELINE - squareSize;
                }

                else if (event.y > BASELINE && squareY == BASELINE - squareSize){
                    squareY = BASELINE;
                }



            }

        }


    }

    @Override
    public Bitmap onDrawingRequested() {
        graphics.clear(0xFFFFFFFF);
        graphics.drawLine(0,BASELINE, screenWidth, BASELINE, 0xFF0F00FF, 7);
        graphics.drawLine(0,TOPLINE, screenWidth, TOPLINE, 0xFFFF0000, 7);

        graphics.drawRect(squareX,squareY,squareSize, squareSize, 0xFF0FB40F);

        return graphics.getFrameBuffer();
    }
}
