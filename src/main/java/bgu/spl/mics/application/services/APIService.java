package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;

import java.util.Vector;

import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TimeToGo;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import javafx.util.Pair;


/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class APIService extends MicroService{

	private Customer myClient;
	private Vector<Pair<String,Integer>> clientOrder;
	private Vector<Future<OrderReceipt>> myFutures;

	public APIService(int i, Customer client) {
		super("APIService "+i);
		this.myClient = client;
		this.clientOrder = new Vector<Pair<String,Integer>>();

		for(int j = 0; j < client.getMySchedule().size(); j++){
			clientOrder.add(client.getMySchedule().get(j));

		}
		myFutures = new Vector<Future<OrderReceipt>>();

		// TODO Implement this
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, br -> {
				int brodTick = br.getTick();
				for (int i = 0; i < clientOrder.size(); i = i + 1) {//send the OrderBookEvent on the tick specified on the schedule
					if (clientOrder.get(i).getValue().equals(brodTick)) {
						Future<OrderReceipt> receiptFuture = sendEvent(new BookOrderEvent(clientOrder.get(i).getKey(), myClient, brodTick));
						myFutures.add(receiptFuture);
					}
				}
				for (int i = 0; i < myFutures.size(); i = i + 1) {
					if (myFutures != null && myFutures.get(i).isDone()) {
						if (myFutures.get(i).get() != null) {
							myClient.addToRecipt(myFutures.get(i).get());//add to the client reciptList
							sendEvent(new DeliveryEvent(myClient));//doesn't return a future
						}
						myFutures.remove(i);
					}
				}

		});
		subscribeBroadcast(TimeToGo.class, br-> {
			//no one is waiting for this these unresolved futures but just in case..
			for(int i = 0; i < myFutures.size(); i = i+1){
				myFutures.get(i).resolve(null);
			}
			terminate();

		});
	}

		



}
