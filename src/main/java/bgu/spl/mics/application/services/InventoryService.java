package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CheckAvailabilty;
import bgu.spl.mics.application.messages.TakeFromInventory;
import bgu.spl.mics.application.messages.TimeToGo;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.OrderResult;

/**
 * InventoryService is in charge of the book inventory and stock.
 * Holds a reference to the {@link Inventory} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class InventoryService extends MicroService {
	private Inventory myInventInstance;


	public InventoryService(int i) {
		super("InventoryService "+i);
		// TODO Implement this
		myInventInstance = Inventory.getInstance();

	}

	@Override
	protected void initialize() {
		// TODO Implement this
		subscribeEvent(CheckAvailabilty.class, ev-> {
			int price = myInventInstance.checkAvailabiltyAndGetPrice(ev.getBookTitle());
			complete(ev,price);

		});
		subscribeEvent(TakeFromInventory.class,ev->{
			OrderResult myOrderR= myInventInstance.take(ev.getBookTitle());
			complete(ev,myOrderR);
		});
		subscribeBroadcast(TimeToGo.class, br-> {
			terminate();

		});




	}
}

