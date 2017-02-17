package edu.csulb.android.tacopronto;

/**
 * Created by Gaurav on 2/17/2017.
 */

public class Item {

    private String name;
    private float price;

    public Item(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
