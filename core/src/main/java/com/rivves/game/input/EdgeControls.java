package com.rivves.game.input;

public class EdgeControls extends Controls{

    private boolean alreadyCounted = false;

    public EdgeControls(int ID, int keyCode) {
        super(ID, keyCode);
    }

    public void attemptActivate(){
        if(!alreadyCounted){
            isPressed = true;
            alreadyCounted = true;
        }
    }

    public void setAlreadyCounted(boolean alreadyCounted){
        this.alreadyCounted = alreadyCounted;
    }

}
