package com.rivves.game.world.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rivves.game.Handler;
import com.rivves.game.gfx.AssetLoader;

public class BlockManager {

    private Handler handler;
    public static Block[] blocks = new Block[128];

    public static Block sandBlock;
    public static Block waterBlock;
    public static Block hotsandBlock;
    public static Block coarsesoilBlock;
    public static Block grassBlock;
    public static Block darkstoneBlock;


    public BlockManager(Handler handler){
        this.handler = handler;
        blockInit();
    }

    public void blockInit(){
        waterBlock = new WaterBlock((byte) 0);
        sandBlock = new SandBlock((byte) 1);
        hotsandBlock = new HotSandBlock((byte) 2);
        coarsesoilBlock = new CoarseSoilBlock((byte) 3);
        grassBlock = new GrassBlock((byte) 4);
        darkstoneBlock = new DarkStoneBlock((byte) 5);
    }


}
