package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;


public class TickBroadcast implements Broadcast {
    private int myTick;
    private boolean isItTimeToGo;
    public TickBroadcast(int tick,boolean t){
        this.myTick=tick;
        this.isItTimeToGo =t;
    }

    public int getTick(){

        return myTick;
    }
    public boolean getIsItTimeToGo(){

        return isItTimeToGo;
    }
}
