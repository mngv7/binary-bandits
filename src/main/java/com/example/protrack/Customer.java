package com.example.protrack;

import com.example.protrack.workorder.WorkOrder;

import java.util.ArrayList;

public class Customer {

    private Integer customerId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer phoneNumber;
    private String billingAddress;
    private String shippingAddress;
    private String status;
    private ArrayList<WorkOrder> orders;

    public Customer(Integer customerId, String firstName, String lastName, String email, Integer phoneNumber, String billingAddress, String shippingAddress, String status, ArrayList<WorkOrder> orders) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
        this.status = status;
        this.orders = orders;
    }

    // Getters and Setters
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<WorkOrder> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<WorkOrder> orders) {
        this.orders = orders;
    }
}
