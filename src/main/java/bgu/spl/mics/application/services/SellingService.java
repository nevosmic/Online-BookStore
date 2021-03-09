package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import bgu.spl.mics.application.passiveObjects.OrderResult;
import org.omg.PortableInterceptor.SUCCESSFUL;

/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService{
	private MoneyRegister Mr;

	private int currentTick;

	public SellingService(int i) {

		super("sellingService "+i);
		Mr=MoneyRegister.getInstance();

	}

	@Override
	protected void initialize() {
		subscribeEvent(BookOrderEvent.class, ev -> {
			int proccessTick = currentTick;
			Future<Integer> priceFuture = sendEvent(new CheckAvailabilty(ev.getMyBook()));
			if (priceFuture !=null && priceFuture.get() != null && priceFuture.get() != -1) {//if not -1 this book available in stock
				synchronized (ev.getMyCustomer()) {
					int priceF = priceFuture.get();
					if (ev.getMyCustomer().possibleToCharge(priceF)) {//check if the customer can pay
						Future<OrderResult> order = sendEvent(new TakeFromInventory(ev.getMyBook()));
						if ((order != null) && (order.get() != null) && (order.get() == OrderResult.SUCCESSFULLY_TAKEN)) {
							Mr.chargeCreditCard(ev.getMyCustomer(),priceF);//charge the customer(thread safe action)
							OrderReceipt myReceipt = new OrderReceipt(0, this.getName(), ev.getMyCustomer().getId(), ev.getMyBook(), priceF, currentTick, ev.getMyOrderTick(), proccessTick);
							Mr.file(myReceipt);//also adding to total earning
							complete(ev, myReceipt);


						}else{//NOT_IN_STOCK
							complete(ev,null);

						}

					} else {//there is not enough amount at the customer credit
						complete(ev, null);
					}
				}
			} else {//the book is not in stock
				complete(ev, null);

			}
		});

		subscribeBroadcast(TickBroadcast.class, br -> {
				currentTick = br.getTick();
		});

		subscribeBroadcast(TimeToGo.class, br-> {
			terminate();

		});

	}

}
