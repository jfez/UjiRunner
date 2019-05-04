package com.example.jorge.ujirunnerapp.testLevelsHUD;

import android.graphics.Rect;

import com.example.jorge.ujirunnerapp.Assets;
import com.example.jorge.ujirunnerapp.model.Animation;
import com.example.jorge.ujirunnerapp.model.Sprite;
import com.example.jorge.ujirunnerapp.model.TimedSprite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

enum RunnerState
{
    RUNNING, CROUCHING, JUMPING;
}

enum Level
{
    EASY, MEDIUM, HARD;
}

public class TestLevelsHudModel {

    public static final int STAGE_WIDTH = 480;
    public static final int STAGE_HEIGHT = 320;
    public static final int PARALLAX_WIDTH = 569;   //568,8888889
    public static final int PARALLAX_LAYERS = 5;
    public static final int START_X = STAGE_WIDTH / 8;
    public static final int END_X = (STAGE_WIDTH * 5) / 8;
    public static final int TOP_HUD = 15;
    public static final int COIN_X = 280;
    public static final int LIFE_MARGIN = 10;

    private static final float UNIT_TIME = 1f / 30;
    private static final int RUNNER_SPEED = 70;
    private static final int JUMP_OFFSET = 10;
    private static final int MARGIN = 50;
    private static final int MARGIN_BOTTOM = 10;
    private static final int MARGIN_TOP = 40;
    private static final int MARGIN_X = 40;

    public static final int POOL_OBSTACLES_SIZE = 12;
    public static final int POOL_COINS_SIZE = 5;
    /**public static final float PROBABILITY_BOB = 0.25f;
    public static final float PROBABILITY_CHOP = 0.25f;
    public static final float PROBABILITY_GOOMBA = 0.25f;
    public static final float PROBABILITY_PLANTA = 0.25f;*/
    //Queremos la misma probabilidad para todos los enemigos de suelo así que no emplearemos las probabilidasdes

    private static final float TIME_BETWEEN_GROUND_OBSTACLES = 6.0f;
    private static final float TIME_BETWEEN_FLYING_OBSTACLES = 6.0f;
    private static final float TIME_BETWEEN_COINS = 5.0f;
    private static final double PROB_ACTIVATION_GROUND_OBSTACLE = 0.7;
    private static final double PROB_ACTIVATION_FLYING_OBSTACLE = 0.7;

    private static final float TIME_BETWEEN_GROUND_OBSTACLES2 = 3.0f;
    private static final float TIME_BETWEEN_FLYING_OBSTACLES2 = 3.0f;
    private static final double PROB_ACTIVATION_GROUND_OBSTACLE2 = 0.8;
    private static final double PROB_ACTIVATION_FLYING_OBSTACLE2 = 0.8;
    private static final double PROB_ACTIVATION_SPECIAL_ENEMY = 0.4;
    private static final float DELAY_OBSTACLE2 = 2.0f;

    private static final double PROB_ACTIVATION_COIN = 0.5;
    private static final float DELAY_OBSTACLE = 5.0f;

    private static final float DELAY_COIN = 2.0f;   //5.0f
    private static final float TIME_BETWEEN_ADD = 2.0f;

    public static final int MAXLIFE = 100;


    private float timeSinceLastGroundObstacle;
    private float timeSinceLastFlyingObstacle;
    private float timeElapsedRunning;
    private float timeSinceLastCoin;


    private Sprite[] poolGroundObstacles;
    private int poolGroundObstaclesIndex;
    private List<Sprite> groundObstacles;

    private Sprite[] poolFlyingObstacles;
    private int poolFlyingObstaclesIndex;
    private List<Sprite> flyingObstacles;

    private Sprite[] poolBeatles;
    private int poolBeatlesIndex;
    private List<Sprite> beatles;

    private Sprite[] poolBoos;
    private int poolBoosIndex;
    private List<Sprite> boos;

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

    private int currentLife;
    private int metresTravelled;
    private int metresForLife;
    private int coinsCollected;

    private Sprite coin;
    private Sprite heart;
    private Sprite lifeContainer;

    private TimedSprite[] poolCoins;
    private int poolCoinsIndex;
    private List<Sprite> coins;

    private int previousEnemy;

    private boolean check1;
    private boolean check2;
    private boolean check3;

    private Level level;

    public Sprite getCoin() {
        return coin;
    }

    public List<Sprite> getCoins() {
        return coins;
    }

    public Sprite getHeart() {
        return heart;
    }

    public Sprite getLifeContainer() {
        return lifeContainer;
    }

    public int getCurrentLife() {
        return currentLife;
    }

    public int getMetresTravelled() {
        return metresTravelled;
    }

    public int getCoinsCollected() {
        return coinsCollected;
    }

    public TestLevelsHudModel(int playerWidth, int baseline, int topline, int threshold) {
        this.playerWidth = playerWidth;
        this.baseline = baseline;
        this.topline = topline;
        this.threshold = threshold;
        tickTime = 0;
        isRunningForward = false;
        isRunningBackwards = false;

        check1 = true;
        check2 = true;
        check3 = true;


        //squareX = STAGE_WIDTH / 5;
        //squareY = baseline - this.playerWidth;

        speed = 60;
        speedGroundObstacles = 100;
        speedFlyingObstacles = 110;
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
                runnerHeights[RunnerState.JUMPING.ordinal()], runnerWidths[RunnerState.JUMPING.ordinal()] * Assets.CHARACTER_JUMP_NUMBER_OF_FRAMES, 2));

        createObstacles();
        createDemises();
        createCoins();

        timeSinceLastGroundObstacle = TIME_BETWEEN_GROUND_OBSTACLES;
        timeSinceLastFlyingObstacle = TIME_BETWEEN_FLYING_OBSTACLES;
        timeElapsedRunning = 0;
        timeSinceLastCoin = 0;

        currentLife = MAXLIFE;
        metresTravelled = 0;
        metresForLife = 0;
        coinsCollected = 0;
        level = Level.EASY;

        coin = new Sprite(Assets.coin, false, COIN_X, TOP_HUD, 0, 0, Assets.coinHudWidth, Assets.hudHeight);
        heart = new Sprite(Assets.heart, false, START_X - MARGIN_X, TOP_HUD, 0, 0, Assets.heartHudWidth, Assets.hudHeight);
        lifeContainer = new Sprite(Assets.lifeContainer, false, START_X - MARGIN_X/2 , TOP_HUD - LIFE_MARGIN, 0, 0, Assets.lifecontainerHudWidth, Assets.hudHeight);

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
            //dependiendo de la dificultad (metros recorridos) podemos llamar a una función diferente para que haya mayor probabilidad de aparecer para los bichos y para que EXISTA la posibilidad de que aparezcan los bichos complicados

            if (level == Level.EASY){
                activateGroundObstacle();
                activateFlyingObjects();
            }

            else if (level == Level.MEDIUM){
                activateGroundObstacle2();
                activateFlyingObjects();
            }

            else if (level == Level.HARD){
                activateGroundObstacle2();
                activateFlyingObjects2();
            }

            activateCoins();
            addMetres();
            updateObstacles();
            updateRunner();
            checkCollisions();
            updateDemises();
            updateLevel();
            recoverLife();

        }
    }



    private void updateLevel() {
        if (metresTravelled > 600 && level == Level.EASY){  //600
            level = Level.MEDIUM;
        }

        if (metresTravelled > 1500 && level == Level.MEDIUM){    //1500
            level = Level.HARD;
        }
    }

    private void recoverLife() {
        if (level == Level.EASY){
            if (metresForLife > 250){
                currentLife = currentLife + 50;
                if (currentLife > 100){
                    currentLife = 100;
                }
                metresForLife = 0;
            }

        }

        else if (level == Level.MEDIUM){
            if (metresForLife > 350){
                currentLife = currentLife + 40;
                if (currentLife > 100){
                    currentLife = 100;
                }
                metresForLife = 0;
            }

        }

        else if (level == Level.HARD){
            if (metresForLife > 500){
                currentLife = currentLife + 30;
                if (currentLife > 100){
                    currentLife = 100;
                }
                metresForLife = 0;
            }

        }

    }


    private void addMetres() {
        timeElapsedRunning += UNIT_TIME;
        if (timeElapsedRunning >= TIME_BETWEEN_ADD) {
            metresTravelled = metresTravelled + 5;
            metresForLife = metresForLife + 5;
            timeElapsedRunning = 0;

        }
    }

    private void updateDemises() {
        /*for (Sprite sprite : demises)
        {
            sprite.setRect(sprite.getAnimation().getCurrentFrame(UNIT_TIME));

            if (sprite.getAnimation().hasRun()){
                synchronized (demises){
                    demises.remove(sprite);
                }


            }

        }*/

        /*for (int i = 0; i < demises.size(); i++){
            demises.get(i).setRect(demises.get(i).getAnimation().getCurrentFrame(UNIT_TIME));

            if (demises.get(i).getAnimation().hasRun()) {
                synchronized (demises) {
                    demises.remove(demises.get(i));
                }
            }

        }*/

        Iterator<Sprite> iterator = demises.iterator();
        while(iterator.hasNext()){
            Sprite sprite = iterator.next();

            sprite.setRect(sprite.getAnimation().getCurrentFrame(UNIT_TIME));
            if (sprite.getAnimation().hasRun()){
                synchronized (demises){
                    iterator.remove();
                }
            }

        }


    }

    public List<Sprite> getBeatles() {
        return beatles;
    }

    public List<Sprite> getBoos() {
        return boos;
    }

    private void checkCollisions() {

        /*synchronized (groundObstacles){
            for (Sprite sprite : groundObstacles)
            {
                if (runner.overlapBoundingBoxes(sprite)){
                    // A ground demise is activated
                    poolGroundDemise[poolGroundDemiseIndex].setX(runner.getX() + MARGIN);
                    //la Y y la velocidad, ya están definidas al crear el obstáculo
                    poolGroundDemise[poolGroundDemiseIndex].getAnimation().resetAnimation();
                    synchronized (demises){
                        demises.add(poolGroundDemise[poolGroundDemiseIndex]);
                    }

                    synchronized (groundObstacles){
                        groundObstacles.remove(sprite);
                    }


                    poolGroundDemiseIndex++;

                    if (poolGroundDemiseIndex >= POOL_OBSTACLES_SIZE){
                        poolGroundDemiseIndex = 0;
                    }
                }
            }
        }


        synchronized (flyingObstacles){
            for (Sprite sprite : flyingObstacles)
            {
                if (runner.overlapBoundingBoxes(sprite)){
                    // A flying demise is activated
                    poolFlyingDemise[poolFlyingDemiseIndex].setX(runner.getX() + MARGIN);
                    //la Y y la velocidad, ya están definidas al crear el obstáculo
                    poolFlyingDemise[poolFlyingDemiseIndex].getAnimation().resetAnimation();
                    synchronized (demises){
                        demises.add(poolFlyingDemise[poolFlyingDemiseIndex]);
                    }

                    synchronized (flyingObstacles){
                        flyingObstacles.remove(sprite);
                    }


                    poolFlyingDemiseIndex++;

                    if (poolFlyingDemiseIndex >= POOL_OBSTACLES_SIZE){
                        poolFlyingDemiseIndex = 0;
                    }
                }
            }
        }*/


        Iterator<Sprite> iterator = groundObstacles.iterator();
        while(iterator.hasNext()){
            Sprite sprite = iterator.next();

            if (runner.overlapBoundingBoxes(sprite)){
                // A ground demise is activated
                poolGroundDemise[poolGroundDemiseIndex].setX(runner.getX() + MARGIN);
                //la Y y la velocidad, ya están definidas al crear el obstáculo
                poolGroundDemise[poolGroundDemiseIndex].getAnimation().resetAnimation();
                synchronized (demises){
                    demises.add(poolGroundDemise[poolGroundDemiseIndex]);
                }

                synchronized (groundObstacles){
                    iterator.remove();
                }

                if (currentLife > 0){
                    currentLife = currentLife - 10;
                }

                if (currentLife < 0){
                    currentLife = 0;
                }


                poolGroundDemiseIndex++;

                if (poolGroundDemiseIndex >= POOL_OBSTACLES_SIZE){
                    poolGroundDemiseIndex = 0;
                }
            }

        }

        iterator = flyingObstacles.iterator();
        while(iterator.hasNext()){
            Sprite sprite = iterator.next();

            if (runner.overlapBoundingBoxes(sprite)){
                // A flying demise is activated
                poolFlyingDemise[poolFlyingDemiseIndex].setX(runner.getX() + MARGIN);
                //la Y y la velocidad, ya están definidas al crear el obstáculo
                poolFlyingDemise[poolFlyingDemiseIndex].getAnimation().resetAnimation();
                synchronized (demises){
                    demises.add(poolFlyingDemise[poolFlyingDemiseIndex]);
                }

                synchronized (flyingObstacles){
                    iterator.remove();
                }

                if (currentLife > 0){
                    currentLife = currentLife - 20;
                }

                if (currentLife < 0){
                    currentLife = 0;
                }


                poolFlyingDemiseIndex++;

                if (poolFlyingDemiseIndex >= POOL_OBSTACLES_SIZE){
                    poolFlyingDemiseIndex = 0;
                }
            }

        }

        if (level == Level.MEDIUM){
            iterator = beatles.iterator();
            while(iterator.hasNext()){
                Sprite sprite = iterator.next();

                if (runner.overlapBoundingBoxes(sprite)){
                    // A ground demise is activated
                    poolGroundDemise[poolGroundDemiseIndex].setX(runner.getX() + MARGIN);
                    //la Y y la velocidad, ya están definidas al crear el obstáculo
                    poolGroundDemise[poolGroundDemiseIndex].getAnimation().resetAnimation();
                    synchronized (demises){
                        demises.add(poolGroundDemise[poolGroundDemiseIndex]);
                    }

                    synchronized (beatles){
                        iterator.remove();
                    }

                    if (currentLife > 0){
                        currentLife = currentLife - 25;
                    }

                    if (currentLife < 0){
                        currentLife = 0;
                    }


                    poolGroundDemiseIndex++;

                    if (poolGroundDemiseIndex >= POOL_OBSTACLES_SIZE){
                        poolGroundDemiseIndex = 0;
                    }
                }

            }
        }

        if (level == Level.HARD){
            iterator = beatles.iterator();
            while(iterator.hasNext()){
                Sprite sprite = iterator.next();

                if (runner.overlapBoundingBoxes(sprite)){
                    // A ground demise is activated
                    poolGroundDemise[poolGroundDemiseIndex].setX(runner.getX() + MARGIN);
                    //la Y y la velocidad, ya están definidas al crear el obstáculo
                    poolGroundDemise[poolGroundDemiseIndex].getAnimation().resetAnimation();
                    synchronized (demises){
                        demises.add(poolGroundDemise[poolGroundDemiseIndex]);
                    }

                    synchronized (beatles){
                        iterator.remove();
                    }

                    if (currentLife > 0){
                        currentLife = currentLife - 25;
                    }

                    if (currentLife < 0){
                        currentLife = 0;
                    }


                    poolGroundDemiseIndex++;

                    if (poolGroundDemiseIndex >= POOL_OBSTACLES_SIZE){
                        poolGroundDemiseIndex = 0;
                    }
                }

            }

            iterator = boos.iterator();
            while(iterator.hasNext()){
                Sprite sprite = iterator.next();

                if (runner.overlapBoundingBoxes(sprite)){
                    // A flying demise is activated
                    poolFlyingDemise[poolFlyingDemiseIndex].setX(runner.getX() + MARGIN);
                    //la Y y la velocidad, ya están definidas al crear el obstáculo
                    poolFlyingDemise[poolFlyingDemiseIndex].getAnimation().resetAnimation();
                    synchronized (demises){
                        demises.add(poolFlyingDemise[poolFlyingDemiseIndex]);
                    }

                    synchronized (boos){
                        iterator.remove();
                    }

                    if (currentLife > 0){
                        currentLife = currentLife - 40;
                    }

                    if (currentLife < 0){
                        currentLife = 0;
                    }


                    poolFlyingDemiseIndex++;

                    if (poolFlyingDemiseIndex >= POOL_OBSTACLES_SIZE){
                        poolFlyingDemiseIndex = 0;
                    }
                }

            }
        }


        iterator = coins.iterator();
        while(iterator.hasNext()){
            Sprite sprite = iterator.next();

            if (runner.overlapBoundingBoxes(sprite)){

                coinsCollected++;
                check3 = true;

                synchronized (coins){
                    iterator.remove();
                }

            }

        }







    }

    private void updateObstacles() {
        /*synchronized (groundObstacles){
            for (Sprite sprite : groundObstacles)
            {
                sprite.Move(UNIT_TIME);
                sprite.setRect(sprite.getAnimation().getCurrentFrame(UNIT_TIME));

                if (sprite.getX() < 0 - sprite.getSizeX()){
                    synchronized (groundObstacles){
                        groundObstacles.remove(sprite);
                    }

                }
            }
        }

        synchronized (flyingObstacles){
            for (Sprite sprite : flyingObstacles)
            {
                sprite.Move(UNIT_TIME);
                sprite.setRect(sprite.getAnimation().getCurrentFrame(UNIT_TIME));

                if (sprite.getX() < 0 - sprite.getSizeX()){
                    synchronized (flyingObstacles){
                        flyingObstacles.remove(sprite);
                    }

                }
            }
        }*/

        Iterator<Sprite> iterator = groundObstacles.iterator();
        while(iterator.hasNext()){
            Sprite sprite = iterator.next();

            sprite.Move(UNIT_TIME);
            sprite.setRect(sprite.getAnimation().getCurrentFrame(UNIT_TIME));

            if (sprite.getX() < 0 - sprite.getSizeX()){
                synchronized (groundObstacles){
                    iterator.remove();
                }

            }

        }

        iterator = flyingObstacles.iterator();
        while(iterator.hasNext()){
            Sprite sprite = iterator.next();

            sprite.Move(UNIT_TIME);
            sprite.setRect(sprite.getAnimation().getCurrentFrame(UNIT_TIME));

            if (sprite.getX() < 0 - sprite.getSizeX()){
                synchronized (flyingObstacles){
                    iterator.remove();
                }

            }

        }

        if (level == Level.MEDIUM){
            iterator = beatles.iterator();
            while(iterator.hasNext()){
                Sprite sprite = iterator.next();

                sprite.Move(UNIT_TIME);
                sprite.setRect(sprite.getAnimation().getCurrentFrame(UNIT_TIME));

                if (sprite.getX() < 0 - sprite.getSizeX()){
                    synchronized (beatles){
                        iterator.remove();
                    }

                }

            }
        }

        else if (level == Level.HARD){
            iterator = beatles.iterator();
            while(iterator.hasNext()){
                Sprite sprite = iterator.next();

                sprite.Move(UNIT_TIME);
                sprite.setRect(sprite.getAnimation().getCurrentFrame(UNIT_TIME));

                if (sprite.getX() < 0 - sprite.getSizeX()){
                    synchronized (beatles){
                        iterator.remove();
                    }

                }

            }

            iterator = boos.iterator();
            while(iterator.hasNext()){
                Sprite sprite = iterator.next();

                sprite.Move(UNIT_TIME);
                sprite.setRect(sprite.getAnimation().getCurrentFrame(UNIT_TIME));

                if (sprite.getX() < 0 - sprite.getSizeX()){
                    synchronized (boos){
                        iterator.remove();
                    }

                }

            }
        }

        iterator = coins.iterator();
        while(iterator.hasNext()){
            Sprite sprite = iterator.next();

            sprite.Move(UNIT_TIME);
            sprite.setRect(sprite.getAnimation().getCurrentFrame(UNIT_TIME));

            if (sprite.getX() < 0 - sprite.getSizeX()){
                synchronized (coins){
                    iterator.remove();
                }

            }

            if(((TimedSprite)sprite).timedOut()){
                synchronized (coins){
                    iterator.remove();
                }
            }

            ((TimedSprite)sprite).updateTimer(UNIT_TIME);       //actualizar el timer de cada moneda

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
                runner.setY(baseline - runnerHeights[RunnerState.CROUCHING.ordinal()] + MARGIN_TOP);       //Corriendo pasamos a agachar --> OJO
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

        previousEnemy = -1;

        Sprite bob = new Sprite(Assets.bobObstacle, true, PARALLAX_WIDTH, this.baseline -
                Assets.heightForGroundObstacles, -speedGroundObstacles,
                0, Assets.bobObstacleWidth, Assets.heightForGroundObstacles);

        Sprite chomp = new Sprite(Assets.chompObstacle, true, PARALLAX_WIDTH, this.baseline -
                Assets.heightForGroundObstacles, -speedGroundObstacles,
                0, Assets.chompObstacleWidth, Assets.heightForGroundObstacles);

        Sprite goomba = new Sprite(Assets.goombaObstacle, true, PARALLAX_WIDTH, this.baseline -
                Assets.heightForGroundObstacles, -speedGroundObstacles,
                0, Assets.goombaObstacleWidth, Assets.heightForGroundObstacles);

        Sprite planta = new Sprite(Assets.plantaObstacle, true, PARALLAX_WIDTH, this.baseline -
                Assets.heightForGroundObstacles, -speedGroundObstacles,
                0, Assets.plantaObstacleWidth, Assets.heightForGroundObstacles);

        for (int i = 0; i < POOL_OBSTACLES_SIZE; i++){
            int enemy = (int) (Math.random()*((3-0)+1));     //random entre 0 y 3

            while (enemy == previousEnemy){
                enemy = (int) (Math.random()*((3-0)+1));
            }

            previousEnemy = enemy;

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

        bob.addAnimation(new Animation(1, Assets.BOB_NUMBER_OF_FRAMES, Assets.bobObstacle.getWidth() / Assets.BOB_NUMBER_OF_FRAMES,
                Assets.bobObstacle.getHeight(), Assets.bobObstacle.getWidth(), 10));

        chomp.addAnimation(new Animation(1, Assets.CHOMP_NUMBER_OF_FRAMES,Assets.chompObstacle.getWidth()/ Assets.CHOMP_NUMBER_OF_FRAMES,
                Assets.chompObstacle.getHeight(), Assets.chompObstacle.getWidth(), 4));

        goomba.addAnimation(new Animation(1, Assets.GOOMBA_NUMBER_OF_FRAMES, Assets.goombaObstacle.getWidth()/Assets.GOOMBA_NUMBER_OF_FRAMES,
                Assets.goombaObstacle.getHeight(), Assets.goombaObstacle.getWidth(), 10));

        planta.addAnimation(new Animation(1, Assets.PLANTA_NUMBER_OF_FRAMES, Assets.plantaObstacle.getWidth() / Assets.PLANTA_NUMBER_OF_FRAMES,
                Assets.plantaObstacle.getHeight(), Assets.plantaObstacle.getWidth(), 4));

        poolGroundObstaclesIndex = 0;


        poolFlyingObstacles = new Sprite[POOL_OBSTACLES_SIZE];
        flyingObstacles = new ArrayList<>();

        previousEnemy = -1;

        Sprite goombaVolador = new Sprite(Assets.goombaVoladorObstacle, true, PARALLAX_WIDTH, topline - MARGIN_TOP , -speedFlyingObstacles,
                0, Assets.goombaObstacleWidth, Assets.heightForFlyingObstacles);

        Sprite lakitu = new Sprite(Assets.lakituObstacle, true, PARALLAX_WIDTH, topline -MARGIN_TOP, -speedFlyingObstacles,
                0, Assets.lakituObstacleWidth, Assets.heightForFlyingObstacles);

        for (int i = 0; i < POOL_OBSTACLES_SIZE; i++){
            int enemy = (int) (Math.random()*((1-0)+1));     //random entre 0 y 1

            while (enemy == previousEnemy){
                enemy = (int) (Math.random()*((1-0)+1));
            }

            previousEnemy = enemy;

            switch (enemy){
                case 0: poolFlyingObstacles[i] = goombaVolador;
                    break;

                case 1: poolFlyingObstacles[i] = lakitu;
                    break;

                default: //por aquí no puede entrar

            }
        }

        goombaVolador.addAnimation(new Animation(1, Assets.GOOMBA_VOLADOR_NUMBER_OF_FRAMES,Assets.goombaVoladorObstacle.getWidth() / Assets.GOOMBA_VOLADOR_NUMBER_OF_FRAMES,
                Assets.goombaVoladorObstacle.getHeight(), Assets.goombaVoladorObstacle.getWidth(), 4));

        lakitu.addAnimation(new Animation(1, Assets.LAKITU_NUMBER_OF_FRAMES, Assets.lakituObstacle.getWidth() / Assets.LAKITU_NUMBER_OF_FRAMES,
                Assets.lakituObstacle.getHeight(), Assets.lakituObstacle.getWidth(), 8));

        poolFlyingObstaclesIndex = 0;



        poolBeatles = new Sprite[POOL_OBSTACLES_SIZE];
        beatles = new ArrayList<>();

        Sprite beatleSprite = new Sprite(Assets.beatleObstacle, true, PARALLAX_WIDTH, this.baseline -
                Assets.heightForGroundObstacles + MARGIN_BOTTOM , -speedGroundObstacles,
                0, Assets.bobObstacleWidth, Assets.heightForGroundObstacles);

        for (int i = 0; i < POOL_OBSTACLES_SIZE; i++){

            poolBeatles[i] = beatleSprite;


        }

        beatleSprite.addAnimation(new Animation(1, Assets.BEATLE_NUMBER_OF_FRAMES, Assets.beatleObstacle.getWidth() / Assets.BEATLE_NUMBER_OF_FRAMES,
                Assets.beatleObstacle.getHeight(), Assets.beatleObstacle.getWidth(), 10));

        poolBeatlesIndex = 0;


        poolBoos = new Sprite[POOL_OBSTACLES_SIZE];
        boos = new ArrayList<>();

        Sprite booSprite = new Sprite(Assets.booObstacle, true, PARALLAX_WIDTH, topline -MARGIN_TOP, -speedFlyingObstacles,
                0, Assets.booObstacleWidth, Assets.heightForFlyingObstacles);

        for (int i = 0; i < POOL_OBSTACLES_SIZE; i++){

            poolBoos[i] = booSprite;


        }

        booSprite.addAnimation(new Animation(1, Assets.BOO_NUMBER_OF_FRAMES, Assets.booObstacle.getWidth() / Assets.BOO_NUMBER_OF_FRAMES,
                Assets.booObstacle.getHeight(), Assets.booObstacle.getWidth(), 10));

        poolBoosIndex = 0;

    }

    private void createCoins() {
        poolCoins = new TimedSprite[POOL_COINS_SIZE];

        coins = new ArrayList<>();


        int random = (int )(Math.random() * 7 + 3);

        TimedSprite coinGame = new TimedSprite(Assets.coins, false, PARALLAX_WIDTH, this.baseline -
                Assets.coinsHeight - MARGIN_BOTTOM, -speedGroundObstacles,
                0, Assets.coinsWidth, Assets.coinsHeight, random);



        for (int i = 0; i < POOL_COINS_SIZE; i++){

            poolCoins[i] = coinGame;


        }

        coinGame.addAnimation(new Animation(1, Assets.COINS_NUMBER_OF_FRAMES, Assets.coins.getWidth() / Assets.COINS_NUMBER_OF_FRAMES,
                Assets.coins.getHeight(), Assets.coins.getWidth(), 10));



        poolCoinsIndex = 0;

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

        Sprite demiseGround = new Sprite(Assets.demiseGroundObstacle, false, PARALLAX_WIDTH, this.baseline -
                Assets.heightForGroundObstacles - MARGIN_BOTTOM, 0,
                0, Assets.demiseGroundObstacleWidth, Assets.heightForFlyingObstacles);

        Sprite demiseFlying = new Sprite(Assets.demiseFlyingObstacle, false, PARALLAX_WIDTH, topline - MARGIN_TOP, 0,
                0, Assets.demiseFlyingObstacleWidth, Assets.heightForFlyingObstacles);

        for (int i = 0; i < POOL_OBSTACLES_SIZE; i++){

            poolGroundDemise[i] = demiseGround;

            poolFlyingDemise[i] = demiseFlying;
        }

        demiseGround.addAnimation(new Animation(1, Assets.DEMISE_GROUND_NUMBER_OF_FRAMES, Assets.demiseGroundObstacle.getWidth() / Assets.DEMISE_GROUND_NUMBER_OF_FRAMES,
                Assets.demiseGroundObstacle.getHeight(), Assets.demiseGroundObstacle.getWidth(), 10));

        demiseFlying.addAnimation(new Animation(1, Assets.DEMISE_FLYING_NUMBER_OF_FRAMES, Assets.demiseFlyingObstacle.getWidth() / Assets.DEMISE_FLYING_NUMBER_OF_FRAMES,
                Assets.demiseFlyingObstacle.getHeight(), Assets.demiseFlyingObstacle.getWidth(), 10));

        poolGroundDemiseIndex = 0;
        poolFlyingDemiseIndex = 0;


    }

    private void activateGroundObstacle() {
        double r;
        timeSinceLastGroundObstacle += UNIT_TIME;
        if (timeSinceLastGroundObstacle >= TIME_BETWEEN_GROUND_OBSTACLES && timeSinceLastCoin >= DELAY_COIN) {
            r = Math.random();
            if (r < PROB_ACTIVATION_GROUND_OBSTACLE) {
                // A ground obstacle is activated
                poolGroundObstacles[poolGroundObstaclesIndex].setX(PARALLAX_WIDTH);
                //la Y y la velocidad, ya están definidas al crear el obstáculo
                poolGroundObstacles[poolGroundObstaclesIndex].getAnimation().resetAnimation();
                synchronized (groundObstacles){
                    groundObstacles.add(poolGroundObstacles[poolGroundObstaclesIndex]);
                }


                if (TIME_BETWEEN_FLYING_OBSTACLES - timeSinceLastFlyingObstacle -
                        UNIT_TIME <= DELAY_OBSTACLE) {
                    timeSinceLastFlyingObstacle = TIME_BETWEEN_FLYING_OBSTACLES - UNIT_TIME - DELAY_OBSTACLE;
                    //timeSinceLastFlyingObstacle = 0;
                }

                if (TIME_BETWEEN_COINS - timeSinceLastCoin -
                        UNIT_TIME <= DELAY_COIN) {
                    timeSinceLastCoin = TIME_BETWEEN_COINS -
                            UNIT_TIME - DELAY_COIN;
                }

                poolGroundObstaclesIndex++;

                if (poolGroundObstaclesIndex >= POOL_OBSTACLES_SIZE){
                    poolGroundObstaclesIndex = 0;
                }

            }
            timeSinceLastGroundObstacle = 0;
        }
    }

    private void activateFlyingObjects() {
        double r;
        timeSinceLastFlyingObstacle += UNIT_TIME;
        if (timeSinceLastFlyingObstacle >= TIME_BETWEEN_FLYING_OBSTACLES) {
            r = Math.random();
            if (r < PROB_ACTIVATION_FLYING_OBSTACLE) {      //
                // A flying obstacle is activated
                poolFlyingObstacles[poolFlyingObstaclesIndex].setX(PARALLAX_WIDTH);
                //la Y y la velocidad, ya están definidas al crear el obstáculo
                poolFlyingObstacles[poolFlyingObstaclesIndex].getAnimation().resetAnimation();
                synchronized (flyingObstacles){
                    flyingObstacles.add(poolFlyingObstacles[poolFlyingObstaclesIndex]);
                }


                if (TIME_BETWEEN_GROUND_OBSTACLES - timeSinceLastGroundObstacle -
                        UNIT_TIME <= DELAY_OBSTACLE) {
                    timeSinceLastGroundObstacle = TIME_BETWEEN_GROUND_OBSTACLES - UNIT_TIME - DELAY_OBSTACLE;
                    //timeSinceLastGroundObstacle = 0;
                }

                poolFlyingObstaclesIndex++;

                if (poolFlyingObstaclesIndex >= POOL_OBSTACLES_SIZE){
                    poolFlyingObstaclesIndex = 0;
                }
            }
            timeSinceLastFlyingObstacle = 0;
        }
    }

    private void activateGroundObstacle2() {
        double r;
        double r2;
        timeSinceLastGroundObstacle += UNIT_TIME;
        if (timeSinceLastGroundObstacle >= TIME_BETWEEN_GROUND_OBSTACLES2 && timeSinceLastCoin >= DELAY_COIN) {
            r = Math.random();
            if (r < PROB_ACTIVATION_GROUND_OBSTACLE2) {
                r2 = Math.random();
                if (r2 < PROB_ACTIVATION_SPECIAL_ENEMY && check1){
                    // A special enemy ground is activated
                    poolBeatles[poolBeatlesIndex].setX(PARALLAX_WIDTH);
                    //la Y y la velocidad, ya están definidas al crear el obstáculo
                    poolBeatles[poolBeatlesIndex].getAnimation().resetAnimation();
                    synchronized (beatles){
                        beatles.add(poolBeatles[poolBeatlesIndex]);
                    }

                    check1 = false;
                }

                else {
                    // A ground obstacle is activated
                    poolGroundObstacles[poolGroundObstaclesIndex].setX(PARALLAX_WIDTH);
                    //la Y y la velocidad, ya están definidas al crear el obstáculo
                    poolGroundObstacles[poolGroundObstaclesIndex].getAnimation().resetAnimation();
                    synchronized (groundObstacles){
                        groundObstacles.add(poolGroundObstacles[poolGroundObstaclesIndex]);
                    }

                    check1 = true;
                }



                if (TIME_BETWEEN_FLYING_OBSTACLES2 - timeSinceLastFlyingObstacle -
                        UNIT_TIME <= DELAY_OBSTACLE2) {
                    timeSinceLastFlyingObstacle = TIME_BETWEEN_FLYING_OBSTACLES2 - UNIT_TIME - DELAY_OBSTACLE2;
                    //timeSinceLastFlyingObstacle = 0;
                }

                if (TIME_BETWEEN_COINS - timeSinceLastCoin -
                        UNIT_TIME <= DELAY_COIN) {
                    timeSinceLastCoin = TIME_BETWEEN_COINS -
                            UNIT_TIME - DELAY_COIN;
                }

                poolGroundObstaclesIndex++;

                if (poolGroundObstaclesIndex >= POOL_OBSTACLES_SIZE){
                    poolGroundObstaclesIndex = 0;
                }

            }
            timeSinceLastGroundObstacle = 0;
        }

    }

    private void activateFlyingObjects2() {
        double r;
        double r2;
        timeSinceLastFlyingObstacle += UNIT_TIME;
        if (timeSinceLastFlyingObstacle >= TIME_BETWEEN_FLYING_OBSTACLES2) {
            r = Math.random();
            if (r < PROB_ACTIVATION_FLYING_OBSTACLE2) {
                r2 = Math.random();
                if (r2 < PROB_ACTIVATION_SPECIAL_ENEMY && check2){
                    // A flying especial enemy is activated
                    poolBoos[poolBoosIndex].setX(PARALLAX_WIDTH);
                    //la Y y la velocidad, ya están definidas al crear el obstáculo
                    poolBoos[poolBoosIndex].getAnimation().resetAnimation();
                    synchronized (boos){
                        boos.add(poolBoos[poolBoosIndex]);
                    }

                    check2 = false;
                }

                else {
                    // A flying obstacle is activated
                    poolFlyingObstacles[poolFlyingObstaclesIndex].setX(PARALLAX_WIDTH);
                    //la Y y la velocidad, ya están definidas al crear el obstáculo
                    poolFlyingObstacles[poolFlyingObstaclesIndex].getAnimation().resetAnimation();
                    synchronized (flyingObstacles){
                        flyingObstacles.add(poolFlyingObstacles[poolFlyingObstaclesIndex]);
                    }

                    check2 = true;
                }



                if (TIME_BETWEEN_GROUND_OBSTACLES2 - timeSinceLastGroundObstacle -
                        UNIT_TIME <= DELAY_OBSTACLE2) {
                    timeSinceLastGroundObstacle = TIME_BETWEEN_GROUND_OBSTACLES2 - UNIT_TIME - DELAY_OBSTACLE2;
                    //timeSinceLastGroundObstacle = 0;
                }

                poolFlyingObstaclesIndex++;

                if (poolFlyingObstaclesIndex >= POOL_OBSTACLES_SIZE){
                    poolFlyingObstaclesIndex = 0;
                }
            }
            timeSinceLastFlyingObstacle = 0;
        }

    }




    private void activateCoins() {
        double r;
        int random;
        timeSinceLastCoin += UNIT_TIME;

        if (!check3 && timeSinceLastCoin >= TIME_BETWEEN_COINS + 4){
            check3 = true;
        }

        if (timeSinceLastCoin >= TIME_BETWEEN_COINS && timeSinceLastGroundObstacle >= DELAY_COIN && check3) {
            r = Math.random();
            if (r < PROB_ACTIVATION_COIN) {
                check3 = false;
                // A coin is activated
                poolCoins[poolCoinsIndex].setX(PARALLAX_WIDTH);
                //la Y y la velocidad, ya están definidas al crear el obstáculo
                poolCoins[poolCoinsIndex].getAnimation().resetAnimation();

                if (level == Level.EASY){
                    random = (int) (Math.random() * 7 + 3);
                }

                else if (level ==Level.MEDIUM){
                    random = (int )(Math.random() * 6 + 3);
                }

                else {
                    random = (int )(Math.random() * 5 + 3);
                }

                poolCoins[poolCoinsIndex].setTimer(random);
                synchronized (coins){
                    coins.add(poolCoins[poolCoinsIndex]);
                }


                if (TIME_BETWEEN_GROUND_OBSTACLES - timeSinceLastGroundObstacle -
                        UNIT_TIME <= DELAY_COIN) {
                    timeSinceLastGroundObstacle = TIME_BETWEEN_GROUND_OBSTACLES -
                            UNIT_TIME - DELAY_COIN;
                }

                poolCoinsIndex++;

                if (poolCoinsIndex >= POOL_COINS_SIZE){
                    poolCoinsIndex = 0;
                }
            }
            timeSinceLastCoin -= TIME_BETWEEN_COINS;
        }
    }

    public boolean isLevelEasy () {
        return level == Level.EASY;
    }

    public boolean isLevelMedium () {
        return level == Level.MEDIUM;
    }


}
