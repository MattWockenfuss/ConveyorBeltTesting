package com.rivves.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.rivves.game.Handler;
import com.rivves.game.gfx.AssetLoader;
import com.rivves.game.input.Controls;
import com.rivves.game.input.InputManager;

public class MenuState extends State implements Screen {

    private OrthographicCamera camera;
    private SpriteBatch sb;

    private int ticksLastSecond = 0;
    private int ticks = 0;

    private BitmapFont font;

    private Texture wand;
    private Sprite[] sprites;
    private float[] rotations;


    public MenuState(Handler handler) {
        super(handler);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sb = new SpriteBatch();

        wand = AssetLoader.am.get("magicwand.png", Texture.class);

        font = AssetLoader.am.get("ui/fonts/consolas.fnt", BitmapFont.class);


        sprites = new Sprite[128];
        rotations = new float[128];

        int xOffset = 0;
        int yOffset = 0;
        int jump = 60;


        for(int i = 0; i < 128; i++){
            sprites[i] = new Sprite(wand);
            sprites[i].setScale(4);

            if(xOffset >= 16){
                xOffset = 0;
                yOffset++;
            }
            sprites[i].setPosition(300 + (xOffset * jump), Gdx.graphics.getHeight() - 300 - (yOffset * jump));
            xOffset++;
            sprites[i].setOrigin(0,0);//sets the rotation about to the bottom left, or the bottom left of rod

            rotations[i] = MathUtils.random(-1.0f, 1.0f);

        }
    }


    @Override
    public void tick() {
        ticks++;
        for(int i = 0; i < 128; i++){
            sprites[i].rotate(rotations[i]);
        }

        if(Controls.WALK_LEFT.isPressed()){
            for(Sprite s : sprites){
                s.rotate(0.5f);
            }
        }
        if(Controls.WALK_RIGHT.isPressed()){
            for(Sprite s : sprites){
                s.rotate(-0.5f);
            }
        }

        if(Controls.WALK_UP.isPressed() || Controls.WALK_RIGHT.isPressed() || Controls.WALK_LEFT.isPressed() || Controls.WALK_DOWN.isPressed()){
            //if we try to move the character, set the state to game State
            handler.getStateManager().setCurrentState(handler.getStateManager().getGameState());
        }


    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(232 / 256f,232 / 256f,232 / 256f,1);
        camera.update();
        sb.setProjectionMatrix(camera.combined);

        sb.begin();
        for(Sprite s : sprites){
            s.draw(sb);
        }

        font.setColor(Color.BLACK);
        font.draw(sb, "FPS: " + Gdx.graphics.getFramesPerSecond() + " TPS: " + ticksLastSecond, 10, Gdx.graphics.getHeight() - 10);


        sb.end();

    }

    @Override
    public void onSecond(){
        //this function is run once per second
        ticksLastSecond = ticks;
        ticks = 0;
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
        sb.dispose();
    }
}
