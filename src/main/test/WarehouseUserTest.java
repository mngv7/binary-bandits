import com.example.protrack.users.WarehouseUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WarehouseUserTest {
    private WarehouseUser user;

    @BeforeEach
    public void setUp() {
        user = new WarehouseUser(104, "Diana", "White", Date.valueOf("1987-03-12"), "diana.white@example.com", "0400123093", "Female", "dianapass");
    }

    @Test
    public void testGetAccessLevel() {
        assertEquals("MEDIUM", user.getAccessLevel());
    }

    @Test
    public void testGetEmployeeId() {
        assertEquals(104, user.getEmployeeId());
    }

    @Test
    public void testGetFirstName() {
        assertEquals("Diana", user.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("White", user.getLastName());
    }

    @Test
    public void testGetPassword() {
        assertEquals("dianapass", user.getPassword());
    }
}
