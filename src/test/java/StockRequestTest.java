import com.example.protrack.requests.Requests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Requests} class.
 * This test class checks for the proper functionality of the Requests constructor and its getter methods.
 * It also verifies that the constructor throws the appropriate exceptions for invalid input.
 */
public class StockRequestTest {
    private Requests request;

    /**
     * Sets up a valid {@link Requests} object before each test.
     * The test object has specific location ID, part ID, request ID, and request quantity.
     */
    @BeforeEach
    public void setUp() { request = new Requests(1, 5, 11, 13);}

    /**
     * Tests that the request ID of the request is correctly returned by {@link Requests#getRequestId()}.
     */
    @Test
    public void testGetRequestId() { assertEquals(11, request.getRequestId());}

    /**
     * Tests that the location ID of the request is correctly returned by {@link Requests#getLocationId()}.
     */
    @Test
    public void testGetLocationId() { assertEquals(1, request.getLocationId());}

    /**
     * Tests that the part ID of the request is correctly returned by {@link Requests#getPartId()}.
     */
    @Test
    public void testGetPartId() { assertEquals(5, request.getPartId()); }

    /**
     * Tests that the quantity of the request is correctly returned by {@link Requests#getQuantity()}.
     */
    @Test
    public void testGetRequestQuantity() { assertEquals(13, request.getQuantity());}

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the request ID is null.
     * Verifies that the exception message matches "No fields can be null".
     */
    @Test
    public void testNullRequestId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
           new Requests(1, 5, null, 13);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the location ID is null.
     * Verifies that the exception message matches "No fields can be null".
     */
    @Test
    public void testNullLocationId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Requests(null, 5, 11, 13);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the partID is null.
     * Verifies that the exception message matches "No fields can be null".
     */
    @Test
    public void testNullPartId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Requests(1, null, 11, 13);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the quantity is null.
     * Verifies that the exception message matches "No fields can be null".
     */
    @Test
    public void testNullQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Requests(1, 5, 11, null);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the part ID is negative.
     * Verifies that the exception message matches "ID cannot be negative".
     */
    @Test
    public void testNegativePartId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Requests(1, -1, 11, 13);
        });
        assertEquals("ID cannot be negative", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the quantity is less than or equal to 0.
     * Verifies that the exception message matches "Quantity requested must be more than 0".
     */
    @Test
    public void testNegativeQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Requests(1, 5, 11, 0);
        });
        assertEquals("Quantity requested must be more than 0", exception.getMessage());
    }
}
