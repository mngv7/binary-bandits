import com.example.protrack.users.WarehouseUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WarehouseUserTest {
    private WarehouseUser user;

    @BeforeEach
    public void setUp() {
        user = new WarehouseUser(101, "TestFirst", "TestLast", "1234") {
        };
    }

    @Test
    public void testGetAccessLevel() {
        assertEquals("MEDIUM", user.getAccessLevel());
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
        assertEquals("1234", user.getPassword());
    }
}
