package com.example.protrack.supplier;

/**
 * Represents a Supplier in the system with details
 */
public class Supplier {
    private int supplierId;
    private String name;
    private String email;
    private String phoneNumber;
    private String billingAddress;
    private String shippingAddress;
    private Double leadTime;

    /**
     * Constructs a Supplier with the specified details.
     *
     * @param supplierId      the unique identifier for the supplier
     * @param name            the name of the supplier
     * @param email           the email address of the supplier
     * @param phoneNumber     the phone number of the supplier
     * @param billingAddress  the billing address of the supplier
     * @param shippingAddress the shipping address of the supplier
     * @param leadTime        the lead time for deliveries in days
     */
    public Supplier(int supplierId, String name, String email, String phoneNumber,
                    String billingAddress, String shippingAddress, Double leadTime) {
        this.supplierId = supplierId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
        this.leadTime = leadTime;
    }

    /**
     * Gets the supplier's unique identifier.
     *
     * @return the supplier ID
     */
    public int getSupplierId() {
        return supplierId;
    }

    /**
     * Sets the supplier's unique identifier.
     *
     * @param supplierId the new supplier ID
     */
    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * Gets the supplier's name.
     *
     * @return the supplier's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the supplier's name.
     *
     * @param name the new supplier name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the supplier's email address.
     *
     * @return the supplier's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the supplier's email address.
     *
     * @param email the new supplier email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the supplier's phone number.
     *
     * @return the supplier's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the supplier's phone number.
     *
     * @param phoneNumber the new supplier phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the supplier's billing address.
     *
     * @return the supplier's billing address
     */
    public String getBillingAddress() {
        return billingAddress;
    }

    /**
     * Sets the supplier's billing address.
     *
     * @param billingAddress the new supplier billing address
     */
    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    /**
     * Gets the supplier's shipping address.
     *
     * @return the supplier's shipping address
     */
    public String getShippingAddress() {
        return shippingAddress;
    }

    /**
     * Sets the supplier's shipping address.
     *
     * @param shippingAddress the new supplier shipping address
     */
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
     * Gets the lead time for deliveries.
     *
     * @return the lead time in days
     */
    public Double getLeadTime() {
        return leadTime;
    }

    /**
     * Sets the lead time for deliveries.
     *
     * @param leadTime the new lead time in days
     */
    public void setLeadTime(Double leadTime) {
        this.leadTime = leadTime;
    }
}
