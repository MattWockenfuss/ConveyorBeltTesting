package com.rivves.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.rivves.game.gfx.AssetLoader;
import com.rivves.game.input.InputManager;
import com.rivves.game.states.StateManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */


public class Main extends Game {

    public Handler handler;

    private InputManager inputManager;
    private StateManager stateManager;


    public void create() {
        AssetLoader.load();

        handler = new Handler();
        handler.setGame(this);

        inputManager = new InputManager(handler);
        Gdx.input.setInputProcessor(inputManager);

        stateManager = new StateManager(handler);
        stateManager.setCurrentState(stateManager.getLoadingState());
    }

    public void render(){
        AssetLoader.tick();
        inputManager.tick();
        stateManager.getLoadingState().setProgress(AssetLoader.getProgress());
        if(AssetLoader.getProgress() == 1.0){//returns true if loaded, false otherwise
            shouldTick();
            super.render();
            stateManager.render();

        }

        if(!handler.isGameRunning()){
            dispose();
            Gdx.app.exit();
        }

    }

    int seconds = 0;
    int targetTPS = 60;
    double timePerTick = 1000000000 / targetTPS;
    double delta = 0;
    long now;
    long lastTime = System.nanoTime();
    long timer = 0; //every second clock
    int ticks = 0;

    public void shouldTick(){
        now = System.nanoTime();
        delta += (now - lastTime) / timePerTick;
        timer += now - lastTime;
        lastTime = now;
        if(delta >= 1) {
            ticks++;
            stateManager.tick();
            delta--;
        }

        if(timer >= 1000000000) {
            //System.out.println("Seconds Elapsed: " + seconds++);
            stateManager.getCurrentState().onSecond();
            timer = 0;
        }

    }



    public void dispose(){
        AssetLoader.dispose();
    }


}
