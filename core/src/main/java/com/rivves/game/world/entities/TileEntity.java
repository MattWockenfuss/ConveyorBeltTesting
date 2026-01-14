package com.rivves.game.world.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class TileEntity {

    /*
            TileEntities are all of the buildings in the game, like belts, miners, smelters etc...

     */

    protected int x, y; //these coords represent the bottom left of the tileEntity if its larger than 1x1
    protected int width, height;    //all buildings are rectangular

    public TileEntity(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void tick();
    public abstract void render(SpriteBatch sb);


}
