package com.rivves.game.world.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.rivves.game.gfx.AssetLoader;

public class HotSandBlock extends Block{

    public static Texture texture = AssetLoader.am.get("tiles/hotsand.png", Texture.class);

    public HotSandBlock(byte ID) {
        super(ID, texture);
    }
}
