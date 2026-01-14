package com.rivves.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.rivves.game.Handler;

public class InputManager implements InputProcessor {

    private Handler handler;

    private boolean[] keys;
    public boolean a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,
        space,enter,back,shift,escape,left_control,TAB,
        F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11,F12,
        num1,num2,num3,num4,num5,num6,num7,num8,num9,num0;


    //mouse stuff
    private int mouseX, mouseY;
    public boolean isMouseLeft, isMouseRight;
    public int mouseScroll;


    public InputManager(Handler handler){
        this.handler = handler;
        this.handler.setIM(this);
        keys = new boolean[256];
    }

    public void tick(){
        a = keys[Input.Keys.A];
        b = keys[Input.Keys.B];
        c = keys[Input.Keys.C];
        d = keys[Input.Keys.D];
        e = keys[Input.Keys.E];
        f = keys[Input.Keys.F];
        g = keys[Input.Keys.G];
        h = keys[Input.Keys.H];
        i = keys[Input.Keys.I];
        j = keys[Input.Keys.J];
        k = keys[Input.Keys.K];
        l = keys[Input.Keys.L];
        m = keys[Input.Keys.M];
        n = keys[Input.Keys.N];
        o = keys[Input.Keys.O];
        p = keys[Input.Keys.P];
        q = keys[Input.Keys.Q];
        r = keys[Input.Keys.R];
        s = keys[Input.Keys.S];
        t = keys[Input.Keys.T];
        u = keys[Input.Keys.U];
        v = keys[Input.Keys.V];
        w = keys[Input.Keys.W];
        x = keys[Input.Keys.X];
        y = keys[Input.Keys.Y];
        z = keys[Input.Keys.Z];

        space = keys[Input.Keys.SPACE];
        enter = keys[Input.Keys.ENTER];
        back = keys[Input.Keys.BACK];
        shift = keys[Input.Keys.SHIFT_LEFT];
        escape = keys[Input.Keys.ESCAPE];
        left_control = keys[Input.Keys.CONTROL_LEFT];
        TAB = keys[Input.Keys.TAB];

        F1 = keys[Input.Keys.F1];
        F2 = keys[Input.Keys.F2];
        F3 = keys[Input.Keys.F3];
        F4 = keys[Input.Keys.F4];
        F5 = keys[Input.Keys.F5];
        F6 = keys[Input.Keys.F6];
        F7 = keys[Input.Keys.F7];
        F8 = keys[Input.Keys.F8];
        F9 = keys[Input.Keys.F9];
        F10 = keys[Input.Keys.F10];
        F11 = keys[Input.Keys.F11];
        F12 = keys[Input.Keys.F12];

        num0 = keys[Input.Keys.NUM_0];
        num1 = keys[Input.Keys.NUM_1];
        num2 = keys[Input.Keys.NUM_2];
        num3 = keys[Input.Keys.NUM_3];
        num4 = keys[Input.Keys.NUM_4];
        num5 = keys[Input.Keys.NUM_5];
        num6 = keys[Input.Keys.NUM_6];
        num7 = keys[Input.Keys.NUM_7];
        num8 = keys[Input.Keys.NUM_8];
        num9 = keys[Input.Keys.NUM_9];


        //Holdables
        Controls.WALK_LEFT.setIsPressed(keys[Controls.WALK_LEFT.keyCode]);
        Controls.WALK_UP.setIsPressed(keys[Controls.WALK_UP.keyCode]);
        Controls.WALK_RIGHT.setIsPressed(keys[Controls.WALK_RIGHT.keyCode]);
        Controls.WALK_DOWN.setIsPressed(keys[Controls.WALK_DOWN.keyCode]);



        //Togglables
        if(keys[Controls.Debug.keyCode])
            Controls.Debug.toggle();
        else
            Controls.Debug.setAlreadyCounted(false);

        if(keys[Controls.trigOlverlay.keyCode])
            Controls.trigOlverlay.toggle();
        else
            Controls.trigOlverlay.setAlreadyCounted(false);

        if(keys[Controls.trigLightingOlverlay.keyCode])
            Controls.trigLightingOlverlay.toggle();
        else
            Controls.trigLightingOlverlay.setAlreadyCounted(false);

        if(keys[Controls.LightingShader.keyCode])
            Controls.LightingShader.toggle();
        else
            Controls.LightingShader.setAlreadyCounted(false);

        if(keys[Controls.flashLight.keyCode])
            Controls.flashLight.toggle();
        else
            Controls.flashLight.setAlreadyCounted(false);

        //Edge Controls
        if(keys[Controls.Rotate.keyCode])
            Controls.Rotate.attemptActivate();
        else
            Controls.Rotate.setAlreadyCounted(false);



    }


    @Override
    public boolean keyDown(int keycode) {
        keys[keycode] = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys[keycode] = false;
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }



    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseX = screenX;
        mouseY = screenY;
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        mouseScroll = (int) amountY;
        return false;
    }


    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //touchDown(): Called when a finger went down on the screen or a mouse button was pressed.
        //Reports the coordinates as well as the pointer index and mouse button (always Buttons.LEFT for touch screens).
        mouseX = screenX;
        mouseY = screenY;

        if(button == Input.Buttons.LEFT){
            isMouseLeft = true;
        }

        else if(button == Input.Buttons.RIGHT)
            isMouseRight = true;

        return false;
    }
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //touchUp(): Called when a finger was lifted from the screen or a mouse button was released.
        // Reports the last known coordinates as well as the pointer index and mouse button (always Buttons.Left for touch screens).
        mouseX = screenX;
        mouseY = screenY;

        if(button == Input.Buttons.LEFT)
            isMouseLeft = false;
        else if(button == Input.Buttons.RIGHT)
            isMouseRight = false;

        return false;
    }
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //touchDragged(): Called when a finger is being dragged over the screen or the mouse is dragged while a button is pressed.
        // Reports the coordinates and pointer index. The button is not reported as multiple buttons could be pressed while the mouse is being dragged.
        // You can use Gdx.input.isButtonPressed() to check for a specific button.
        return false;
    }




}
