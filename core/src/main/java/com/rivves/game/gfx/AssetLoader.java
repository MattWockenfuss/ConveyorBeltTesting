package com.rivves.game.gfx;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.rivves.game.Handler;
import com.rivves.game.world.blocks.WaterBlock;
import com.rivves.game.world.entities.ConveyorBelt;

public class AssetLoader {

    public static AssetManager am = new AssetManager();

    public static void tick(){
        am.update(17);
    }

    public static void load(){
        loadTextures();
        loadSkins();
        loadFonts();
        loadAudio();
    }

    private static void loadTextures(){
        am.load("tiles/coarse_soil.png", Texture.class);
        am.load("tiles/dark_stone.png", Texture.class);
        am.load("tiles/sand.png", Texture.class);
        am.load("tiles/hotsand.png", Texture.class);
        am.load("tiles/grass.png", Texture.class);
        am.load("tiles/water_tile-Sheet.png", Texture.class);
        am.load("tiles/conveyor-Sheet.png", Texture.class);
        am.load("tiles/conveyor-curved-Sheet.png", Texture.class);




        am.load("flashlight.png", Texture.class);
        am.load("magicwand.png", Texture.class);
        am.load("character-Sheet.png", Texture.class);

        //load the items for each tier
        am.load("items/base/air.png", Texture.class);
        am.load("items/base/earth.png", Texture.class);
        am.load("items/base/fire.png", Texture.class);
        am.load("items/base/water.png", Texture.class);


        am.load("buildings/collector.png", Texture.class);





        //load the world aswell
        am.load("world/world.png", Texture.class);

    }
    private static void loadSkins(){
        am.load("ui/skins/clean/clean-crispy-ui.json", Skin.class);
        am.load("ui/skins/cloud/cloud-form-ui.json", Skin.class);
        am.load("ui/skins/default/uiskin.json", Skin.class);
        am.load("ui/skins/plainjames/plain-james-ui.json", Skin.class);
        am.load("ui/skins/vis/uiskin.json", Skin.class);
    }
    private static void loadFonts(){
        am.load("ui/fonts/segoeUI.fnt", BitmapFont.class);
        am.load("ui/fonts/consolas.fnt", BitmapFont.class);
    }
    private static void loadAudio(){
//        am.load("audio/menu_music.mp3", Music.class);
//        am.load("audio/button-hover2.wav", Sound.class);
    }

    public static void onDoneLoading(){
        ConveyorBelt.prepareTextures();
        WaterBlock.prepareTextures();
    }

    public static float getProgress(){
        return am.getProgress();
    }


    public static void dispose(){
        //called when the game is being closed to free up resources
        ConveyorBelt.dispose();
        am.dispose();

    }
}
