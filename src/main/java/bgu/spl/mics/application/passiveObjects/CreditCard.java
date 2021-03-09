package bgu.spl.mics.application.passiveObjects;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class CreditCard implements Serializable {
    private int number;
    private AtomicInteger myAmount;

    public CreditCard(int n, int a){
        this.number = n;
        this.myAmount = new AtomicInteger(a);
    }
    public CreditCard(JsonObject j){
        this.number = j.get("number").getAsInt();
        this.myAmount = new AtomicInteger(j.get("amount").getAsInt());
    }

    public int getNumber() {
        return number;
    }

    public AtomicInteger getAmount() {
        return myAmount;
    }

    public void setAmount(int amount) {//atomic action
        int val;
        do{
            val= this.myAmount.get();
        }while(!myAmount.compareAndSet(val,amount));
        //if val is not what i expect do again
    }
}
