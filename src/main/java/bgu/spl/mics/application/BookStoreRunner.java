package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.*;
import javax.xml.bind.Element;
import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static AtomicInteger globalCounter = new AtomicInteger(0);
    private static MoneyRegister myMoney = MoneyRegister.getInstance();


    public static void main(String[] args) {
        Vector<MicroService> microServices = new Vector<>();
        JsonParser parser = new JsonParser();
        Vector<Thread> myTVec = new Vector<>();

        try {


            JsonObject json = (JsonObject) parser.parse(new FileReader(args[0]));
            JsonArray booksArray = json.get("initialInventory").getAsJsonArray();
            BookInventoryInfo[] books = new BookInventoryInfo[booksArray.size()];
            for (int i = 0; i < booksArray.size(); i++) {
                books[i] = new BookInventoryInfo(booksArray.get(i).getAsJsonObject());
            }

            JsonArray vehicle = json.get("initialResources").getAsJsonArray().get(0).getAsJsonObject().get("vehicles").getAsJsonArray();
            DeliveryVehicle[] vehicles = new DeliveryVehicle[vehicle.size()];
            for (int i = 0; i < vehicle.size(); i++) {
                int licenss = vehicle.get(i).getAsJsonObject().get("license").getAsInt();
                int speed = vehicle.get(i).getAsJsonObject().get("speed").getAsInt();
                vehicles[i] = new DeliveryVehicle(licenss, speed);
            }
            JsonArray customers = json.get("services").getAsJsonObject().get("customers").getAsJsonArray();
            Vector<Customer> customers1 = new Vector<>();
            for (int i = 0; i < customers.size(); i++) {
                customers1.add(new Customer(customers.get(i).getAsJsonObject()));
            }


            int duration = json.get("services").getAsJsonObject().get("time").getAsJsonObject().get("duration").getAsInt();
            int speed = json.get("services").getAsJsonObject().get("time").getAsJsonObject().get("speed").getAsInt();
            int numOfSellingServ = json.get("services").getAsJsonObject().get("selling").getAsInt();
            int numOfInventoryServ = json.get("services").getAsJsonObject().get("inventoryService").getAsInt();
            int numOfLogisticsServ = json.get("services").getAsJsonObject().get("logistics").getAsInt();
            int numOfResourcesServ = json.get("services").getAsJsonObject().get("resourcesService").getAsInt();

            //add the customers:
            for (int i = 0; i < customers1.size(); i++) {
                microServices.add(new APIService(i, customers1.get(i)));
            }

            //create selling service
            for (int i = 0; i < numOfSellingServ; i++) {
                microServices.add(new SellingService(i));
            }
            //create Inventory Service
            for (int i = 0; i < numOfInventoryServ; i++) {
                microServices.add(new InventoryService(i));
            }
            //create Logistics Service
            for (int i = 0; i < numOfLogisticsServ; i++) {
                microServices.add(new LogisticsService(i));
            }
            //create Resources Service
            for (int i = 0; i < numOfResourcesServ; i++) {
                microServices.add(new ResourceService(i));
            }

            //load books to the Inventory
            Inventory.getInstance().load(books);

            //load vehicles to R
            ResourcesHolder.getInstance().load(vehicles);

            //initial the threads
            for (int i = 0; i < microServices.size(); i = i + 1) {
                Thread myT = new Thread(microServices.get(i));
                myT.start();
                myTVec.add(myT);
            }
            while (globalCounter.get() < microServices.size() - 1) {//waiting for all the services to register
            }
            //create the only Time service
            Thread timerT = new Thread(new TimeService(speed, duration));
            timerT.start();

            //Before printing we check that all threads were terminate
            for(Thread t : myTVec){
                try {
                    t.join();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //prints the MoneyRegister
           printMoneyRegisterToFile(args[4]);


            //prints the Inventory
            Inventory inv = Inventory.getInstance();
            inv.printInventoryToFile(args[2]);

            //prints the orderReceipts
            MoneyRegister.getInstance().printOrderReceipts(args[3]);

            //prints the customers
            HashMap<Integer,Customer> myCustomers = new HashMap<>();
            for(int i = 0;i < customers1.size();i = i+1){
                myCustomers.put(customers1.get(i).getId(),customers1.get(i));
            }
            try {
                FileOutputStream f = new FileOutputStream(args[1]);
                ObjectOutputStream o = new ObjectOutputStream(f);

                o.writeObject(myCustomers);
                o.close();
                f.close();

            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("Error initializing stream");
            }



        }
        catch (FileNotFoundException e) {
            System.out.println(Thread.currentThread().getName() +" FileNotFoundException");}
        catch (IOException e){
            System.out.println(Thread.currentThread().getName() +" IOException");}


    }


    public static void printMoneyRegisterToFile(String filename ){
        try {

            FileOutputStream f = new FileOutputStream(filename);
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(myMoney);
            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

}
