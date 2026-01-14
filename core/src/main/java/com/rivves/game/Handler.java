package com.rivves.game;

import com.rivves.game.input.InputManager;
import com.rivves.game.states.StateManager;
import com.rivves.game.world.World;
import com.rivves.game.world.blocks.BlockManager;
import com.rivves.game.world.entities.Player;

public class Handler {
    //the handler object handles a variety of commonly need functions or objects that we can get in our game.

    private World world;
    private boolean isGameRunning = true;

    private Main game;
    private Player player;
    private InputManager inputManager;
    private BlockManager blockManager;
//    private AudioManager audioManager;
//    private LightManager lightManager;

    private StateManager stateManager;

    public Handler() {

    }
    public World getCurrentWorld(){
        return world;
    }

    public InputManager getIM(){
        return inputManager;
    }
//    public AudioManager getAudioManager() {
//        return audioManager;
//    }
//    public LightManager getLightManager() {
//        return lightManager;
//    }
    public StateManager getStateManager(){
        return stateManager;
    }

    ////////////////////////////////SETTERS////////////////////////////////////
    public void setCurrentWorld(World world){
        this.world = world;
    }

    public void setIM(InputManager inputManager){
        this.inputManager = inputManager;
    }
//    public void setAudioManager(AudioManager audioManager) {
//        this.audioManager = audioManager;
//    }
//    public void setLightManager(LightManager lightManager) {
//        this.lightManager = lightManager;
//    }
    public void setStateFarm(StateManager stateManager){
        this.stateManager = stateManager;
    }

    public void setGameStatus(boolean running){
        isGameRunning = running;
    }
    public boolean isGameRunning(){
        return isGameRunning;
    }

    public void setGame(Main game) {
        this.game = game;
    }
    public Main getGame(){
        return game;
    }
    public Player getPlayer(){
        return player;
    }
    public void setPlayer(Player p){
        this.player = p;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }
    public void setBlockManager(BlockManager blockManager) {
        this.blockManager = blockManager;
    }
}
