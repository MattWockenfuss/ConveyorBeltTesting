package com.rivves.game.world.entities.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rivves.game.gfx.AssetLoader;
import com.rivves.game.world.entities.TileEntity;

public class Collector extends TileEntity {

    public static Texture texture = AssetLoader.am.get("buildings/collector.png", Texture.class);
    public static int width = 1, height = 1;

    public Collector(int x, int y) {
        super(x, y, width, height);
    }

    @Override
    public void tick() {

    }
    @Override
    public void render(SpriteBatch sb) {

    }
}
