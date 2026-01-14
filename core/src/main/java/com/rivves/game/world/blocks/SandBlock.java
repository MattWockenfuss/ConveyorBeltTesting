package com.rivves.game.world.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.rivves.game.gfx.AssetLoader;

public class SandBlock extends Block{

    public static Texture texture = AssetLoader.am.get("tiles/sand.png", Texture.class);

    public SandBlock(byte ID) {
        super(ID, texture);
    }
}
