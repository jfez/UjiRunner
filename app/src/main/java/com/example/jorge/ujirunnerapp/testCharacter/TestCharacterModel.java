package com.example.jorge.ujirunnerapp.testCharacter;

import com.example.jorge.ujirunnerapp.Assets;
import com.example.jorge.ujirunnerapp.model.Animation;
import com.example.jorge.ujirunnerapp.model.Sprite;

enum RunnerState
{
    RUNNING, CROUCHING, JUMPING;
}

public class TestCharacterModel {

    public static final int STAGE_WIDTH = 480;
    public static final int STAGE_HEIGHT = 320;
    public static final int PARALLAX_WIDTH = 569;   //568,8888889
    public static final int PARALLAX_LAYERS = 5;
    public static final int START_X = STAGE_WIDTH / 8;
    public static final int END_X = (STAGE_WIDTH * 5) / 8;

    private static final float UNIT_TIME = 1f / 30;
    private static final int RUNNER_SPEED = 7;
    private static final int JUMP_OFFSET = 20;

    private float tickTime;

    private int playerWidth;
    private int baseline;
    private int topline;
    private int threshold;



    private Sprite[] bgParallax;
    private Sprite[] shiftedBgParallax;
    private int speed;

    private Sprite runner;
    private RunnerState runnerState;

    private int[] runnerWidths;
    private int[] runnerHeights;


    private boolean isRunningForward;
    private boolean isRunningBackwards;

    private int time;


    public TestCharacterModel(int playerWidth, int baseline, int topline, int threshold) {
        this.playerWidth = playerWidth;
        this.baseline = baseline;
        this.topline = topline;
        this.threshold = threshold;
        tickTime = 0;
        isRunningForward = false;
        isRunningBackwards = false;


        //squareX = STAGE_WIDTH / 5;
        //squareY = baseline - this.playerWidth;

        speed = 60;
        time = 3;


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

        // Creation of the arrays
        runnerWidths = new int[RunnerState.JUMPING.ordinal() + 1];
        runnerHeights = new int[RunnerState.JUMPING.ordinal() + 1];

        // Creation of the runner. Set initial position and dimensions
        runner = new Sprite(Assets.characterRunning, false, START_X, this.baseline -
                Assets.playerHeight, 0, 0, playerWidth, Assets.playerHeight);

        // set runner state: initially running
        runnerState = RunnerState.RUNNING;

        // store dimensions when running
        runnerWidths[runnerState.ordinal()] = playerWidth;
        runnerHeights[runnerState.ordinal()] = Assets.playerHeight;

        // get and store dimensions when crouching
        runnerWidths[RunnerState.CROUCHING.ordinal()] = Assets.runnerCrouchesWidth;
        runnerHeights[RunnerState.CROUCHING.ordinal()] = Assets.runnerCrouchesHeight;

        // get and store dimensions when jumping
        runnerWidths[RunnerState.JUMPING.ordinal()] = Assets.runnerJumpsWidth;
        runnerHeights[RunnerState.JUMPING.ordinal()] = Assets.runnerJumpsHeight;


        runner.addAnimation(new Animation(1, Assets.CHARACTER_RUN_NUMBER_OF_FRAMES,runnerWidths[RunnerState.RUNNING.ordinal()],
                runnerHeights[RunnerState.RUNNING.ordinal()], runnerWidths[RunnerState.RUNNING.ordinal()] * Assets.CHARACTER_RUN_NUMBER_OF_FRAMES, 30));

        runner.addAnimation(new Animation(1, Assets.CHARACTER_CROUCH_NUMBER_OF_FRAMES,runnerWidths[RunnerState.CROUCHING.ordinal()],
                runnerHeights[RunnerState.CROUCHING.ordinal()], runnerWidths[RunnerState.CROUCHING.ordinal()] * Assets.CHARACTER_CROUCH_NUMBER_OF_FRAMES, 30));

        runner.addAnimation(new Animation(1, Assets.CHARACTER_JUMP_NUMBER_OF_FRAMES,runnerWidths[RunnerState.JUMPING.ordinal()],
                runnerHeights[RunnerState.JUMPING.ordinal()], runnerWidths[RunnerState.JUMPING.ordinal()] * Assets.CHARACTER_JUMP_NUMBER_OF_FRAMES, 30));



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

    public void onTouch(float touchX, float touchY){


        if (runnerState == RunnerState.RUNNING){
            if (touchX > threshold + runnerWidths[RunnerState.RUNNING.ordinal()] && isRunningBackwards){
                runner.setSpeedX(0);
                runner.Move(time);
                isRunningBackwards = false;
            }

            else if (touchX > threshold + runnerWidths[RunnerState.RUNNING.ordinal()] && isRunningForward){

            }

            else if (touchX > threshold + runnerWidths[RunnerState.RUNNING.ordinal()] && !isRunningForward && !isRunningBackwards){
                runner.setSpeedX(RUNNER_SPEED);
                runner.Move(time);
                isRunningForward = true;

            }

            if (touchX < threshold && isRunningBackwards){

            }

            else if (touchX < threshold && isRunningForward){
                runner.setSpeedX(0);
                runner.Move(time);
                isRunningForward = false;

            }

            else if (touchX < threshold && !isRunningForward && !isRunningBackwards){
                runner.setSpeedX(-RUNNER_SPEED);
                runner.Move(time);
                isRunningBackwards = true;

            }

            if (runner.getX() <= START_X){
                runner.setSpeedX(0);
                runner.Move(time);
                isRunningBackwards = false;
            }

            else if (runner.getX() >= END_X){
                runner.setSpeedX(0);
                runner.Move(time);
                isRunningForward = false;
            }

            if (touchY < topline){
                runnerState = RunnerState.JUMPING;
                runner.getAnimation(RunnerState.JUMPING.ordinal()).resetAnimation();
                runner.setBitmapToRender(Assets.characterJumping);
                runner.setSizeX(runnerWidths[RunnerState.JUMPING.ordinal()]);
                runner.setSizeY(runnerHeights[RunnerState.JUMPING.ordinal()]);
                runner.setY(topline - runnerHeights[RunnerState.JUMPING.ordinal()] - JUMP_OFFSET );       //Corriendo pasamos a salto
                runner.setSpeedX(0);
                runner.Move(time);
                isRunningForward = false;
                isRunningBackwards = false;
            }

            else if (touchY > baseline){
                runnerState = RunnerState.CROUCHING;
                runner.getAnimation(RunnerState.CROUCHING.ordinal()).resetAnimation();
                runner.setBitmapToRender(Assets.characterCrouching);
                runner.setSizeX(runnerWidths[RunnerState.CROUCHING.ordinal()]);
                runner.setSizeY(runnerHeights[RunnerState.CROUCHING.ordinal()]);
                runner.setY(baseline);       //Corriendo pasamos a agachar
                runner.setSpeedX(0);
                runner.Move(time);
                isRunningForward = false;
                isRunningBackwards = false;
            }

        }

        else if (runnerState == RunnerState.CROUCHING){
            if (touchY < baseline){
                runnerState = RunnerState.RUNNING;
                runner.getAnimation(RunnerState.RUNNING.ordinal()).resetAnimation();
                runner.setBitmapToRender(Assets.characterRunning);
                runner.setSizeX(runnerWidths[RunnerState.RUNNING.ordinal()]);
                runner.setSizeY(runnerHeights[RunnerState.RUNNING.ordinal()]);
                runner.setY(baseline - runnerHeights[RunnerState.RUNNING.ordinal()] );       //Agachar pasamos a salto pero no salta sino que corre o pasamos a correr

            }
        }

    }



    public Sprite[] getBgParallax() {
        return bgParallax;
    }

    public Sprite[] getShiftedBgParallax() {
        return shiftedBgParallax;
    }

}
