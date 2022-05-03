package edu.uga.cs.roommateshopper;

public class Price {
    private String userName;
    private double priceTotal;

    public Price(){
        userName = "";
        priceTotal = 0;
    }

    public Price(String user, double price){
        userName = user;
        priceTotal = price;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public String getUserName() {
        return userName;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public void addToPrice(double addMe){
        priceTotal += addMe;
    }
}
