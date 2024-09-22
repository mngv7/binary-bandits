import com.example.protrack.users.ProductionUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ProductionUserTest {

    private ProductionUser productionUser;

    @BeforeEach
    public void setUp() {
        productionUser = new ProductionUser(1, "Bob", "Johnson", Date.valueOf("1995-05-15"), "bob@example.com", "0412345678", "Male", "securePassword");
    }

    @Test
    public void testGetEmployeeId() {
        assertEquals(1, productionUser.getEmployeeId());
    }

    @Test
    public void testGetFirstName() {
        assertEquals("Bob", productionUser.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("Johnson", productionUser.getLastName());
    }

    @Test
    public void testGetDob() {
        assertEquals(Date.valueOf("1995-05-15"), productionUser.getDob());
    }

    @Test
    public void testGetEmail() {
        assertEquals("bob@example.com", productionUser.getEmail());
    }

    @Test
    public void testGetPhoneNo() {
        assertEquals("0412345678", productionUser.getPhoneNo());
    }

    @Test
    public void testGetGender() {
        assertEquals("Male", productionUser.getGender());
    }

    @Test
    public void testGetAccessLevel() {
        assertEquals("LOW", productionUser.getAccessLevel());
    }

    @Test
    public void testValidInputs() {
        assertEquals(1, productionUser.getEmployeeId());
        assertEquals("Bob", productionUser.getFirstName());
        assertEquals("Johnson", productionUser.getLastName());
        assertEquals(Date.valueOf("1995-05-15"), productionUser.getDob());
        assertEquals("bob@example.com", productionUser.getEmail());
        assertEquals("0412345678", productionUser.getPhoneNo());
        assertEquals("LOW", productionUser.getAccessLevel());
    }

    @Test
    public void testNullEmployeeId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ProductionUser(null, "Bob", "Johnson", Date.valueOf("1995-05-15"), "bob@example.com", "0412345678", "Male", "securePassword");
        });
        assertEquals("No field can be null", exception.getMessage());
    }

    @Test
    public void testNullFirstName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ProductionUser(1, null, "Johnson", Date.valueOf("1995-05-15"), "bob@example.com", "0412345678", "Male", "securePassword");
        });
        assertEquals("No field can be null", exception.getMessage());
    }

    @Test
    public void testNullLastName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ProductionUser(1, "Bob", null, Date.valueOf("1995-05-15"), "bob@example.com", "0412345678", "Male", "securePassword");
        });
        assertEquals("No field can be null", exception.getMessage());
    }

    @Test
    public void testNullDob() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ProductionUser(1, "Bob", "Johnson", null, "bob@example.com", "0412345678", "Male", "securePassword");
        });
        assertEquals("No field can be null", exception.getMessage());
    }

    @Test
    public void testNullEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ProductionUser(1, "Bob", "Johnson", Date.valueOf("1995-05-15"), null, "0412345678", "Male", "securePassword");
        });
        assertEquals("No field can be null", exception.getMessage());
    }

    @Test
    public void testNullPassword() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ProductionUser(1, "Bob", "Johnson", Date.valueOf("1995-05-15"), "bob@example.com", "0412345678", "Male", null);
        });
        assertEquals("No field can be null", exception.getMessage());
    }
}