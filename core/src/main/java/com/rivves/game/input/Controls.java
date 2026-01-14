package com.rivves.game.input;

import com.badlogic.gdx.Input;

public class Controls {

    //Every Control is either a keyboard or mouse
    //For example you can create a Control.WALK_LEFT and set it to A or G or anything really
    //can also create a TogglableControl which is the same but it toggles on input

    public static Controls[] controls = new Controls[128];

    public static Controls WALK_LEFT = new Controls(0, Input.Keys.A);
    public static Controls WALK_UP = new Controls(1, Input.Keys.W);
    public static Controls WALK_RIGHT = new Controls(2, Input.Keys.D);
    public static Controls WALK_DOWN = new Controls(3, Input.Keys.S);

    public static ToggleControls Debug = new ToggleControls(4, Input.Keys.F1);
    public static ToggleControls trigOlverlay = new ToggleControls(5, Input.Keys.F2);
    public static ToggleControls trigLightingOlverlay = new ToggleControls(5, Input.Keys.F3);
    public static ToggleControls LightingShader = new ToggleControls(5, Input.Keys.F4);
    public static ToggleControls flashLight = new ToggleControls(6, Input.Keys.F);
    public static EdgeControls Rotate = new EdgeControls(7, Input.Keys.R);



    protected final int ID;
    protected boolean isPressed = false;
    protected int keyCode;

    public Controls(int ID, int keyCode){
        this.ID = ID;
        this.keyCode = keyCode;//if the keycode is pressed
        controls[ID] = this;
    }

    public boolean isPressed(){
        return isPressed;
    }
    public void setIsPressed(boolean pressed){
        this.isPressed = pressed;
    }
    public void setKeyCode(int keyCode){
        this.keyCode = keyCode;
    }






}

