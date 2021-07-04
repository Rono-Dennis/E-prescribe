package com.example.prescribe.Model;

public class Orders {
  private   String address,  date, name, phone,uid, time, totalAmount,orderNo;
  private int city, pid;

    public Orders() {}

    public Orders(String address, String date, String name, String phone, String uid, String time, String totalAmount, String orderNo, int city, int pid) {
        this.address = address;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.uid = uid;
        this.time = time;
        this.totalAmount = totalAmount;
        this.orderNo = orderNo;
        this.city = city;
        this.pid = pid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
