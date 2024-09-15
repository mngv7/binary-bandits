package com.example.protrack.applicationpages;
import java.util.List;  // Ensure this import is correct


import com.example.protrack.users.AbstractUser;
import com.example.protrack.users.UsersDAO;

import java.awt.*;
import java.sql.SQLException;

public class EmployeesController {

    public void initialize() {
        UsersDAO usersDAO = new UsersDAO();
        List<AbstractUser> allUsers = usersDAO.getAllUsers();

        // For loop to print user details
        for (AbstractUser user : allUsers) {
            System.out.println("Employee ID: " + user.getEmployeeId());
            System.out.println("First Name: " + user.getFirstName());
            System.out.println("Last Name: " + user.getLastName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Phone Number: " + user.getPhoneNo());
            System.out.println("Access Level: " + user.getAccessLevel());
            System.out.println("-----------------------");
        }
    }

}
