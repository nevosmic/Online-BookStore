package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

public class AcquireEvent implements Event<Future<DeliveryVehicle>> {
    private String name;
    public AcquireEvent(String n){
        this.name = n;
    }
    public String getName(){
        return name;
    }
}

