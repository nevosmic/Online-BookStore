package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class TakeFromInventory implements Event {
    private String bookTitle;

    public TakeFromInventory(String book){

        bookTitle = book;
    }
    public String getBookTitle(){
        return bookTitle;
    }
}
