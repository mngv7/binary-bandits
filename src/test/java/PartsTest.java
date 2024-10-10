import com.example.protrack.parts.Parts;
import com.example.protrack.requests.Requests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Parts} class.
 * This test class checks for the proper functionality of the Parts constructor and its getter methods.
 * It also verifies that the constructor throws the appropriate exceptions for invalid input.
 */
public class PartsTest {

    private Parts part;

    /**
     * Sets up a valid {@link Parts} object before each test.
     * The test object has specific part ID, name, description, supplier ID, and cost.
     */
    @BeforeEach
    public void setUp() {
        part = new Parts(1, "Part Name", "Part Description", 1001, 9.99);
    }

    /**
     * Tests that the part ID of the part is correctly returned by {@link Parts#getPartsId()}.
     */
    @Test
    public void testGetPartsId() {
        assertEquals(1, part.getPartsId());
    }

    /**
     * Tests that the name of the part is correctly returned by {@link Parts#getName()}.
     */
    @Test
    public void testGetName() {
        assertEquals("Part Name", part.getName());
    }

    /**
     * Tests that the description of the part is correctly returned by {@link Parts#getDescription()}.
     */
    @Test
    public void testGetDescription() {
        assertEquals("Part Description", part.getDescription());
    }

    /**
     * Tests that the supplier ID of the part is correctly returned by {@link Parts#getSupplierId()}.
     */
    @Test
    public void testGetSupplierId() {
        assertEquals(1001, part.getSupplierId());
    }

    /**
     * Tests that the cost of the part is correctly returned by {@link Parts#getCost()}.
     */
    @Test
    public void testGetCost() {
        assertEquals(9.99, part.getCost());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the part ID is null.
     * Verifies that the exception message matches "No fields can be null".
     */
    @Test
    public void testNullPartsId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(null, "Part", "Part Description", 1001, 9.99);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the name is null.
     * Verifies that the exception message matches "No fields can be null".
     */
    @Test
    public void testNullName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(1, null, "Part Description", 1001, 9.99);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the description is null.
     * Verifies that the exception message matches "No fields can be null".
     */
    @Test
    public void testNullDescription() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(1, "Part", null, 1001, 9.99);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the supplier ID is null.
     * Verifies that the exception message matches "No fields can be null".
     */
    @Test
    public void testNullSupplierId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(1, "Part", "Part Description", null, 9.99);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the cost is null.
     * Verifies that the exception message matches "No fields can be null".
     */
    @Test
    public void testNullCost() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(1, "Part", "Part Description", 1001, null);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the part ID is negative.
     * Verifies that the exception message matches "ID cannot be negative".
     */
    @Test
    public void testNegativePartsId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(-1, "Part Name", "Part Description", 1, 10.0);
        });
        assertEquals("ID cannot be negative", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the cost exceeds the maximum.
     * Verifies that the exception message matches "Cost must be between 0 and 10,000.00".
     */
    @Test
    public void testCostExceedsMaximum() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(1, "Part Name", "Part Description", 1, 10001.00);
        });
        assertEquals("Cost must be between 0 and 10,000.00", exception.getMessage());
    }
}