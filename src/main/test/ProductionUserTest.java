import com.example.protrack.users.ProductionUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductionUserTest {
    private ProductionUser user;

    @BeforeEach
    public void setUp() {
        user = new ProductionUser(106, "Frank", "Miller", Date.valueOf("1989-12-09"), "frank.miller@example.com", "0400192123", "Male", "frankpass");
    }

    @Test
    public void testGetAccessLevel() {
        assertEquals("LOW", user.getAccessLevel());
    }

    @Test
    public void testGetEmployeeId() {
        assertEquals(106, user.getEmployeeId());
    }

    @Test
    public void testGetFirstName() {
        assertEquals("Frank", user.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("Miller", user.getLastName());
    }

    @Test
    public void testGetPassword() {
        assertEquals("frankpass", user.getPassword());
    }
}