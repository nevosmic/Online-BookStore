package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class ResourcesHolder {
	private static ResourcesHolder myResourcesHolder = null;
	private ConcurrentHashMap<DeliveryVehicle,Boolean> isFree;
	private Vector<DeliveryVehicle> myVehicles;
	private LinkedBlockingQueue<Future<DeliveryVehicle>> waitingList;


	/**
     * Retrieves the single instance of this class.
     */
	private ResourcesHolder (){
		isFree = new ConcurrentHashMap<>();
		myVehicles = new Vector<DeliveryVehicle>();
		this.waitingList = new LinkedBlockingQueue<>();
	}
	public static ResourcesHolder getInstance() {
		if(myResourcesHolder == null) {
			myResourcesHolder = new ResourcesHolder();
		}
		return myResourcesHolder;
	}
	
	/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	public Future<DeliveryVehicle> acquireVehicle() {
		Future<DeliveryVehicle> output = new Future<>();
		for (int i = 0; i < myVehicles.size(); i++) {//check who is free
			DeliveryVehicle currentV = myVehicles.get(i);
			synchronized (currentV) {//we dont want any other thread to work with this currentV
				if (isFree.get(currentV)) {
					this.lockV(myVehicles.get(i));
					output.resolve(myVehicles.get(i));
					return output;
				}
			}

		}
		waitingList.add(output);// Queue of futures that unresolved
		return output;//empty future
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle(DeliveryVehicle vehicle) {
		if (!waitingList.isEmpty()) {
			waitingList.poll().resolve(vehicle);
		} else {
			isFree.put(vehicle,true);//released
		}
	}
	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles) {
		//TODO: Implement this
		for (int i = 0; i < vehicles.length; i++) {
			myVehicles.add(vehicles[i]);
			isFree.put(vehicles[i], true);//in the beginning everyone is free;


		}
	}

	private void lockV (DeliveryVehicle v) {
		isFree.put(v,false);//locked now
	}
}
