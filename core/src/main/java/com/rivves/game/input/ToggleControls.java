package com.rivves.game.input;

public class ToggleControls extends Controls{

    protected boolean isToggle = false;
    private boolean alreadyCounted = false;

    public ToggleControls(int ID, int keyCode) {
        super(ID, keyCode);
    }

    public void toggle(){
        if(!alreadyCounted){
            isPressed = !isPressed;
            alreadyCounted = true;
        }
    }
    public void setAlreadyCounted(boolean alreadyCounted){
        this.alreadyCounted = alreadyCounted;
    }



}
