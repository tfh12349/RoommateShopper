package edu.uga.cs.roommateshopper;

import java.util.ArrayList;
import java.util.List;

public class Purchase {
    private String userName;
    private double price;
    private List<Item> items;

    public Purchase(){
        userName = "";
        price = 0;
        items = new ArrayList<Item>();
    }

    public Purchase(String name, double priceNum, List<Item> itemList){
        userName = name;
        price = priceNum;
        items = itemList;
    }

    public double getPrice() {
        return price;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getUserName() {
        return userName;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
