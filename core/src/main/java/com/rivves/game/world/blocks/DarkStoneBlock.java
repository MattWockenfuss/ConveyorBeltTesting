package com.rivves.game.world.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.rivves.game.gfx.AssetLoader;

public class DarkStoneBlock extends Block{

    public static Texture texture = AssetLoader.am.get("tiles/dark_stone.png", Texture.class);

    public DarkStoneBlock(byte ID) {
        super(ID, texture);
    }
}
