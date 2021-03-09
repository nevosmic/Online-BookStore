package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TimeToGo;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService {
	private int mySpeed;
	private int myDuration;
	private int myTick;


	public TimeService(int speed, int duration) {
		super("TimeService");
		mySpeed = speed;
		myDuration = duration;
		myTick = 1;

	}

	@Override
	protected void initialize() {
		Timer timer = new Timer();
		TimerTask newTask = new TimerTask() {
			@Override
			public void run() {
				if (myTick < myDuration) {
					sendBroadcast(new TickBroadcast(myTick,false));
					myTick++;

				}
				else if(myTick == myDuration) {
					sendBroadcast(new TimeToGo()); // special broadcast to terminate
					timer.cancel();
					timer.purge();

				}
			}

		};
		timer.schedule(newTask, 0, mySpeed);//java's default TimeUnit is in milliseconds
			terminate();


	}

}
