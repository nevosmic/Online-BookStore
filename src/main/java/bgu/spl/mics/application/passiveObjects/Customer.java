package bgu.spl.mics.application.passiveObjects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javafx.util.Pair;


import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Passive data-object representing a customer of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Customer implements Serializable {

	private int id;
	private String name;
	private String address;
	private  int distance;
	private List<OrderReceipt> receipts;
	private CreditCard myCard;
	private Vector<Pair<String,Integer>> mySchedule;

	public Customer(int id, String name,String address,int distance,int creditCard,int availableAmountInCreditCard ){
		this.id = id;
		this.name =name;
		this.address = address;
		this. distance = distance;
		this.receipts = new Vector<OrderReceipt>();
		this.myCard = new CreditCard(creditCard,availableAmountInCreditCard);

	}
	public Customer(JsonObject j){
		this.id = j.get("id").getAsInt();
		this.name = j.get("name").getAsString();
		this.address = j.get("address").toString();
		this.distance = j.get("distance").getAsInt();
		this.receipts = new Vector<OrderReceipt>();
		this.myCard = new CreditCard(j.get("creditCard").getAsJsonObject());
		this.mySchedule = new Vector<Pair<String, Integer>>();
		JsonArray myScheduleArray =  j.get("orderSchedule").getAsJsonArray();
		for(int i = 0;i < myScheduleArray.size();i++){
			String bookT = myScheduleArray.get(i).getAsJsonObject().get("bookTitle").getAsString();
			Integer tick = myScheduleArray.get(i).getAsJsonObject().get("tick").getAsInt();
			mySchedule.add(new Pair<String, Integer>(bookT,tick));

		}
	}

	/**
     * Retrieves the name of the customer.
     */
	public String getName() {
		// TODO Implement this
		return this.name;
	}

	/**
     * Retrieves the ID of the customer  . 
     */
	public int getId() {
		// TODO Implement this
		return this.id;
	}
	
	/**
     * Retrieves the address of the customer.  
     */
	public String getAddress() {
		// TODO Implement this
		return this.address;
	}
	
	/**
     * Retrieves the distance of the customer from the store.  
     */
	public int getDistance() {
		// TODO Implement this
		return this.distance;
	}

	
	/**
     * Retrieves a list of receipts for the purchases this customer has made.
     * <p>
     * @return A list of receipts.
     */
	public List<OrderReceipt> getCustomerReceiptList() {
		// TODO Implement this
		return this.receipts;
	}

	public CreditCard getMyCard() {
		return myCard;
	}

	/**
     * Retrieves the amount of money left on this customers credit card.
     * <p>
     * @return Amount of money left.   
     */
	public int getAvailableCreditAmount() {
		// TODO Implement this
		return this.myCard.getAmount().get();
	}


    /**  Retrieves this customers credit card serial number.
     */
	public int getCreditNumber() {
		// TODO Implement this
		return this.myCard.getNumber();
	}


	public boolean possibleToCharge(int amount){
		boolean output = true;
		if(this.myCard.getAmount().get() < amount){
			output = false;
		}
		return output;
	}
	public void setAvailbleAmountInCreditCard(int newAmount){//need compare and set

		this.myCard.setAmount(newAmount);
	}

	public void addToRecipt(OrderReceipt o){

		this.receipts.add(o);
	}
	public Vector<Pair<String,Integer>> getMySchedule () {

		return this.mySchedule;
	}
	
}
