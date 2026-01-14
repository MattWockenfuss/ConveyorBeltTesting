package com.rivves.game.world.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rivves.game.world.World;

import java.awt.*;

public abstract class Entity {

    protected World world;
    protected int x, y;
    protected float xMove, yMove;
    protected int texWidth, texHeight;
    protected Rectangle collisionBox;

    public Entity(World world, int x, int y){
        this.world = world;
        this.x = x;
        this.y = y;
        xMove = 0;
        yMove = 0;
    }


    public abstract void tick();
    public abstract void render(SpriteBatch sb);


    public void move(){
        x += (int) xMove;
        y += (int) yMove;
    }

    private void checkCollisions(){
        //this function changes xMove and yMove incase we collid with something
    }




    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public float getxMove(){return this.xMove;}
    public float getyMove(){return this.yMove;}

}
