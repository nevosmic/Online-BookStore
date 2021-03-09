package bgu.spl.mics.application.passiveObjects;

import java.io.*;

import java.util.HashMap;
import java.util.Vector;

/**
 * Passive data-object representing the store inventory.
 * It holds a collection of {@link BookInventoryInfo} for all the
 * books in the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory implements Serializable {//singelton
	private HashMap<String,Integer> myBookMap;
	private static Inventory inventoryInstance = null;
	private  Vector<BookInventoryInfo> bookCollection;

	/**
     * Retrieves the single instance of this class.
     */
	private Inventory(){

		bookCollection = new Vector<BookInventoryInfo>();
	}
	public static Inventory getInstance() {//there is only one thread which initializes all the instances of the classes in the program
		//TODO: Implement this
		if(inventoryInstance == null) {
			inventoryInstance = new Inventory();
		}
		return inventoryInstance;
	}

	/**
     * Initializes the store inventory. This method adds all the items given to the store
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (BookInventoryInfo[ ] inventory ) {
		//TODO: Implement this
		for(int i = 0; i < inventory.length; i++){
			this.bookCollection.add(inventory[i]);
		}

	}
	
	/**
     * Attempts to take one book from the store.
     * <p>
     * @param book 		Name of the book to take from the store
     * @return 	an {@link Enum} with options NOT_IN_STOCK and SUCCESSFULLY_TAKEN.
     * 			The first should not change the state of the inventory while the 
     * 			second should reduce by one the number of books of the desired type.
     */
	public OrderResult take (String book) {
		//TODO: Implement this
		synchronized (book) {
			OrderResult output = OrderResult.NOT_IN_STOCK;
			BookInventoryInfo temp = this.searchByTitle(book);
			if (temp != null) {
				//no need to synch, selling service is in charge of the concurrency
				if (temp.getAmountInInventory() != 0) {//found
					temp.decreaseAmountInInventory();//an atomic action, thus thread safe
					output = OrderResult.SUCCESSFULLY_TAKEN;

				}
			}

			return output;
		}
	}
	
	/**
     * Checks if a certain book is available in the inventory.
     * <p>
     * @param book 		Name of the book.
     * @return the price of the book if it is available, -1 otherwise.
     */
	public int checkAvailabiltyAndGetPrice(String book) {
		//TODO: Implement this
		int output = -1;
		BookInventoryInfo temp = this.searchByTitle(book);
		if(temp != null && temp.getAmountInInventory() > 0){
			output = temp.getPrice();
		}
		return output;
	}
	
	/**
     * 
     * <p>
     * Prints to a file name @filename a serialized object HashMap<String,Integer> which is a Map of all the books in the inventory. The keys of the Map (type {@link String})
     * should be the titles of the books while the values (type {@link Integer}) should be
     * their respective available amount in the inventory. 
     * This method is called by the main method in order to generate the output.
     */
	public void printInventoryToFile(String filename) {

		myBookMap = new HashMap<String, Integer>();
		for (int i = 0; i < bookCollection.size(); i = i + 1) {
			myBookMap.put(bookCollection.get(i).getBookTitle(), bookCollection.get(i).getAmountInInventory());//initialize hashmap
		}

		try {
			FileOutputStream f = new FileOutputStream(filename);
			ObjectOutputStream o = new ObjectOutputStream(f);

			o.writeObject(myBookMap);
			o.close();//need a catch if its not close?
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");

		}
	}
	private BookInventoryInfo searchByTitle(String title){//not thread safe

		BookInventoryInfo output=null;
		for(int i=0;i<bookCollection.size();i=i+1){
			if(bookCollection.get(i).getBookTitle().equals(title)){
				return  bookCollection.get(i);
			}
		}
		return output;
	}

}
