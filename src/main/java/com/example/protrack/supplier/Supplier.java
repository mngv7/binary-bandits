package com.example.protrack.supplier;

public class Supplier {
    private int supplierId;
    private String name;
    private String email;
    private String phoneNumber;
    private String billingAddress;
    private String shippingAddress;
    private Double leadTime;

    // Constructor
    public Supplier(int supplierId, String name, String email, String phoneNumber, String billingAddress, String shippingAddress, Double leadTime) {
        this.supplierId = supplierId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
        this.leadTime = leadTime;
    }

    // Getters
    public int getSupplierId() {
        return supplierId;
    }

    // Setters
    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Double leadTime) {
        this.leadTime = leadTime;
    }
}
