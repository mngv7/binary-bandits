package com.example.protrack.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IUsersDAO {
    void createTable();

    List<AbstractUser> getAllUsers();

    AbstractUser mapResultSetToUser(ResultSet resultSet) throws SQLException;

    List<ManagerialUser> getManagerialUsers() throws SQLException;

    List<WarehouseUser> getWarehouseUsers() throws SQLException;

    List<ProductionUser> getProductionUsers() throws SQLException;

    void dropTable();

    AbstractUser getUserById(Integer employeeId);

    Integer getEmployeeIdByFullName(String fullName) throws SQLException;

    void newUser(AbstractUser user);

    boolean isTableEmpty();
}
