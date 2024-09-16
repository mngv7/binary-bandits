package com.example.protrack.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface IUsersDAO {
    void createTable();
    List<AbstractUser> getAllUsers();
    AbstractUser mapResultSetToUser(ResultSet resultSet) throws SQLException;
    HashMap<Integer, ManagerialUser> getManagerialUsers() throws SQLException;
    List<WarehouseUser> getWarehouseUsers() throws SQLException;
    HashMap<Integer, ProductionUser> getProductionUsers() throws SQLException;
    void dropTable();
    String getPasswordByFirstName(String firstname);
    String getAccessLevelByFirstName(String firstname);
    void newUser(AbstractUser user);
    boolean isTableEmpty();
}
