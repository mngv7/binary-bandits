import com.example.protrack.users.ManagerialUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManagerialUserTest {
    private ManagerialUser user;

    @BeforeEach
    public void setUp() {
        user = new ManagerialUser(100, "John", "Doe", Date.valueOf("1985-01-01"), "john.doe@example.com", "0400125123", "Male", "password");
    }

    @Test
    public void testGetAccessLevel() {
        assertEquals("HIGH", user.getAccessLevel());
    }

    @Test
    public void testGetEmployeeId() {
        assertEquals(100, user.getEmployeeId()); // Changed from 101 to 100 to match initialization
    }

    @Test
    public void testGetFirstName() {
        assertEquals("John", user.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("Doe", user.getLastName()); // Changed from "TestLast" to "Doe" to match initialization
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", user.getPassword());
    }
}
