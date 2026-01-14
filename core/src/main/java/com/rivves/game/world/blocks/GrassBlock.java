package com.rivves.game.world.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.rivves.game.gfx.AssetLoader;

public class GrassBlock extends Block{

    public static Texture texture = AssetLoader.am.get("tiles/grass.png", Texture.class);

    public GrassBlock(byte ID) {
        super(ID, texture);
    }
}
