package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;

public class DeliveryEvent implements Event {
    private Customer myCustomer;

    public DeliveryEvent (Customer c){

        myCustomer = c;
    }

    public Customer getMyCustomer()
    {
        return myCustomer;
    }



}
