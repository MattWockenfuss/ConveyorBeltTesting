package com.rivves.game.world.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rivves.game.Handler;
import com.rivves.game.gfx.AssetLoader;
import com.rivves.game.input.Controls;
import com.rivves.game.world.World;

import java.awt.*;

public class Player extends Entity{

    private OrthographicCamera playerCamera, hudCamera;
    private int playerCameraX, playerCameraY;

    private String name;
    private Handler handler;

    private float TEMP_imageScale = 6f;

    private TextureRegion[] animationFramesRIGHT;
    private TextureRegion[] animationFramesLEFT;
    private int numOfFrames = 8;
    private int ticksPerFrame = 3;
    private int animationCount = 0;//the current animation counter
    private int currentFrame = 0;
    private int animationLength = numOfFrames * ticksPerFrame;//remember 60 ticks per second

    public Player(Handler handler, World world, String name, int x, int y){
        super(world, x, y);
        this.name = name;
        this.handler = handler;

        //because this is the player, we can set the texWidth and texHeight
        this.texWidth = 32;
        this.texHeight = 80;

        Texture sheet = AssetLoader.am.get("character-Sheet.png", Texture.class);

        animationFramesRIGHT = new TextureRegion[numOfFrames];
        for(int i = 0; i < numOfFrames; i++){
            animationFramesRIGHT[i] = new TextureRegion(sheet, 16 * i, 0, 16, 32);
        }

        animationFramesLEFT = new TextureRegion[numOfFrames];
        for(int i = 0; i < numOfFrames; i++){
            TextureRegion tr = new TextureRegion(sheet, 16 * i, 0, 16, 32);
            tr.flip(true, false);
            animationFramesLEFT[i] = tr;
        }

        //sheet.dispose();//no longer needed
        texWidth = (int)(animationFramesRIGHT[0].getRegionWidth() * TEMP_imageScale);
        texHeight = (int)(animationFramesRIGHT[0].getRegionHeight() * TEMP_imageScale);


        /*
                The Player camera is moved with the player, so the player itself, the world and other entities are all rendered with the player camera
                the hud camera is rendered for anything that doesn't have a position relative to the world, inventories, Debug screen, options menu etc...
         */

        playerCamera = new OrthographicCamera();
        playerCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



    }


    public void tick(){
        float speed = 5f;

        if(handler.getIM().shift) speed = 100f;
        if(handler.getIM().y){
            x = 0;
            y = 0;
        }

        if(Controls.WALK_UP.isPressed()){
            yMove = speed;
        }
        if(Controls.WALK_DOWN.isPressed()){
            yMove = -speed;
        }

        if(Controls.WALK_LEFT.isPressed()){
            xMove = -speed;
            animationCount++;
        }

        if(Controls.WALK_RIGHT.isPressed()){
            xMove = speed;
            animationCount++;
        }

        if(animationCount > ticksPerFrame){
            animationCount = 0;
            currentFrame++;
            if(currentFrame > numOfFrames - 1){
                currentFrame = 0;
            }
        }

        //add functionality to reduce speed when pressing 2 at a time

        if(!Controls.WALK_LEFT.isPressed() && !Controls.WALK_RIGHT.isPressed())
            xMove = 0;
        if(!Controls.WALK_UP.isPressed() && !Controls.WALK_DOWN.isPressed())
            yMove = 0;
        move();
        updatePlayerCamera();







    }
    private void updatePlayerCamera(){
        //this method prevents the player from seeing beyond the map

        //okay so the player has a location (x,y) on the map and the screen has a width and height
        //because the map is always rectangular, an easy fix is to make a slightly smaller rectangle where the camera cannot move past

        //first get screenwidth and height
        int halfscreenwidth = Gdx.graphics.getWidth() / 2;
        int halfscreenheight = Gdx.graphics.getHeight() / 2;

        playerCameraX = x;
        playerCameraY = y;


        if(x < halfscreenwidth){
            playerCameraX = halfscreenwidth;
        }else if(x > (handler.getCurrentWorld().getPixelWidth() - halfscreenwidth)){
            playerCameraX = handler.getCurrentWorld().getPixelWidth() - halfscreenwidth;
        }

        if(y < halfscreenheight){
            playerCameraY = halfscreenheight;
        }else if(y > (handler.getCurrentWorld().getPixelHeight() - halfscreenheight)){
            playerCameraY = handler.getCurrentWorld().getPixelHeight() - halfscreenheight;
        }




        playerCamera.position.set(playerCameraX, playerCameraY, 0);





    }
    private boolean wasLastMoveLeft = false;
    public void render(SpriteBatch sb){

        if(Controls.WALK_LEFT.isPressed()){
//            for(int yT = -5; yT < 6; yT++){
//                for(int xT = -5; xT < 6; xT++){
//                    sb.draw(animationFramesLEFT[currentFrame], x - (texWidth / 2) + (xT * 100), y - (texHeight / 2) + (yT * 100), texWidth, texHeight);
//                }
//            }
            sb.draw(animationFramesLEFT[currentFrame], x - (texWidth / 2), y - (texHeight / 2), texWidth, texHeight);
            wasLastMoveLeft = true;
        }else if(Controls.WALK_RIGHT.isPressed()){
            sb.draw(animationFramesRIGHT[currentFrame], x - (texWidth / 2), y - (texHeight / 2), texWidth, texHeight);
            wasLastMoveLeft = false;
        }else{
            if(wasLastMoveLeft){
                sb.draw(animationFramesLEFT[currentFrame], x - (texWidth / 2), y - (texHeight / 2), texWidth, texHeight);
            }else{
                sb.draw(animationFramesRIGHT[currentFrame], x - (texWidth / 2), y - (texHeight / 2), texWidth, texHeight);
            }
        }




    }


    public OrthographicCamera getPlayerCamera(){
        return playerCamera;
    }
    public OrthographicCamera getHudCamera(){
        return hudCamera;
    }

    public int getPlayerCameraX(){
        return playerCameraX;
    }
    public int getPlayerCameraY(){
        return playerCameraY;
    }

    public Point getMouseCoords(){
        Point p = new Point(x - (Gdx.graphics.getWidth() / 2) + Gdx.input.getX(),
            y - (Gdx.graphics.getHeight() / 2) + (Gdx.graphics.getHeight() - Gdx.input.getY()));
        return p;
    }




}
