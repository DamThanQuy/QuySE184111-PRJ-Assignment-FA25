package Models;

import java.time.LocalDateTime;

public class Order {
    private int orderID;
    private int customerID;
    private LocalDateTime orderDate;
    private String shipAddress;
    private String phoneNumber;

    public Order() {}

    public Order(int orderID, int customerID, LocalDateTime orderDate, String shipAddress, String phoneNumber) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.orderDate = orderDate;
        this.shipAddress = shipAddress;
        this.phoneNumber = phoneNumber;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }


    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
