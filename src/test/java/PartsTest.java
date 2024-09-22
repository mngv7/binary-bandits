import com.example.protrack.parts.Parts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PartsTest {

    private Parts part;

    @BeforeEach
    public void setUp() {
        part = new Parts(1, "Part Name", "Part Description", 1001, 9.99);
    }

    @Test
    public void testGetPartsId() {
        assertEquals(1, part.getPartsId());
    }

    @Test
    public void testGetName() {
        assertEquals("Part Name", part.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Part Description", part.getDescription());
    }

    @Test
    public void testGetSupplierId() {
        assertEquals(1001, part.getSupplierId());
    }

    @Test
    public void testGetCost() {
        assertEquals(9.99, part.getCost());
    }

    @Test
    public void testNullPartsId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(null, "Part", "Part Description", 1001, 9.99);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(1, null, "Part Description", 1001, 9.99);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullDescription() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(1, "Part", null, 1001, 9.99);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullSupplierId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(1, "Part", "Part Description", null, 9.99);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullCost() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(1, "Part", "Part Description", 1001, null);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNegativePartsId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(-1, "Part Name", "Part Description", 1, 10.0);
        });
        assertEquals("ID cannot be negative", exception.getMessage());
    }

    @Test
    public void testCostExceedsMaximum() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(1, "Part Name", "Part Description", 1, 10001.00);
        });
        assertEquals("Cost must be between 0 and 10,000.00", exception.getMessage());
    }
}