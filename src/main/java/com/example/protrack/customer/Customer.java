package com.example.protrack.customer;

import com.example.protrack.workorder.WorkOrder;

import java.util.ArrayList;

public class Customer {

    private Integer customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber; // Can be null
    private String billingAddress;
    private String shippingAddress;
    private String status; // Can only be "Active" or "Inactive"
    private ArrayList<WorkOrder> orders; // Can be null

    public Customer(Integer customerId, String firstName, String lastName, String email, String phoneNumber, String billingAddress, String shippingAddress, String status) {
        if (customerId == null || customerId <= 0 || firstName == null || lastName == null || email == null || status == null) {
            throw new IllegalArgumentException("No field can be null");
        }
        if (email.length() > 255) {
            throw new IllegalArgumentException("Email must not exceed 255 characters");
        }
        if (phoneNumber != null && (phoneNumber.length() != 10 || !phoneNumber.matches("\\d{10}"))) {
            throw new IllegalArgumentException("Phone number must be 10 digits long");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Email must contain '@' and '.'");
        }
        if (!status.equals("Active") && !status.equals("Inactive")) {
            throw new IllegalArgumentException("Status must be either 'Active' or 'Inactive'");
        }
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        setPhoneNumber(phoneNumber);
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
        setStatus(status);
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() > 11) {
            throw new IllegalArgumentException("Phone number cannot exceed 11 characters.");
        }
        this.phoneNumber = phoneNumber; // Can be null
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
        if (!"Active".equals(status) && !"Inactive".equals(status)) {
            throw new IllegalArgumentException("Status must be 'Active' or 'Inactive'");
        }
        this.status = status;
    }

    public ArrayList<WorkOrder> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<WorkOrder> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + ", " + this.shippingAddress;
    }
}