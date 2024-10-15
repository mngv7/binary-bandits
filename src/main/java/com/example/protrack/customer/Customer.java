package com.example.protrack.customer;

import com.example.protrack.workorder.WorkOrder;

import java.util.ArrayList;

/**
 * The {@code Customer} class represents a customer.
 * The Customer class contains details about the customer.
 */
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

    /**
     * Constructs a new Customer object with the bellow parameters.
     *
     * @param customerId      the unique ID of the customer, must not be null or negative
     * @param firstName       the first name of the customer, must not be null
     * @param lastName        the last name of the customer, must not be null
     * @param email           the email address of the customer, must not be null and must contain '@' and '.'
     * @param phoneNumber     the phone number of the customer, can be null, but must be exactly 10 digits if provided
     * @param billingAddress  the billing address of the customer
     * @param shippingAddress the shipping address of the customer
     * @param status          the status of the customer, must be either "Active" or "Inactive"
     * @throws IllegalArgumentException if any mandatory field is null, email is invalid, or status is incorrect
     */
    public Customer(Integer customerId, String firstName, String lastName, String email, String phoneNumber,
                    String billingAddress, String shippingAddress, String status) {
        if (customerId == null || customerId < 0 || firstName == null || lastName == null || email == null || status == null) {
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

    /**
     * Gets the customer ID.
     *
     * @return the customer ID
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID.
     *
     * @param customerId the customer ID to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the first name of the customer.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the customer.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the customer.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the customer.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email address of the customer.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the customer.
     *
     * @param email the email address to set, must not exceed 255 characters and must contain '@' and '.'
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number of the customer.
     *
     * @return the phone number, can be null
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the customer.
     *
     * @param phoneNumber the phone number to set, must be 10 digits if provided, can be null
     * @throws IllegalArgumentException if the phone number exceeds 11 characters
     */
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() > 11) {
            throw new IllegalArgumentException("Phone number cannot exceed 11 characters.");
        }
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the billing address of the customer.
     *
     * @return the billing address
     */
    public String getBillingAddress() {
        return billingAddress;
    }

    /**
     * Sets the billing address of the customer.
     *
     * @param billingAddress the billing address to set
     */
    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    /**
     * Gets the shipping address of the customer.
     *
     * @return the shipping address
     */
    public String getShippingAddress() {
        return shippingAddress;
    }

    /**
     * Sets the shipping address of the customer.
     *
     * @param shippingAddress the shipping address to set
     */
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
     * Gets the status of the customer.
     *
     * @return the status, either "Active" or "Inactive"
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the customer.
     *
     * @param status the status to set, must be either "Active" or "Inactive"
     */
    public void setStatus(String status) {
        if (!"Active".equals(status) && !"Inactive".equals(status)) {
            throw new IllegalArgumentException("Status must be 'Active' or 'Inactive'");
        }
        this.status = status;
    }

    /**
     * Gets the list of work orders associated with the customer.
     *
     * @return the list of work orders, can be null
     */
    public ArrayList<WorkOrder> getOrders() {
        return orders;
    }

    /**
     * Sets the list of work orders for the customer.
     *
     * @param orders the list of work orders to set, can be null
     */
    public void setOrders(ArrayList<WorkOrder> orders) {
        this.orders = orders;
    }

    /**
     * Returns a string representation of the customer.
     *
     * @return a string in the format "firstName lastName, shippingAddress"
     */
    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }
}
