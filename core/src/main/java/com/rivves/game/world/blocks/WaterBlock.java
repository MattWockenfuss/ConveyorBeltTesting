package com.rivves.game.world.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rivves.game.gfx.AssetLoader;


public class WaterBlock extends AnimatedBlock{

    private static Texture texture = AssetLoader.am.get("tiles/water_tile-Sheet.png", Texture.class);
    public static TextureRegion[] textures = new TextureRegion[4];

    public WaterBlock(byte ID) {
        super(ID, textures, new int[]{60, 60, 60, 60}, 3);
    }

    public static void prepareTextures(){
        for(int i = 0; i < 4; i++){
            textures[i] = new TextureRegion(texture, 16 * i, 0, 16, 16);
        }
    }

}
