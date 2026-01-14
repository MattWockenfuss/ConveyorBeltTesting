package com.rivves.game.world.blocks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class AnimatedBlock extends Block{

    //animated blocks have an array of textures,
    //they also have an animated speed
    private static int animationSpeed;

    private static int currentFrame = 0;
    private static int[] animationTimes;//same length as textures array
    private static int currentTime = 0;



    public AnimatedBlock(byte ID, TextureRegion[] textures, int[] animationTimes, int animationSpeed) {
        super(ID, textures);
        this.animationSpeed = animationSpeed;
        this.animationTimes = animationTimes;
    }

    public static void tick(){
        currentTime += animationSpeed;

        if(currentTime >= animationTimes[currentFrame]){
            //then proceed to the next frame
            currentFrame++;
            if(currentFrame > (animationTimes.length - 1)){
                currentFrame = 0;
            }
            currentTime = 0;
        }


    }
    public void render(SpriteBatch sb, int x, int y){
        sb.draw(textures[currentFrame], x, y, Block.BlockWidth, Block.BlockHeight);
    }

}
