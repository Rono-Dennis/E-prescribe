package com.example.prescribe.Model;

public class Orders {
  private   String address,  date, name, phone,uid, time, totalAmount,orderNo, pid, city;


    public Orders() {}


    public Orders(String address, String date, String name, String phone, String uid, String time, String totalAmount, String orderNo, String pid, String city) {
        this.address = address;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.uid = uid;
        this.time = time;
        this.totalAmount = totalAmount;
        this.orderNo = orderNo;
        this.pid = pid;
        this.city = city;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "address='" + address + '\'' +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", uid='" + uid + '\'' +
                ", time='" + time + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", pid='" + pid + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
