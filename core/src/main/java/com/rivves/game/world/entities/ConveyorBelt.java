package com.rivves.game.world.entities;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.rivves.game.gfx.AssetLoader;
import com.rivves.game.world.blocks.Block;

public class ConveyorBelt{

    public static int width = 1, height = 1;

    private static int numberOfFrames = 8;
    private static TextureRegion[] framesRIGHT = new TextureRegion[numberOfFrames];
    private static TextureRegion[] framesDOWN = new TextureRegion[numberOfFrames];
    private static TextureRegion[] framesLEFT = new TextureRegion[numberOfFrames];
    private static TextureRegion[] framesUP = new TextureRegion[numberOfFrames];

    private static TextureRegion[] framesRIGHTDOWN = new TextureRegion[numberOfFrames];
    private static TextureRegion[] framesRIGHTUP = new TextureRegion[numberOfFrames];
    private static TextureRegion[] framesLEFTDOWN = new TextureRegion[numberOfFrames];
    private static TextureRegion[] framesLEFTUP = new TextureRegion[numberOfFrames];

    private static TextureRegion[] framesDOWNRIGHT = new TextureRegion[numberOfFrames];
    private static TextureRegion[] framesDOWNLEFT = new TextureRegion[numberOfFrames];
    private static TextureRegion[] framesUPRIGHT = new TextureRegion[numberOfFrames];
    private static TextureRegion[] framesUPLEFT = new TextureRegion[numberOfFrames];


    /*
                In a byte, 0 is the default, so that is no belt


                1  Right
                2  DOWN
                3  LEFT
                4  UP

                5  LeftDown
                6  LeftUp
                7  RightDown
                8  RightUp

                9  UpRight
                10 UpLeft
                11 DownRight
                12 DownLeft

                Have on Belt Place Event to match up corners?

                On Belt Place Event, calculate start and stop of belt this segment intersects?
                handle lone belt

                how can I handle items on belt segments?
                how can I handle into and out of machines?
                how can I handle the animations into and out of machines
                how can I structure it in such a way to handle 100k items on belts?
                is that even feasible on reasonable for a demo






     */


    private static int animationSpeed = 60;
    private static int currentFrame = 0;
    private static int[] animationTimes = {60, 60, 60, 60, 60, 60, 60, 60};//same length as textures array
    private static int currentTime = 0;



    public static void prepareTextures(){
        //this method is called to create the textures for the conveyorbelts, called from AssetLoader

        Texture sheet = AssetLoader.am.get("tiles/conveyor-Sheet.png", Texture.class);

        //RIGHT
        for(int i = 0; i < numberOfFrames; i++){
            TextureRegion tr = new TextureRegion(sheet, 16 * i, 0, 16, 16);
            framesRIGHT[i] = new TextureRegion(tr);
            tr.flip(true, false);
            framesLEFT[i] = new TextureRegion(tr);
            tr.flip(true, false);
        }

        //alright, now we have a 16x16 textureRegion called tr, we want to get the pixmap from it
        sheet.getTextureData().prepare();
        Pixmap wholePixmap = sheet.getTextureData().consumePixmap();

        //okay now lets create a small piece of said pixmap
        for(int i = 0; i < numberOfFrames; i++){

            Pixmap subPixmap = new Pixmap(16, 16, wholePixmap.getFormat());
            subPixmap.drawPixmap(wholePixmap, 0, 0, i * 16, 0, 16, 16);
            //now rotate the pixmap
            Pixmap rotatedSubMap = new Pixmap(subPixmap.getWidth(), subPixmap.getHeight(), subPixmap.getFormat());

            for(int y = 0; y < 16; y++){
                for(int x = 0; x < 16; x++){
                    int pixel = subPixmap.getPixel(x, y);
                    rotatedSubMap.drawPixel(y, 16 - 1 - x, pixel);
                }
            }


            Texture temp = new Texture(rotatedSubMap);
            TextureRegion tr = new TextureRegion(temp);
            framesUP[i] = new TextureRegion(tr);
            tr.flip(false, true);
            framesDOWN[i] = new TextureRegion(tr);
            //temp.dispose();
        }

        //okay now lets make the right to down texture

        Texture curved_sheet = AssetLoader.am.get("tiles/conveyor-curved-Sheet.png", Texture.class);
        for(int i = 0; i < numberOfFrames; i++){
            TextureRegion tr = new TextureRegion(curved_sheet, i * 16, 0, 16, 16);

            framesRIGHTDOWN[i] = new TextureRegion(tr);
            tr.flip(false, true);
            framesRIGHTUP[i] = new TextureRegion(tr);
            tr.flip(false, true);
            tr.flip(true, false);

            framesLEFTDOWN[i] = new TextureRegion(tr);
            tr.flip(false, true);
            framesLEFTUP[i] = new TextureRegion(tr);

        }

        curved_sheet.getTextureData().prepare();
        Pixmap wholecurvedPixmap = curved_sheet.getTextureData().consumePixmap();

        //okay now lets create a small piece of said pixmap
        for(int i = 0; i < numberOfFrames; i++){

            Pixmap subPixmap = new Pixmap(16, 16, wholecurvedPixmap.getFormat());
            subPixmap.drawPixmap(wholecurvedPixmap, 0, 0, i * 16, 0, 16, 16);
            //now rotate the pixmap
            Pixmap rotatedSubMap = new Pixmap(subPixmap.getWidth(), subPixmap.getHeight(), subPixmap.getFormat());

            for(int y = 0; y < 16; y++){
                for(int x = 0; x < 16; x++){
                    int pixel = subPixmap.getPixel(x, y);
                    rotatedSubMap.drawPixel(y, 16 - 1 - x, pixel);
                }
            }


            Texture temp = new Texture(rotatedSubMap);
            TextureRegion tr = new TextureRegion(temp);

            framesUPRIGHT[i] = new TextureRegion(tr); //UP RIGHT
            tr.flip(true, false);
            framesUPLEFT[i] = new TextureRegion(tr);

            tr.flip(true, false);
            tr.flip(false, true);

            framesDOWNRIGHT[i] = new TextureRegion(tr);
            tr.flip(true, false);
            framesDOWNLEFT[i] = new TextureRegion(tr);
        }





    }









    public static void tickAnimation(){
        currentTime += animationSpeed;

        if(currentTime >= animationTimes[currentFrame]){
            //then proceed to the next frame
            currentFrame++;
            if(currentFrame > (animationTimes.length - 1)){
                currentFrame = 0;
            }
            currentTime = 0;
        }
    }

    public static void tick(int x, int y, byte rotation) {

    }

    public static void render(SpriteBatch sb, int x, int y, byte rotation) {

        switch(rotation){
            case 1:
                sb.draw(framesRIGHT[currentFrame], x * Block.BlockWidth, y * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
                break;
            case 2:
                sb.draw(framesDOWN[currentFrame], (x + 0) * Block.BlockWidth, (y - 0) * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
                break;
            case 3:
                sb.draw(framesLEFT[currentFrame], (x + 0) * Block.BlockWidth, (y - 0) * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
                break;
            case 4:
                sb.draw(framesUP[currentFrame], (x + 0) * Block.BlockWidth, (y - 0) * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
                break;

            case 5:
                sb.draw(framesRIGHTDOWN[currentFrame], (x + 0) * Block.BlockWidth, (y - 0) * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
                break;
            case 6:
                sb.draw(framesRIGHTUP[currentFrame], (x + 0) * Block.BlockWidth, (y - 0) * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
                break;
            case 7:
                sb.draw(framesLEFTDOWN[currentFrame], (x + 0) * Block.BlockWidth, (y - 0) * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
                break;
            case 8:
                sb.draw(framesLEFTUP[currentFrame], (x + 0) * Block.BlockWidth, (y - 0) * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
                break;

            case 9:
                sb.draw(framesDOWNRIGHT[currentFrame], (x + 0) * Block.BlockWidth, (y - 0) * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
                break;
            case 10:
                sb.draw(framesDOWNLEFT[currentFrame], (x + 0) * Block.BlockWidth, (y - 0) * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
                break;
            case 11:
                sb.draw(framesUPRIGHT[currentFrame], (x + 0) * Block.BlockWidth, (y - 0) * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
                break;
            case 12:
                sb.draw(framesUPLEFT[currentFrame], (x + 0) * Block.BlockWidth, (y - 0) * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
                break;
        }




//        sb.draw(framesLEFTDOWN[currentFrame], x * Block.BlockWidth, y * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
//        sb.draw(framesDOWN[currentFrame], (x + 0) * Block.BlockWidth, (y - 1) * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
//        sb.draw(framesLEFT[currentFrame], (x + 0) * Block.BlockWidth, (y - 2) * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);
//        sb.draw(framesUP[currentFrame], (x + 0) * Block.BlockWidth, (y - 3) * Block.BlockHeight, Block.BlockWidth, Block.BlockHeight);

    }
    public static void dispose(){
        for(int i = 0; i < numberOfFrames; i++){
            framesRIGHT[i].getTexture().dispose();
            framesDOWN[i].getTexture().dispose();
            framesLEFT[i].getTexture().dispose();
            framesUP[i].getTexture().dispose();

            framesRIGHTDOWN[i].getTexture().dispose();
            framesRIGHTUP[i].getTexture().dispose();
            framesLEFTDOWN[i].getTexture().dispose();
            framesLEFTUP[i].getTexture().dispose();

            framesDOWNRIGHT[i].getTexture().dispose();
            framesDOWNLEFT[i].getTexture().dispose();
            framesUPRIGHT[i].getTexture().dispose();
            framesUPLEFT[i].getTexture().dispose();
        }
    }




}
