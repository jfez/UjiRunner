package com.example.jorge.ujirunnerapp.testObstacles;

import android.graphics.Rect;

import com.example.jorge.ujirunnerapp.Assets;
import com.example.jorge.ujirunnerapp.model.Animation;
import com.example.jorge.ujirunnerapp.model.Sprite;

import java.util.ArrayList;
import java.util.List;

enum RunnerState
{
    RUNNING, CROUCHING, JUMPING;
}

public class TestObstaclesModel {

    public static final int STAGE_WIDTH = 480;
    public static final int STAGE_HEIGHT = 320;
    public static final int PARALLAX_WIDTH = 569;   //568,8888889
    public static final int PARALLAX_LAYERS = 5;
    public static final int START_X = STAGE_WIDTH / 8;
    public static final int END_X = (STAGE_WIDTH * 5) / 8;

    private static final float UNIT_TIME = 1f / 30;
    private static final int RUNNER_SPEED = 70;
    private static final int JUMP_OFFSET = 10;

    public static final int POOL_OBSTACLES_SIZE = 12;
    /**public static final float PROBABILITY_BOB = 0.25f;
    public static final float PROBABILITY_CHOP = 0.25f;
    public static final float PROBABILITY_GOOMBA = 0.25f;
    public static final float PROBABILITY_PLANTA = 0.25f;*/
    //Queremos la misma probabilidad para todos los enemigos de suelo así que no emplearemos las probabilidasdes

    private static final float TIME_BETWEEN_GROUND_OBSTACLES = 6.0f;
    private static final float TIME_BETWEEN_FLYING_OBSTACLES = 6.0f;
    private static final double PROB_ACTIVATION_GROUND_OBSTACLE = 0.5;
    private static final float DELAY_OBSTACLE = 5.0f;   //5.0f


    private float timeSinceLastGroundObstacle;
    private float timeSinceLastFlyingObstacle;


    private Sprite[] poolGroundObstacles;
    private int poolGroundObstaclesIndex;
    private List<Sprite> groundObstacles;

    private Sprite[] poolFlyingObstacles;
    private int poolFlyingObstaclesIndex;
    private List<Sprite> flyingObstacles;

    private Sprite[] poolGroundDemise;
    private Sprite[] poolFlyingDemise;
    private int poolGroundDemiseIndex;
    private int poolFlyingDemiseIndex;
    private List<Sprite> demises;



    private float tickTime;

    private int playerWidth;
    private int baseline;
    private int topline;
    private int threshold;

    private Sprite[] bgParallax;
    private Sprite[] shiftedBgParallax;
    private int speed;
    private int speedGroundObstacles;
    private int speedFlyingObstacles;

    private Sprite runner;
    private RunnerState runnerState;
    private Rect currentFrame;

    private int[] runnerWidths;
    private int[] runnerHeights;


    private boolean isRunningForward;
    private boolean isRunningBackwards;

    private int time;


    public TestObstaclesModel(int playerWidth, int baseline, int topline, int threshold) {
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
        speedGroundObstacles = 40;
        speedFlyingObstacles = 80;
        //time = 3;


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
                runnerHeights[RunnerState.RUNNING.ordinal()], runnerWidths[RunnerState.RUNNING.ordinal()] * Assets.CHARACTER_RUN_NUMBER_OF_FRAMES, 10));

        runner.addAnimation(new Animation(1, Assets.CHARACTER_CROUCH_NUMBER_OF_FRAMES,runnerWidths[RunnerState.CROUCHING.ordinal()],
                runnerHeights[RunnerState.CROUCHING.ordinal()], runnerWidths[RunnerState.CROUCHING.ordinal()] * Assets.CHARACTER_CROUCH_NUMBER_OF_FRAMES, 10));

        runner.addAnimation(new Animation(1, Assets.CHARACTER_JUMP_NUMBER_OF_FRAMES,runnerWidths[RunnerState.JUMPING.ordinal()],
                runnerHeights[RunnerState.JUMPING.ordinal()], runnerWidths[RunnerState.JUMPING.ordinal()] * Assets.CHARACTER_JUMP_NUMBER_OF_FRAMES, 10));

        createObstacles();
        createDemises();

        timeSinceLastGroundObstacle = 0f;
        timeSinceLastFlyingObstacle = 0f;

    }

    public void update(float deltaTime) {
        tickTime += deltaTime;
        while (tickTime >= UNIT_TIME) {
            if(runnerState == RunnerState.JUMPING){
                if (runner.getAnimation(RunnerState.JUMPING.ordinal()).hasRun()){
                    runnerState = RunnerState.RUNNING;
                    runner.getAnimation(RunnerState.RUNNING.ordinal()).resetAnimation();
                    runner.setBitmapToRender(Assets.characterRunning);
                    runner.setSizeX(runnerWidths[RunnerState.RUNNING.ordinal()]);
                    runner.setSizeY(runnerHeights[RunnerState.RUNNING.ordinal()]);
                    runner.setY(baseline - runnerHeights[RunnerState.RUNNING.ordinal()] );
                }
            }

            tickTime -= UNIT_TIME;
            updateParallaxBg();
            activateGroundObstacle();
            activateFlyingObjects();
            updateObstacles();
            updateRunner();
            checkCollisions();
            updateDemises();

        }
    }

    private void updateDemises() {
        for (Sprite sprite : demises)
        {
            sprite.setRect(sprite.getAnimation().getCurrentFrame(UNIT_TIME));

            if (sprite.getAnimation().hasRun()){
                demises.remove(sprite);

            }

        }


    }

    private void checkCollisions() {

        for (Sprite sprite : groundObstacles)
        {
            if (runner.overlapBoundingBoxes(sprite)){
                // A ground demise is activated
                poolGroundDemise[poolGroundDemiseIndex].setX(runner.getX());
                //la Y y la velocidad, ya están definidas al crear el obstáculo
                poolGroundDemise[poolGroundDemiseIndex].getAnimation().resetAnimation();
                demises.add(poolGroundDemise[poolGroundDemiseIndex]);
                groundObstacles.remove(sprite);

                poolGroundDemiseIndex++;

                if (poolGroundDemiseIndex >= POOL_OBSTACLES_SIZE){
                    poolGroundDemiseIndex = 0;
                }
            }
        }

        for (Sprite sprite : flyingObstacles)
        {
            if (runner.overlapBoundingBoxes(sprite)){
                // A flying demise is activated
                poolFlyingDemise[poolFlyingDemiseIndex].setX(runner.getX());
                //la Y y la velocidad, ya están definidas al crear el obstáculo
                poolFlyingDemise[poolFlyingDemiseIndex].getAnimation().resetAnimation();
                demises.add(poolFlyingDemise[poolFlyingDemiseIndex]);
                flyingObstacles.remove(sprite);

                poolFlyingDemiseIndex++;

                if (poolFlyingDemiseIndex >= POOL_OBSTACLES_SIZE){
                    poolFlyingDemiseIndex = 0;
                }
            }
        }






    }

    private void updateObstacles() {
        for (Sprite sprite : groundObstacles)
        {
            sprite.Move(UNIT_TIME);
            sprite.setRect(sprite.getAnimation().getCurrentFrame(UNIT_TIME));

            if (sprite.getX() < 0 - sprite.getSizeX()){
                groundObstacles.remove(sprite);
            }
        }

        for (Sprite sprite : flyingObstacles)
        {
            sprite.Move(UNIT_TIME);
            sprite.setRect(sprite.getAnimation().getCurrentFrame(UNIT_TIME));

            if (sprite.getX() < 0 - sprite.getSizeX()){
                flyingObstacles.remove(sprite);
            }
        }

    }

    public Sprite getRunner() {
        return runner;
    }

    private void updateRunner() {
        runner.Move(UNIT_TIME);

        if (runnerState == RunnerState.RUNNING){
            currentFrame = runner.getAnimation(RunnerState.RUNNING.ordinal()).getCurrentFrame(UNIT_TIME);
        }

        else if (runnerState == RunnerState.JUMPING){
            currentFrame = runner.getAnimation(RunnerState.JUMPING.ordinal()).getCurrentFrame(UNIT_TIME);
        }

        else if (runnerState == RunnerState.CROUCHING){
            currentFrame = runner.getAnimation(RunnerState.CROUCHING.ordinal()).getCurrentFrame(UNIT_TIME);
        }

        runner.setRect(currentFrame);

        if (runner.getX() <= START_X && isRunningBackwards){
            runner.setSpeedX(0);
            //runner.Move(time);
            isRunningBackwards = false;
        }

        if (runner.getX() >= END_X && isRunningForward){
            runner.setSpeedX(0);
            //runner.Move(time);
            isRunningForward = false;
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
                //runner.Move(time);
                isRunningBackwards = false;
            }

            else if (touchX > threshold + runnerWidths[RunnerState.RUNNING.ordinal()] && isRunningForward){

            }

            else if (touchX > threshold + runnerWidths[RunnerState.RUNNING.ordinal()] && !isRunningForward && !isRunningBackwards){
                runner.setSpeedX(RUNNER_SPEED);
                //runner.Move(time);
                isRunningForward = true;

            }

            if (touchX < threshold && isRunningBackwards){

            }

            else if (touchX < threshold && isRunningForward){
                runner.setSpeedX(0);
                //runner.Move(time);
                isRunningForward = false;

            }

            else if (touchX < threshold && !isRunningForward && !isRunningBackwards){
                runner.setSpeedX(-RUNNER_SPEED);
                //runner.Move(time);
                isRunningBackwards = true;

            }





            if (touchY < topline){
                runnerState = RunnerState.JUMPING;
                runner.getAnimation(RunnerState.JUMPING.ordinal()).resetAnimation();
                runner.setBitmapToRender(Assets.characterJumping);
                runner.setSizeX(runnerWidths[RunnerState.JUMPING.ordinal()]);
                runner.setSizeY(runnerHeights[RunnerState.JUMPING.ordinal()]);
                runner.setY(topline - runnerHeights[RunnerState.JUMPING.ordinal()] - JUMP_OFFSET );       //Corriendo pasamos a salto
                runner.setSpeedX(0);
                //runner.Move(time);
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
                //runner.Move(time);
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

    private void createObstacles () {

        poolGroundObstacles = new Sprite[POOL_OBSTACLES_SIZE];
        groundObstacles = new ArrayList<>();

        Sprite bob = new Sprite(Assets.bobObstacle, true, PARALLAX_WIDTH, this.baseline -
                Assets.playerHeight, -speedGroundObstacles,
                0, Assets.bobObstacleWidth, Assets.heightForGroundObstacles);

        Sprite chomp = new Sprite(Assets.chompObstacle, true, PARALLAX_WIDTH, this.baseline -
                Assets.playerHeight, -speedGroundObstacles,
                0, Assets.chompObstacleWidth, Assets.heightForGroundObstacles);

        Sprite goomba = new Sprite(Assets.goombaObstacle, true, PARALLAX_WIDTH, this.baseline -
                Assets.playerHeight, -speedGroundObstacles,
                0, Assets.goombaObstacleWidth, Assets.heightForGroundObstacles);

        Sprite planta = new Sprite(Assets.plantaObstacle, true, PARALLAX_WIDTH, this.baseline -
                Assets.playerHeight, -speedGroundObstacles,
                0, Assets.plantaObstacleWidth, Assets.heightForGroundObstacles);

        for (int i = 0; i < POOL_OBSTACLES_SIZE; i++){
            int enemy = (int) (Math.random()*((3-0)+1));     //random entre 0 y 3

            switch (enemy){
                case 0: poolGroundObstacles[i] = bob;
                    break;

                case 1: poolGroundObstacles[i] = chomp;
                    break;

                case 2: poolGroundObstacles[i] = goomba;
                    break;

                case 3: poolGroundObstacles[i] = planta;
                    break;

                default: //por aquí no puede entrar

            }
        }

        bob.addAnimation(new Animation(1, Assets.BOB_NUMBER_OF_FRAMES,Assets.BOB_FRAME_WIDTH,
                Assets.BOB_FRAME_HEIGHT, Assets.BOB_FRAME_WIDTH * Assets.BOB_NUMBER_OF_FRAMES, 10));

        chomp.addAnimation(new Animation(1, Assets.CHOMP_NUMBER_OF_FRAMES,Assets.CHOMP_FRAME_WIDTH,
                Assets.CHOMP_FRAME_HEIGHT, Assets.CHOMP_FRAME_WIDTH * Assets.CHOMP_NUMBER_OF_FRAMES, 10));

        goomba.addAnimation(new Animation(1, Assets.GOOMBA_NUMBER_OF_FRAMES,Assets.GOOMBA_FRAME_WIDTH,
                Assets.GOOMBA_FRAME_HEIGHT, Assets.GOOMBA_FRAME_WIDTH * Assets.GOOMBA_NUMBER_OF_FRAMES, 10));

        planta.addAnimation(new Animation(1, Assets.PLANTA_NUMBER_OF_FRAMES,Assets.PLANTA_FRAME_WIDTH,
                Assets.PLANTA_FRAME_HEIGHT, Assets.PLANTA_FRAME_WIDTH * Assets.PLANTA_NUMBER_OF_FRAMES, 10));

        poolGroundObstaclesIndex = 0;


        poolFlyingObstacles = new Sprite[POOL_OBSTACLES_SIZE];
        flyingObstacles = new ArrayList<>();

        Sprite goombaVolador = new Sprite(Assets.goombaVoladorObstacle, true, PARALLAX_WIDTH, topline - Assets.heightForFlyingObstacles, -speedFlyingObstacles,
                0, Assets.goombaObstacleWidth, Assets.heightForFlyingObstacles);

        Sprite lakitu = new Sprite(Assets.lakituObstacle, true, PARALLAX_WIDTH, topline - Assets.heightForFlyingObstacles, -speedFlyingObstacles,
                0, Assets.lakituObstacleWidth, Assets.heightForFlyingObstacles);

        for (int i = 0; i < POOL_OBSTACLES_SIZE; i++){
            int enemy = (int) (Math.random()*((1-0)+1));     //random entre 0 y 1

            switch (enemy){
                case 0: poolFlyingObstacles[i] = goombaVolador;
                    break;

                case 1: poolFlyingObstacles[i] = lakitu;
                    break;

                default: //por aquí no puede entrar

            }
        }

        goombaVolador.addAnimation(new Animation(1, Assets.GOOMBA_VOLADOR_NUMBER_OF_FRAMES,Assets.GOOMBA_VOLADOR_FRAME_WIDTH,
                Assets.GOOMBA_VOLADOR_FRAME_HEIGHT, Assets.GOOMBA_VOLADOR_FRAME_WIDTH * Assets.GOOMBA_VOLADOR_NUMBER_OF_FRAMES, 10));

        lakitu.addAnimation(new Animation(1, Assets.LAKITU_NUMBER_OF_FRAMES,Assets.LAKITU_FRAME_WIDTH,
                Assets.LAKITU_FRAME_HEIGHT, Assets.LAKITU_FRAME_WIDTH * Assets.LAKITU_NUMBER_OF_FRAMES, 10));

        poolFlyingObstaclesIndex = 0;

    }

    public List<Sprite> getGroundObstacles() {
        return groundObstacles;
    }

    public List<Sprite> getFlyingObstacles() {
        return flyingObstacles;
    }

    public List<Sprite> getDemises() {
        return demises;
    }

    private void createDemises () {
        poolFlyingDemise = new Sprite[POOL_OBSTACLES_SIZE];
        poolGroundDemise = new Sprite[POOL_OBSTACLES_SIZE];
        demises = new ArrayList<>();

        Sprite demiseGround = new Sprite(Assets.demiseGroundObstacle, false, PARALLAX_WIDTH, topline - Assets.heightForFlyingObstacles, 0,
                0, Assets.demiseGroundObstacleWidth, Assets.heightForFlyingObstacles);

        Sprite demiseFlying = new Sprite(Assets.demiseFlyingObstacle, false, PARALLAX_WIDTH, topline - Assets.heightForFlyingObstacles, 0,
                0, Assets.demiseFlyingObstacleWidth, Assets.heightForFlyingObstacles);

        for (int i = 0; i < POOL_OBSTACLES_SIZE; i++){

            poolGroundDemise[i] = demiseGround;

            poolFlyingDemise[i] = demiseFlying;
        }

        demiseGround.addAnimation(new Animation(1, Assets.DEMISE_GROUND_NUMBER_OF_FRAMES,Assets.DEMISE_GROUND_FRAME_WIDTH,
                Assets.DEMISE_GROUND_FRAME_HEIGHT, Assets.DEMISE_GROUND_FRAME_WIDTH * Assets.DEMISE_GROUND_NUMBER_OF_FRAMES, 10));

        demiseFlying.addAnimation(new Animation(1, Assets.DEMISE_FLYING_NUMBER_OF_FRAMES,Assets.DEMISE_FLYING_FRAME_WIDTH,
                Assets.DEMISE_FLYING_FRAME_HEIGHT, Assets.DEMISE_FLYING_FRAME_WIDTH * Assets.DEMISE_FLYING_NUMBER_OF_FRAMES, 10));

        poolGroundDemiseIndex = 0;
        poolFlyingDemiseIndex = 0;


    }

    private void activateGroundObstacle() {
        double r;
        timeSinceLastGroundObstacle += UNIT_TIME;
        if (timeSinceLastGroundObstacle >= TIME_BETWEEN_GROUND_OBSTACLES) {
            r = Math.random();
            if (r < PROB_ACTIVATION_GROUND_OBSTACLE) {
                // A ground obstacle is activated
                poolGroundObstacles[poolGroundObstaclesIndex].setX(PARALLAX_WIDTH);
                //la Y y la velocidad, ya están definidas al crear el obstáculo
                poolGroundObstacles[poolGroundObstaclesIndex].getAnimation().resetAnimation();
                groundObstacles.add(poolGroundObstacles[poolGroundObstaclesIndex]);

                if (TIME_BETWEEN_FLYING_OBSTACLES - timeSinceLastFlyingObstacle -
                        UNIT_TIME <= DELAY_OBSTACLE) {
                    timeSinceLastFlyingObstacle = TIME_BETWEEN_FLYING_OBSTACLES -
                            UNIT_TIME - DELAY_OBSTACLE;
                }

                poolGroundObstaclesIndex++;

                if (poolGroundObstaclesIndex >= POOL_OBSTACLES_SIZE){
                    poolGroundObstaclesIndex = 0;
                }

            }
            timeSinceLastGroundObstacle -= TIME_BETWEEN_GROUND_OBSTACLES;
        }
    }

    private void activateFlyingObjects() {
        double r;
        timeSinceLastFlyingObstacle += UNIT_TIME;
        if (timeSinceLastFlyingObstacle >= TIME_BETWEEN_FLYING_OBSTACLES) {
            r = Math.random();
            if (r < PROB_ACTIVATION_GROUND_OBSTACLE) {      //en este caso podemos mantener la misma constante porque es justo la mitad (50%)
                // A flying obstacle is activated
                poolFlyingObstacles[poolFlyingObstaclesIndex].setX(PARALLAX_WIDTH);
                //la Y y la velocidad, ya están definidas al crear el obstáculo
                poolFlyingObstacles[poolFlyingObstaclesIndex].getAnimation().resetAnimation();
                flyingObstacles.add(poolFlyingObstacles[poolFlyingObstaclesIndex]);

                if (TIME_BETWEEN_GROUND_OBSTACLES - timeSinceLastGroundObstacle -
                        UNIT_TIME <= DELAY_OBSTACLE) {
                    timeSinceLastGroundObstacle = TIME_BETWEEN_GROUND_OBSTACLES -
                            UNIT_TIME - DELAY_OBSTACLE;
                }

                poolFlyingObstaclesIndex++;

                if (poolFlyingObstaclesIndex >= POOL_OBSTACLES_SIZE){
                    poolFlyingObstaclesIndex = 0;
                }
            }
            timeSinceLastFlyingObstacle -= TIME_BETWEEN_FLYING_OBSTACLES;
        }
    }


}
