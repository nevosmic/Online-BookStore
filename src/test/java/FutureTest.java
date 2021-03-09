/*
import bgu.spl.mics.Future;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FutureTest {
    private Future myFuture;

    @Before
    public void setUp() throws Exception {
        this.myFuture = creatFuture();
    }

    public Future creatFuture() {

        return new Future();
    }



    @Test
    public void get() throws Exception {
        assertNotNull(myFuture.get());

    }

    @Test
    public void resolve() {
        this.myFuture.resolve("SUCCESSFULLY_TAKEN");
        assertTrue(this.myFuture.isDone());
    }

    @Test
    public void isDone() {
        boolean done = this.myFuture.isDone();
        if(this.myFuture.get(0, TimeUnit.SECONDS)==null){
            assertTrue(done==false);
        }
        else{
            assertTrue(done==true);
        }

    }

    @Test
    public void get1() {
        assertNull(myFuture.get(0,TimeUnit.SECONDS));
        myFuture.resolve("done");
        assertNotNull(myFuture.get(0, TimeUnit.SECONDS));

    }
}*/
