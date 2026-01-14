package com.rivves.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.rivves.game.Handler;
import com.rivves.game.gfx.AssetLoader;
import com.rivves.game.input.Controls;
import com.rivves.game.world.blocks.AnimatedBlock;
import com.rivves.game.world.blocks.Block;
import com.rivves.game.world.blocks.BlockManager;
import com.rivves.game.world.blocks.WaterBlock;
import com.rivves.game.world.entities.ConveyorBelt;
import com.rivves.game.world.entities.ConveyorBeltManager;
import com.rivves.game.world.entities.TileEntity;

import java.util.ArrayList;

public class World {

    private Handler handler;
    private String name;
    private String path;

    private byte[][] TerrainTiles;
    private byte[][] belts;                         //maybe make belts have their own tier as they tick differently?
    /*
           By default, a Byte has a value of 0, so that will be no belt


                1  Right
                2  DOWN
                3  LEFT
                4  UP

                5  RightDown
                6  RightUp
                7  LeftDown
                8  LeftUp

                9  DownRight
                10  DownLeft
                11 UpRight
                12 UpLeft
     */
    private ArrayList<TileEntity> tileEntities;     //this is for all of the buildings in the game

    private ConveyorBeltManager cvm;

    private final int width, height;


    public World(Handler handler, String name, String path){
        this.handler = handler;
        this.name = name;
        this.path = path;

        BlockManager bm = new BlockManager(handler);
        handler.setBlockManager(bm);

        Texture texture = AssetLoader.am.get("world/world.png", Texture.class);
        TextureData textureData = texture.getTextureData();
        textureData.prepare();
        Pixmap pixmap = textureData.consumePixmap();

        width = pixmap.getWidth();
        height = pixmap.getHeight();

        readFileAsWorld(pixmap);

        pixmap.dispose();
        texture.dispose();
        AssetLoader.am.unload("world/world.png");

        cvm = new ConveyorBeltManager(handler);
    }

    private void readFileAsWorld(Pixmap pixmap){

        TerrainTiles = new byte[width][height];
        belts = new byte[width][height];
        tileEntities = new ArrayList<>();

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                String hexColor = Integer.toHexString(pixmap.getPixel(x, pixmap.getHeight() - 1 - y)).toUpperCase().substring(0, 6);

                switch (hexColor){
                    case "72B4FF":
                        TerrainTiles[x][y] = BlockManager.waterBlock.getID();
                        break;
                    case "FFD168":
                        TerrainTiles[x][y] = BlockManager.sandBlock.getID();
                        break;
                    case "7F3300":
                        TerrainTiles[x][y] = BlockManager.coarsesoilBlock.getID();
                        break;
                    case "FF8821":
                        TerrainTiles[x][y] = BlockManager.hotsandBlock.getID();
                        break;
                    case "2DBC42":
                        TerrainTiles[x][y] = BlockManager.grassBlock.getID();
                        break;
                    case "303030":
                        TerrainTiles[x][y] = BlockManager.darkstoneBlock.getID();
                        break;
                }

                belts[x][y] = 0;    //0 for no belt, 1 for low belt, 2 for fast belt?

            }
        }






    }



    public void tick(){
        cvm.tick();
        ConveyorBelt.tickAnimation();
        WaterBlock.tick();
    }
    public void render(SpriteBatch sb){

        //we currently render the entire world, we only want to render the portion we actually see


        /*
                        Okay, so 0, 0 is the bottom left of the screen
                        and Gdx.graphics.getWidth() is the width, usually 1080 or close to it



         */


        int startX = Math.max(0, (handler.getPlayer().getPlayerCameraX() - Gdx.graphics.getWidth() / 2) / Block.BlockWidth);
        int endX = Math.min(width - 1, (handler.getPlayer().getPlayerCameraX() + Gdx.graphics.getWidth() / 2) / Block.BlockWidth) + 1;
        int starty = Math.max(0, (handler.getPlayer().getPlayerCameraY() - Gdx.graphics.getHeight() / 2) / Block.BlockHeight);
        int endy = Math.min(height - 1, (handler.getPlayer().getPlayerCameraY() + Gdx.graphics.getHeight() / 2) / Block.BlockHeight) + 1;

        //to see the effect in action, add 1 to both startx and starty as opposed to endx and endy


        for(int y = starty; y < endy; y++){
            for(int x = startX; x < endX; x++){
                BlockManager.blocks[TerrainTiles[x][y]].render(sb, x * Block.BlockWidth, y * Block.BlockWidth);

                if(belts[x][y] != 0){
                    ConveyorBelt.render(sb, x, y, belts[x][y]);
                }


            }
        }


    }
    public void renderDebugBatch(SpriteBatch sb){
        cvm.renderDebugBatch(sb);
    }
    public void renderDebug(ShapeRenderer sr){
        cvm.renderDebug(sr);
    }

    public int getPixelWidth(){
        return width * Block.BlockWidth;
    }
    public int getPixelHeight(){
        return height * Block.BlockHeight;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public Byte getIDAt(int x, int y){
        return TerrainTiles[x][y];
    }
    public byte[][] getTerrainTiles(){
        return TerrainTiles;
    }
    public byte[][] getBelts(){
        return belts;
    }
    public ConveyorBeltManager getCVM(){
        return cvm;
    }

}
