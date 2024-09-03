import com.example.protrack.users.WarehouseUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WarehouseUserTest {
    private WarehouseUser user;

    @BeforeEach
    public void setUp() {
        user = new WarehouseUser(101, "TestFirst", "TestLast", "1234") {
        };
    }

    @Test
    public void testGetAccessLevel() {
        assertEquals("LOW", user.getAccessLevel());
    }

    @Test
    public void testGetEmployeeId() {
        assertEquals(101, user.getEmployeeId());
    }

    @Test
    public void testGetFirstName() {
        assertEquals("TestFirst", user.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("TestLast", user.getLastName());
    }

    @Test
    public void testGetPassword() {
        assertTrue(BCrypt.checkpw("password", user.getPassword()));
    }
}
