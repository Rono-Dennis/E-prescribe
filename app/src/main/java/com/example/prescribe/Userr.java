package com.example.prescribe;

public class Userr {
//    private String name;
//    private int age;
//    private String mail;
    private String location;
    private int shippment;

    public Userr(String location, int shippment) {
        this.location = location;
        this.shippment = shippment;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getShippment() {
        return shippment;
    }

    public void setShippment(int shippment) {
        this.shippment = shippment;
    }

    @Override
    public String toString() {
        return location;
    }
}
