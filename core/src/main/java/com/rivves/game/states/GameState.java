package com.rivves.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.rivves.game.Handler;
import com.rivves.game.Main;
import com.rivves.game.gfx.AssetLoader;
import com.rivves.game.input.Controls;
import com.rivves.game.world.World;
import com.rivves.game.world.blocks.Block;
import com.rivves.game.world.blocks.BlockManager;
import com.rivves.game.world.entities.ConveyorBelt;
import com.rivves.game.world.entities.ConveyorBeltManager;
import com.rivves.game.world.entities.Player;

import java.awt.*;

public class GameState extends State implements Screen {

    private OrthographicCamera playerCamera, hudCamera;
    private SpriteBatch sb;
    private ShapeRenderer sr;

    private BitmapFont font;

    private Player player;
    private World world;
    private Point mouseWorldCoords, mouseScreenCoords, mouseBlockCoords = new Point(0, 0);


    private int ticksLastSecond = 0;
    private int ticks = 0;

    public GameState(Handler handler) {
        super(handler);

        world = new World(handler, "test world", "world/world.png");
        player = new Player(handler, world, "Matt", 19700, 31000);
        handler.setCurrentWorld(world);
        handler.setPlayer(player);

        playerCamera = player.getPlayerCamera();
        hudCamera = player.getHudCamera();


        sb = new SpriteBatch();
        sr = new ShapeRenderer();

        font = AssetLoader.am.get("ui/fonts/consolas.fnt", BitmapFont.class);







    }

    private byte rotation = 1;


    private int lastPlacedX = -1, lastPlacedY = -1;
    private byte lastType = -1;

    @Override
    public void tick() {
        ticks++;
        handler.getIM().tick();
        world.tick();
        player.tick();

        mouseBlockCoords = new Point((int)(mouseWorldCoords.x / Block.BlockWidth), (int)(mouseWorldCoords.y / Block.BlockHeight));

        if(handler.getIM().shift && Controls.Rotate.isPressed()){
            rotation -= 1;
            if(rotation <= 0)
                rotation = 12;
            Controls.Rotate.setIsPressed(false);
        }else if(Controls.Rotate.isPressed()){
            rotation += 1;
            if(rotation >= 13)
                rotation = 1;

            Controls.Rotate.setIsPressed(false);
        }



        if(handler.getIM().isMouseLeft){
            if(lastPlacedX != -1 && lastPlacedY != -1 && lastType != -1){
                /*
                        okay so we are holding left click and recently placed a rail

                        we also have the coordinates we are trying to place at,
                        maybe get the x difference and the y difference


                 */
                int tx = mouseBlockCoords.x - lastPlacedX;
                int ty = mouseBlockCoords.y - lastPlacedY;

                /*
                        okay so if tx is negative we are going to the left, if positive to the right, if 0 nowhere
                        if ty is postive then going up, if negative then down.sa





                 */

                /*
                        ✅ We want to make sure we dont skip on the diagonals
                        ✅ so only one or the other, not both

                        ✅ we also want to make sure that if there is a tile already there than we delete it

                        lets also make it so no matter what the first rail you place is, you can drag it in the other directions
                        make it work on edge of world/ make world infinite?

                 */


                if(tx != 0 ^ ty != 0){

                    byte typeRight = handler.getCurrentWorld().getBelts()[mouseBlockCoords.x + 1][mouseBlockCoords.y];
                    byte typeDown = handler.getCurrentWorld().getBelts()[mouseBlockCoords.x][mouseBlockCoords.y - 1];
                    byte typeLeft = handler.getCurrentWorld().getBelts()[mouseBlockCoords.x - 1][mouseBlockCoords.y];
                    byte typeUp = handler.getCurrentWorld().getBelts()[mouseBlockCoords.x][mouseBlockCoords.y + 1];

                    byte type = handler.getCurrentWorld().getBelts()[mouseBlockCoords.x][mouseBlockCoords.y];
                    byte placeType = 0;
                    if(type != 0)
                        handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x, mouseBlockCoords.y, type, true);

                    if(tx > 0){
                        if(typeLeft == 2){
                            //okay we are facing down, but going right, we want to check top left
                            byte typeTopLeft = handler.getCurrentWorld().getBelts()[mouseBlockCoords.x - 1][mouseBlockCoords.y + 1];
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x - 1, mouseBlockCoords.y, 2, true);

                            if(typeTopLeft == 2 || typeTopLeft == 5 || typeTopLeft == 7)
                                handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x - 1, mouseBlockCoords.y, 9, false);
                            else
                                handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x - 1, mouseBlockCoords.y, 1, false);


                        }else if(typeLeft == 4){
                            byte typeBottomLeft = handler.getCurrentWorld().getBelts()[mouseBlockCoords.x - 1][mouseBlockCoords.y - 1];
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x - 1, mouseBlockCoords.y, 4, true);

                            if(typeBottomLeft == 4 || typeBottomLeft == 6 || typeBottomLeft == 8)
                                handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x - 1, mouseBlockCoords.y, 11, false);
                            else
                                handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x - 1, mouseBlockCoords.y, 1, false);
                        }else if(typeLeft == 3){
                            byte typeLeftLeft = handler.getCurrentWorld().getBelts()[mouseBlockCoords.x - 2][mouseBlockCoords.y];
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x - 1, mouseBlockCoords.y, 3, true);
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x - 1, mouseBlockCoords.y, 1, false);
                            if(!(typeLeftLeft == 3 || typeLeftLeft == 7 || typeLeftLeft == 8)){

                            }
                        }
                        placeType = 1;
                    }else if(tx < 0){
                        if(typeRight == 2){
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x + 1, mouseBlockCoords.y, 2, true);
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x + 1, mouseBlockCoords.y, 10, false);
                        }else if(typeRight == 4){
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x + 1, mouseBlockCoords.y, 4, true);
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x + 1, mouseBlockCoords.y, 12, false);
                        }
                        placeType = 3;
                    }else if(ty > 0){
                        if(typeDown == 1){
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x, mouseBlockCoords.y - 1, 1, true);
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x, mouseBlockCoords.y - 1, 6, false);
                        }else if(typeDown == 3){
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x, mouseBlockCoords.y - 1, 3, true);
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x, mouseBlockCoords.y - 1, 8, false);
                        }
                        placeType = 4;
                    }else if(ty < 0){
                        if(typeUp == 1){
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x, mouseBlockCoords.y + 1, 1, true);
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x, mouseBlockCoords.y + 1, 5, false);
                        }else if(typeUp == 3){
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x, mouseBlockCoords.y + 1, 3, true);
                            handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x, mouseBlockCoords.y + 1, 7, false);
                        }

                        placeType = 2;
                    }
                    assert placeType != 0;
                    handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x, mouseBlockCoords.y, placeType, false);

                    lastPlacedX = mouseBlockCoords.x;
                    lastPlacedY = mouseBlockCoords.y;
                    lastType = type;

                }

            }else{
                //then we are placing a new rail section
                if(handler.getCurrentWorld().getBelts()[mouseBlockCoords.x][mouseBlockCoords.y] != rotation){
                    byte type = handler.getCurrentWorld().getBelts()[mouseBlockCoords.x][mouseBlockCoords.y];
                    if(type != 0)
                        handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x, mouseBlockCoords.y, type, true);
                    handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x, mouseBlockCoords.y, rotation, false);
                    lastPlacedX = mouseBlockCoords.x;
                    lastPlacedY = mouseBlockCoords.y;
                    lastType = type;
                }
            }

        }else{
            //we are not placing rails, reset all of the values
            if(handler.getIM().isMouseRight){
                byte type = handler.getCurrentWorld().getBelts()[mouseBlockCoords.x][mouseBlockCoords.y];
                if(type != 0){
                    handler.getCurrentWorld().getCVM().addChange(mouseBlockCoords.x, mouseBlockCoords.y, type, true);
                }

            }
            lastPlacedX = -1;
            lastPlacedY = -1;
            lastType = -1;
        }







    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(70 / 256f,70 / 256f,70 / 256f,1);

        mouseWorldCoords = player.getMouseCoords();
        mouseScreenCoords = new Point(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());



        renderPlayerCam();
        renderHudCam();





    }
    public void renderPlayerCam(){
        playerCamera.update();

        sb.setProjectionMatrix(playerCamera.combined);
        sr.setProjectionMatrix(playerCamera.combined);

        font.setColor(Color.BLACK);


        sb.begin();
            world.render(sb);
            player.render(sb);
            font.draw(sb, "Yeet", 48000, 48000);
            world.renderDebugBatch(sb);
        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.MAROON);
            sr.rect(mouseBlockCoords.x * Block.BlockWidth, mouseBlockCoords.y * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
        sr.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
            world.renderDebug(sr);
        sr.end();
    }
    public void renderHudCam(){
        hudCamera.update();

        sb.setProjectionMatrix(hudCamera.combined);
        sr.setProjectionMatrix(hudCamera.combined);

        font.setColor(Color.BLACK);


        sb.begin();
            font.draw(sb, "FPS: " + Gdx.graphics.getFramesPerSecond() + " TPS: " + ticksLastSecond, 10, Gdx.graphics.getHeight() - 10);
            font.draw(sb, "(x, y): (" + player.getX() + ", " + player.getY() + ")", 10, Gdx.graphics.getHeight() - 28);
        font.draw(sb, "Segments: " + handler.getCurrentWorld().getCVM().getSegmentCount(), 10, Gdx.graphics.getHeight() - 46);

            font.draw(sb, "Rotation: " + rotation, mouseScreenCoords.x + 32, mouseScreenCoords.y + 92);
            font.draw(sb, "(" + mouseBlockCoords.x + "," + mouseBlockCoords.y + ")", mouseScreenCoords.x + 32, mouseScreenCoords.y + 72);
            if(lastPlacedX != -1 && lastPlacedY != -1 && lastType != -1){
                int tx = mouseBlockCoords.x - lastPlacedX;
                int ty = mouseBlockCoords.y - lastPlacedY;
                font.draw(sb,  tx + ", " + ty, mouseScreenCoords.x + 32, mouseScreenCoords.y + 52);
            }


        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl20.glLineWidth(4.0f);


            sr.setColor(Color.YELLOW);
            //sr.rect(mouseScreenCoords.x - 25, mouseScreenCoords.y - 25, 50, 50);
        sr.end();
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
    public void onSecond() {
        ticksLastSecond = ticks;
        ticks = 0;
    }

    @Override
    public void dispose() {

    }

    public Point getMouseWorldCoords(){
        return mouseWorldCoords;
    }
    public Point getMouseScreenCoords(){
        return mouseScreenCoords;
    }
    public Point getMouseBlockCoords(){
        return mouseBlockCoords;
    }
}
