import com.example.protrack.users.ManagerialUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManagerialUserTest {
    private ManagerialUser user;

    @BeforeEach
    public void setUp() {
        user = new ManagerialUser(101, "TestFirst", "TestLast", "Password@1234") {
        };
    }

    @Test
    public void testGetAccessLevel() {
        assertEquals("HIGH", user.getAccessLevel());
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
        assertEquals("Password@1234", user.getPassword());
    }
}
