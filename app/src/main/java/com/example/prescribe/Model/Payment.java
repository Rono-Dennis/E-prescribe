package com.example.prescribe.Model;

public class Payment {

    public String  phoneNumber,dateTime,userId,amountToPay,paidAmount,balance, orderNo;

    public Payment() {}

    @Override
    public String toString() {
        return "Payment{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", userId='" + userId + '\'' +
                ", amountToPay='" + amountToPay + '\'' +
                ", paidAmount='" + paidAmount + '\'' +
                ", balance='" + balance + '\'' +
                ", orderNo='" + orderNo + '\'' +
                '}';
    }

    public Payment(String phoneNumber, String dateTime, String userId, String amountToPay, String paidAmount, String balance, String orderNo) {
        this.phoneNumber = phoneNumber;
        this.dateTime = dateTime;
        this.userId = userId;
        this.amountToPay = amountToPay;
        this.paidAmount = paidAmount;
        this.balance = balance;
        this.orderNo = orderNo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(String amountToPay) {
        this.amountToPay = amountToPay;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
