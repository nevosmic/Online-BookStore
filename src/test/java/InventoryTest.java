/*
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.OrderResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class InventoryTest {

    private Inventory inventory;


    @Before
    public void setUp() throws Exception {
        this.inventory = createInventory();
    }
    public Inventory createInventory(){

       return Inventory.getInstance();
    }


    @Test
    public void getInstance() {
    }

    @Test
    public void load() throws Exception {//check if the books are really there
        BookInventoryInfo a = new BookInventoryInfo("When harry met Sally",new AtomicInteger(2),10);
        BookInventoryInfo b= new BookInventoryInfo("Zili & Gili",new AtomicInteger(8),11);
        BookInventoryInfo[] array= {a,b};
        this.inventory.load(array);
        assertTrue(this.inventory.checkAvailabiltyAndGetPrice("When harry met Sally")==10);
        assertTrue(this.inventory.checkAvailabiltyAndGetPrice("Zili & Gili")==11);

    }

    @Test
    public void takeNOT_IN_STOCK()throws Exception {

       OrderResult temp = inventory.take("Little Michal");
        assertEquals("NOT_IN_STOCK",temp);
    }
    @Test
    public void takeSUCCESSFULLY_TAKEN() throws Exception {
        BookInventoryInfo b = new BookInventoryInfo("Lion King",new AtomicInteger(1),10);
        BookInventoryInfo[] array= {b};
        inventory.load(array);
        OrderResult temp = inventory.take(b.getBookTitle());
        assertEquals("SUCCESSFULLY_TAKEN",temp);
    }

    @Test
    public void checkAvailabiltyAndGetPriceAVALIABLE() throws Exception {
        BookInventoryInfo b = new BookInventoryInfo("Pinokio",new AtomicInteger(1),10);
        BookInventoryInfo[] array= {b};
        inventory.load(array);
        assertTrue(this.inventory.checkAvailabiltyAndGetPrice("Pinokio")==10);

    }
    @Test
    public void checkAvailabiltyAndGetPriceNOTAVALIABLE() throws Exception{
        assertTrue(this.inventory.checkAvailabiltyAndGetPrice("Pluto")==-1);
    }

    @Test
    public void printInventoryToFile() {//how to do1!?!?!?
    }
}
*/
