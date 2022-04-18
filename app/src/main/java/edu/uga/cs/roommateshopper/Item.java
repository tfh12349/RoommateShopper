package edu.uga.cs.roommateshopper;

public class Item {

    private String name;
    private int count;
    private String details;

    public Item(){
        this.name = "";
        this.count = 0;
        this.details = "";
    }

     public Item(String name, int count, String details){
        this.name = name;
        this.count = count;
        this.details = details;
     }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
