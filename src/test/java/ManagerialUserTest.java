import com.example.protrack.users.ManagerialUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerialUserTest {

    private ManagerialUser managerialUser;

    @BeforeEach
    public void setUp() {
        managerialUser = new ManagerialUser(1, "Alice", "Smith", Date.valueOf("1990-01-01"), "alice@example.com", "0412345678", "Female", "securePassword");
    }

    @Test
    public void testGetAccessLevel() {
        assertEquals("HIGH", managerialUser.getAccessLevel());
    }

    @Test
    public void testValidManagerialUser() {
        assertEquals(1, managerialUser.getEmployeeId());
        assertEquals("Alice", managerialUser.getFirstName());
        assertEquals("Smith", managerialUser.getLastName());
        assertEquals(Date.valueOf("1990-01-01"), managerialUser.getDob());
        assertEquals("alice@example.com", managerialUser.getEmail());
        assertEquals("0412345678", managerialUser.getPhoneNo());
        assertEquals("Female", managerialUser.getGender());
        assertEquals("HIGH", managerialUser.getAccessLevel());
    }

    @Test
    public void testNullEmployeeId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ManagerialUser(null, "Alice", "Smith", Date.valueOf("1990-01-01"), "alice@example.com", "0412345678", "Female", "securePassword");
        });
        assertEquals("None of the fields can be null", exception.getMessage());
    }

    @Test
    public void testNullFirstName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ManagerialUser(1, null, "Smith", Date.valueOf("1990-01-01"), "alice@example.com", "0412345678", "Female", "securePassword");
        });
        assertEquals("None of the fields can be null", exception.getMessage());
    }

    @Test
    public void testNullLastName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ManagerialUser(1, "Alice", null, Date.valueOf("1990-01-01"), "alice@example.com", "0412345678", "Female", "securePassword");
        });
        assertEquals("None of the fields can be null", exception.getMessage());
    }

    @Test
    public void testNullDob() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ManagerialUser(1, "Alice", "Smith", null, "alice@example.com", "0412345678", "Female", "securePassword");
        });
        assertEquals("None of the fields can be null", exception.getMessage());
    }

    @Test
    public void testNullEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ManagerialUser(1, "Alice", "Smith", Date.valueOf("1990-01-01"), null, "0412345678", "Female", "securePassword");
        });
        assertEquals("None of the fields can be null", exception.getMessage());
    }

    @Test
    public void testInvalidEmailNoAt() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ManagerialUser(1, "Alice", "Smith", Date.valueOf("1990-01-01"), "aliceexample.com", "0412345678", "Female", "securePassword");
        });
        assertEquals("Email must contain an '@' and a '.'", exception.getMessage());
    }

    @Test
    public void testInvalidEmailNoDot() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ManagerialUser(1, "Alice", "Smith", Date.valueOf("1990-01-01"), "alice@com", "0412345678", "Female", "securePassword");
        });
        assertEquals("Email must contain an '@' and a '.'", exception.getMessage());
    }

    @Test
    public void testNullPassword() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ManagerialUser(1, "Alice", "Smith", Date.valueOf("1990-01-01"), "alice@example.com", "0412345678", "Female", null);
        });
        assertEquals("None of the fields can be null", exception.getMessage());
    }

    @Test
    public void testNegativeEmployeeId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ManagerialUser(-1, "John", "Doe", new Date(System.currentTimeMillis()), "email@example.com", "1234567890", "M", "password");
        });
        assertEquals("Employee ID must be a positive integer", exception.getMessage());
    }

    @Test
    public void testShortPassword() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ManagerialUser(1, "John", "Doe", new Date(System.currentTimeMillis()), "email@example.com", "1234567890", "M", "test");
        });
        assertEquals("Password must be at least 6 characters long", exception.getMessage());
    }
}