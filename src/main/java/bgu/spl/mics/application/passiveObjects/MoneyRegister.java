package bgu.spl.mics.application.passiveObjects;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Passive object representing the store finance management. 
 * It should hold a list of receipts issued by the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class MoneyRegister implements Serializable {
	private static MoneyRegister myRegister = null;
	private Vector<OrderReceipt> order;
	private int totalEarnings;

	private MoneyRegister(){

		order = new Vector<OrderReceipt>();
		totalEarnings = 0;
	}
	/**
     * Retrieves the single instance of this class.
     */

	public static MoneyRegister getInstance() {
		if(myRegister == null) {
			myRegister = new MoneyRegister();
		}
		return myRegister;
	}
	
	/**
     * Saves an order receipt in the money register.
     * <p>   
     * @param r		The receipt to save in the money register.
     */
	public void file (OrderReceipt r) {
		this.totalEarnings = totalEarnings + r.getPrice();
		this.order.add(r);
	}
	
	/**
     * Retrieves the current total earnings of the store.  
     */
	public int getTotalEarnings() {
		return totalEarnings;
	}
	
	/**
     * Charges the credit card of the customer a certain amount of money.
     * <p>
     * @param amount 	amount to charge
     */
	public void chargeCreditCard(Customer c, int amount) {// using the atomic action at the credit card object
			if (c.possibleToCharge(amount)) {
				c.setAvailbleAmountInCreditCard(c.getAvailableCreditAmount() - amount);
			}
		}

	
	/**
     * Prints to a file named @filename a serialized object List<OrderReceipt> which holds all the order receipts 
     * currently in the MoneyRegister
     * This method is called by the main method in order to generate the output.. 
     */
	public void printOrderReceipts(String filename) {

		List<OrderReceipt> orderReceiptList = new ArrayList<>();
		for(int i=0;i<order.size(); i=i+1){
			orderReceiptList.add(order.get(i));
		}
		try {
			FileOutputStream f = new FileOutputStream(filename);
			ObjectOutputStream o = new ObjectOutputStream(f);

			o.writeObject(orderReceiptList);
			o.close();
			f.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream");
		}
	}




}
