package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.Vector;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourceHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService{
	private ResourcesHolder myR;
	private Vector<Future<DeliveryVehicle>> futureThatWait;
	public ResourceService(int i) {
		super("ResourceService "+i);
		myR = ResourcesHolder.getInstance();
		futureThatWait = new Vector<>();
	}

	@Override
	protected void initialize() {
		subscribeEvent(AcquireEvent.class,ev ->{

			Future<DeliveryVehicle> myVehicleF= myR.acquireVehicle();
			if(!(myVehicleF.isDone())){ //if we receive a future that was not resolved yet
				futureThatWait.add(myVehicleF);
			}

				complete(ev,myVehicleF);


		});

		subscribeEvent(FreeVehicle.class,ev->{

			myR.releaseVehicle(ev.getMyVehicle());

		});

		subscribeBroadcast(TimeToGo.class, br-> {
			for(int i = 0; i < futureThatWait.size(); i++){// all the unresolved futures now resolved null before terminate
				futureThatWait.get(i).resolve(null);
			}
			terminate();

		});
	}

}
