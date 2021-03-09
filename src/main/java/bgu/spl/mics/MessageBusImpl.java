package bgu.spl.mics;


import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private ConcurrentHashMap<Class<? extends Event>, LinkedBlockingQueue<MicroService>> eventToMicro;
	private ConcurrentHashMap<MicroService,LinkedBlockingQueue<Message>> microToQueue;
	private ConcurrentHashMap<Class<? extends Broadcast>,ConcurrentLinkedQueue<MicroService>> broadcastToMicro;
	private ConcurrentHashMap< Event,Future>  eventToFuture;
		private static MessageBusImpl messageBusImplInstance = null;

	private MessageBusImpl (){
		eventToMicro = new ConcurrentHashMap<>();
		microToQueue = new ConcurrentHashMap<>();
		broadcastToMicro = new ConcurrentHashMap<>();
		eventToFuture = new ConcurrentHashMap<>();


	}
	public static MessageBusImpl getInstance() {//there is only one thread which initializes all the instances of the classes in the program
		//TODO: Implement this
		if(messageBusImplInstance == null) {
			messageBusImplInstance = new MessageBusImpl();
		}
		return messageBusImplInstance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		try{
			eventToMicro.putIfAbsent(type,new LinkedBlockingQueue<>());
			(eventToMicro.get(type)).put(m);//adding the given mc to the queue associated to the given event

		}
		catch (Exception e ){}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(broadcastToMicro.get(type) == null){
			broadcastToMicro.put(type,new ConcurrentLinkedQueue<>());
		}

		broadcastToMicro.get(type).add(m);//add the mc to the vector associated to this broadcast

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		synchronized (e) {
			eventToFuture.get(e).resolve(result);//change the result of the future
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		ConcurrentLinkedQueue<MicroService> v ;
		v = broadcastToMicro.get(b.getClass());//get all the mc that registered
		if(v != null) {//null if no microS is register to this broadcast
			for (MicroService m : v)  {
				LinkedBlockingQueue q = microToQueue.get(m);
				if (q != null)
					q.add(b);//add the broadcast to the mc queue
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {// in a round robin fashion

		if (eventToMicro.get(e.getClass()) == null) {//if there is no microS subscribed to this event
			return null;
		}

		Future<T> output = new Future<>();
		eventToFuture.put(e, output);
		MicroService currentMic = eventToMicro.get(e.getClass()).poll();//poll the head of the queue
		try {
			eventToMicro.get(e.getClass()).put(currentMic);//add back to the queue
		}
		catch (Exception e2) {}

		LinkedBlockingQueue tempQ = microToQueue.get(currentMic);
		synchronized (tempQ) {//when doing unregister we dont want any thread to work on this Q
			try {
				(microToQueue.get(currentMic)).put(e);// C-hashmap wouldn't allow 2 threads to access this microS and his queue
			}
			catch (Exception e1) {}

			return output;
		}

	}
	@Override
	public void register(MicroService m) {

		microToQueue.putIfAbsent(m,new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
		//also remove this microS from eventToMicro and broadcastToMicro :
		for (HashMap.Entry<Class<? extends Event>, LinkedBlockingQueue<MicroService>> entry : eventToMicro.entrySet()) {
			(entry.getValue()).remove(m);//removes m from each vector
		}
		for (HashMap.Entry<Class<? extends Broadcast>,ConcurrentLinkedQueue<MicroService>> entry : broadcastToMicro.entrySet()) {
			(entry.getValue()).remove(m);//removes m from each vector
		}
		LinkedBlockingQueue<Message> myQ = microToQueue.get(m);
		microToQueue.remove(m);//remove this microS's queue
		synchronized (myQ) {
			for (Message message : myQ) {// resolved the futures that didnt resolved yet, with null
				if (!(message instanceof Broadcast)) {
					eventToFuture.get(message).resolve(null);
				}
			}
		}



	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		Message output = null;
		try{
			output = microToQueue.get(m).take();
		}
		catch(InterruptedException e){}

		return output;
	}

	

}
