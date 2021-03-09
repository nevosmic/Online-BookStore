package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;


public class CheckAvailabilty implements Event {
    private String bookTitle;

    public CheckAvailabilty (String book) {

        bookTitle = book;
    }
    public String getBookTitle(){
        return bookTitle;
    }
}
