package com.rivves.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.rivves.game.Handler;
import com.rivves.game.gfx.AssetLoader;

public class LoadingState extends State implements Screen {

    private float progress = 0;
    private int timer = 0;

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    public LoadingState(Handler handler) {
        super(handler);

        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void tick(){
        timer++;    //60 tps, 1200 = 20 seconds
        if(timer >= 30 && progress == 1.0){
            handler.getStateManager().createStates();
            //set the state to the game state
            //System.out.println("Switching to Menu State!");
            handler.getStateManager().setCurrentState(handler.getStateManager().getMenuState());
            AssetLoader.onDoneLoading();
        }
        //System.out.println("Timer: " + timer + " Progress: " + progress);
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(53 / 256f,81 / 256f,92 / 256f,1);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(20);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(300, 300, 100 + (500 * progress), 400);
        shapeRenderer.end();

        if(progress == 1.0){
            //System.out.println("Done Loading!");
        }
    }





    @Override
    public void resize(int width, int height) {

    }


    @Override
    public void pause() {

    }
    @Override
    public void resume() {

    }


    @Override
    public void show() {

    }
    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
    public void setProgress(float progress){
        this.progress = progress;
    }

}
