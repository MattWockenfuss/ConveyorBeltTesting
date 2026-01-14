package com.rivves.game.world.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.rivves.game.gfx.AssetLoader;

public class CoarseSoilBlock extends Block{

    public static Texture texture = AssetLoader.am.get("tiles/coarse_soil.png", Texture.class);

    public CoarseSoilBlock(byte ID) {
        super(ID, texture);
    }
}
