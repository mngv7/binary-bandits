package com.example.protrack.users;

import java.sql.Date;

/**
 * Abstract class representing a user in the system.
 * This class serves as a base for different types of users with common attributes
 * such as employee ID, name, date of birth, email, phone number, gender, and password.
 */
public abstract class AbstractUser {
    // Protected fields for user attributes
    protected Integer employeeId;
    protected String firstName;
    protected String lastName;
    protected Date dob;
    protected String email;
    protected String phoneNo;
    protected String gender;
    protected String password;

    /**
     * Constructor to initialize an AbstractUser object with the provided attributes.
     *
     * @param employeeId The employee ID of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param dob The date of birth of the user.
     * @param email The email address of the user.
     * @param phoneNo The phone number of the user.
     * @param gender The gender of the user.
     * @param password The password of the user.
     */
    public AbstractUser(Integer employeeId, String firstName, String lastName, Date dob, String email, String phoneNo, String gender, String password) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.email = email;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.password = password;
    }

    /**
     * Retrieves the employee ID of the user.
     *
     * @return The employee ID.
     */
    public Integer getEmployeeId() {
        return employeeId;
    }

    /**
     * Retrieves the first name of the user.
     *
     * @return The first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Retrieves the last name of the user.
     *
     * @return The last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Retrieves the date of birth of the user.
     *
     * @return The date of birth.
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Retrieves the email address of the user.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the phone number of the user.
     *
     * @return The phone number.
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * Retrieves the gender of the user.
     *
     * @return The gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Abstract method to retrieve the access level of the user.
     * This method must be implemented by any concrete class extending AbstractUser.
     *
     * @return The access level of the user.
     */
    public abstract String getAccessLevel();
}
