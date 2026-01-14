package com.rivves.game.Utilities;

public class BeltChangeEvent {

    public int x, y, type;
    public boolean isDelete;

    public BeltChangeEvent(int x, int y, int type){
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public BeltChangeEvent(int x, int y, int type, boolean isDelete){
        this.x = x;                 //if this is a delete, than the type is the type of the belt that was deleted
        this.y = y;
        this.type = type;
        this.isDelete = isDelete;
    }



}
