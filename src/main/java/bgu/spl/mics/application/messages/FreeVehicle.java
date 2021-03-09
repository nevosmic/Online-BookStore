package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

public class FreeVehicle implements Event {
    private DeliveryVehicle myVehicle;

    public FreeVehicle(DeliveryVehicle v){
        myVehicle = v;
    }

    public DeliveryVehicle getMyVehicle() {
        return myVehicle;
    }
}
