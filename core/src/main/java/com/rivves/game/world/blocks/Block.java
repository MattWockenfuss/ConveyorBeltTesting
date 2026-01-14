package com.rivves.game.world.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Block {



    public static final int BlockWidth = 64;
    public static final int BlockHeight = 64;

    protected Texture texture;
    protected TextureRegion[] textures;
    protected final byte ID;

    public Block(byte ID, Texture texture){     //used for still blocks
        this.ID = ID;
        this.texture = texture;
        BlockManager.blocks[ID] = this;
    }
    public Block(byte ID, TextureRegion[] textures){ //used for animated blocks
        this.ID = ID;
        this.textures = textures;
        BlockManager.blocks[ID] = this;
    }

    public void render(SpriteBatch sb, int x, int y){
        sb.draw(texture, x, y, Block.BlockWidth, Block.BlockHeight);
    }
    public Byte getID(){
        return this.ID;
    }


}
