import com.example.protrack.users.ManagerialUser;
import com.example.protrack.users.WarehouseUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseUserTest {

    private WarehouseUser warehouseUser;

    @BeforeEach
    public void SetUp() {
        warehouseUser = new WarehouseUser(1, "John", "Doe", Date.valueOf("1990-01-01"), "email@example.com", "1234567890", "Male", "password");
    }

    @Test
    public void testGetEmployeeId() {
        assertEquals(1, warehouseUser.getEmployeeId());
    }

    @Test
    public void testGetFirstName() {
        assertEquals("John", warehouseUser.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("Doe", warehouseUser.getLastName());
    }

    @Test
    public void testGetDob() {
        assertEquals(Date.valueOf("1990-01-01"), warehouseUser.getDob());
    }

    @Test
    public void testGetEmail() {
        assertEquals("email@example.com", warehouseUser.getEmail());
    }

    @Test
    public void testGetPhoneNo() {
        assertEquals("1234567890", warehouseUser.getPhoneNo());
    }

    @Test
    public void testGetGender() {
        assertEquals("Male", warehouseUser.getGender());
    }

    @Test
    public void testGetAccessLevel() {
        assertEquals("MEDIUM", warehouseUser.getAccessLevel());
    }

    @Test
    public void testNegativeEmployeeId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WarehouseUser(-1, "John", "Doe", new Date(System.currentTimeMillis()), "email@example.com", "1234567890", "M", "password");
        });
        assertEquals("Employee ID must be a positive integer", exception.getMessage());
    }

    @Test
    public void testShortPassword() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WarehouseUser(1, "John", "Doe", new Date(System.currentTimeMillis()), "email@example.com", "1234567890", "M", "test");
        });
        assertEquals("Password must be at least 6 characters long", exception.getMessage());
    }

    @Test
    public void testNullFirstName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WarehouseUser(1, null, "Doe", new Date(System.currentTimeMillis()), "email@example.com", "1234567890", "M", "password");
        });
        assertEquals("No field can be null", exception.getMessage());
    }

    @Test
    public void testNullEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WarehouseUser(1, "John", "Doe", new Date(System.currentTimeMillis()), null, "1234567890", "M", "password");
        });
        assertEquals("No field can be null", exception.getMessage());
    }
}