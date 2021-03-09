package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Customer;

public class BookOrderEvent implements Event {
    private String myBook;
    private Customer myCustomer;
    private int myOrderTick;

    public BookOrderEvent(String book, Customer customer,int orderTick){
        this.myBook = book;
        this.myCustomer = customer;
        this.myOrderTick=orderTick;
    }
    public String getMyBook(){
        return myBook;
    }
    public Customer getMyCustomer(){
        return myCustomer;
    }
    public int getMyOrderTick(){
        return myOrderTick;
    }

}
