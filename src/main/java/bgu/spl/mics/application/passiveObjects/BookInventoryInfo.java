package bgu.spl.mics.application.passiveObjects;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a information about a certain book in the inventory.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class BookInventoryInfo implements Serializable {
	//fields
	private String bookTitle;
	private AtomicInteger  amountInInventory;
	private int price;

	//constractur
	public BookInventoryInfo(String bookTitle,AtomicInteger amountInInventory, int price1){
		this.bookTitle=bookTitle;
		this.amountInInventory = amountInInventory;
		this.price=price1;
	}
	public BookInventoryInfo(JsonObject j){ // a constractur for the json object
		this.bookTitle = j.get("bookTitle").getAsString();
		this.amountInInventory = new AtomicInteger(j.get("amount").getAsInt());
		this.price = j.get("price").getAsInt();
	}

	/**
     * Retrieves the title of this book.
     * <p>
     * @return The title of this book.   
     */
	public String getBookTitle() {
		// TODO Implement this
		return this.bookTitle;
	}

	/**
     * Retrieves the amount of books of this type in the inventory.
     * <p>
     * @return amount of available books.      
     */
	public int getAmountInInventory() {
		// TODO Implement this
		return amountInInventory.get();
	}

	/**
     * Retrieves the price for  book.
     * <p>
     * @return the price of the book.
     */
	public int getPrice() {
		// TODO Implement this
		return this.price;
	}



	public void decreaseAmountInInventory(){

		this.amountInInventory.decrementAndGet();//atomic action, decrease by 1
	}

}
