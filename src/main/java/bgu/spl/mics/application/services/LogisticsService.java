package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

/**
 * Logistic service in charge of delivering books that have been purchased to customers.
 * Handles {@link DeliveryEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LogisticsService extends MicroService {

	public LogisticsService(int i) {
		super("LogisticsService "+i);
		// TODO Implement this
	}

	@Override
	protected void initialize() {
		subscribeEvent(DeliveryEvent.class,ev-> {
			Future<Future<DeliveryVehicle>> myVehicleF = sendEvent(new AcquireEvent(ev.getMyCustomer().getName()));
			if (myVehicleF != null ) {
				Future<DeliveryVehicle> myFCar = myVehicleF.get();
				if (myFCar != null) {
					DeliveryVehicle myCar = myFCar.get();
					if (myCar != null) {
						//vehicle acquired successfully
						myCar.deliver(ev.getMyCustomer().getAddress(), ev.getMyCustomer().getDistance());//thread is sleeping,after this line vehicle needs to be free
						sendEvent(new FreeVehicle(myCar));//we release that vehicle
					}
				}

			}

		});
		subscribeBroadcast(TimeToGo.class, br-> {
			terminate();

		});
		
	}

}
