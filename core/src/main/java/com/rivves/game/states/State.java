package com.rivves.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.rivves.game.Handler;

public abstract class State implements Screen {
    protected Handler handler;
    protected Stage stage;
    protected InputMultiplexer inputMultiplexer;


    public State(Handler handler){
        this.handler = handler;
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(handler.getIM());
        inputMultiplexer.addProcessor(stage);
    }

    public void tick(){}

    public void onSecond(){
        //this function is called once per second
    }
}
