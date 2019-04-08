package com.example.jorge.ujirunnerapp.testParallax;

import com.example.jorge.ujirunnerapp.Assets;
import com.example.jorge.ujirunnerapp.framework.TouchHandler;
import static com.example.jorge.ujirunnerapp.framework.TouchHandler.TouchType.TOUCH_DOWN;
import com.example.jorge.ujirunnerapp.framework.TouchHandler;
import com.example.jorge.ujirunnerapp.model.Sprite;

public class TestParallaxModel {

    public static final int STAGE_WIDTH = 480;
    public static final int STAGE_HEIGHT = 320;
    public static final int PARALLAX_WIDTH = 569;   //568,8888889
    public static final int PARALLAX_LAYERS = 5;

    private static final float UNIT_TIME = 1f / 30;

    private float tickTime;

    private int playerWidth;
    private int baseline;
    private int topline;
    private int threshold;

    private int squareX;
    private int squareY;

    private Sprite[] bgParallax;
    private Sprite[] shiftedBgParallax;
    private int speed;

    public TestParallaxModel(int playerWidth, int baseline, int topline, int threshold) {
        this.playerWidth = playerWidth;
        this.baseline = baseline;
        this.topline = topline;
        this.threshold = threshold;
        tickTime = 0;

        squareX = STAGE_WIDTH / 5;
        squareY = baseline - this.playerWidth;

        speed = 60;


        // Set speeds and initial pos X for parallax layers
        bgParallax = new Sprite[PARALLAX_LAYERS];
        shiftedBgParallax = new Sprite[PARALLAX_LAYERS];
        for (int i = 0; i < PARALLAX_LAYERS; i++) {
            speed = speed -10; // a different velocity for each sprite explained before
            bgParallax[i] = new Sprite(Assets.bgLayers[i], false, 0f, 0f, -speed,
                    0, PARALLAX_WIDTH, STAGE_HEIGHT);
            shiftedBgParallax[i] = new Sprite(Assets.bgLayers[i], false, PARALLAX_WIDTH,
                    0f, -speed, 0, PARALLAX_WIDTH, STAGE_HEIGHT);


        }
    }

    public void update(float deltaTime) {
        tickTime += deltaTime;
        while (tickTime >= UNIT_TIME) {
            tickTime -= UNIT_TIME;
            updateParallaxBg();
        }
    }

    private void updateParallaxBg() {

        for (int i = 0; i < PARALLAX_LAYERS; i++){
            bgParallax[i].Move(UNIT_TIME);
            shiftedBgParallax[i].Move(UNIT_TIME);

            if (shiftedBgParallax[i].getX() <= 0){
                bgParallax[i].setX(0);
                shiftedBgParallax[i].setX(PARALLAX_WIDTH);
            }
        }
    }

    public void onTouch(float scaleX, float scaleY){



        if (scaleX > threshold + playerWidth && squareX != STAGE_WIDTH*3/5){
            squareX = STAGE_WIDTH*3/5;
        }

        else if (scaleX < threshold && squareX != STAGE_WIDTH/5){
            squareX = STAGE_WIDTH/5;
        }

        if (scaleY < topline && squareY == baseline){
            squareY = baseline - playerWidth;
        }

        else if (scaleY < topline && squareY == baseline - playerWidth){
            squareY = topline - playerWidth;

        }

        if (scaleY > topline && scaleY < baseline && squareY != baseline - playerWidth){
            squareY = baseline - playerWidth;
        }

        if (scaleY > baseline && squareY == topline - playerWidth){
            squareY = baseline - playerWidth;
        }

        else if (scaleY > baseline && squareY == baseline - playerWidth){
            squareY = baseline;
        }

    }

    public int getSquareX() {
        return squareX;
    }

    public int getSquareY() {
        return squareY;
    }

    public Sprite[] getBgParallax() {
        return bgParallax;
    }

    public Sprite[] getShiftedBgParallax() {
        return shiftedBgParallax;
    }

}
