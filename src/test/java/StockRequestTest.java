import com.example.protrack.requests.Requests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StockRequestTest {
    private Requests request;

    @BeforeEach
    public void setUp() { request = new Requests(1, 5, 11, 13);}

    @Test
    public void testGetRequestId() { assertEquals(11, request.getRequestId());}

    @Test
    public void testGetLocationId() { assertEquals(1, request.getLocationId());}

    @Test
    public void testGetPartId() { assertEquals(5, request.getPartId()); }

    @Test
    public void testGetRequestQuantity() { assertEquals(13, request.getQuantity());}

    @Test
    public void testNullRequestId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
           new Requests(1, 5, null, 13);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullLocationId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Requests(null, 5, 11, 13);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullPartId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Requests(1, null, 11, 13);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Requests(1, 5, 11, null);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNegativePartId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Requests(1, -1, 11, 13);
        });
        assertEquals("ID cannot be negative", exception.getMessage());
    }

    @Test
    public void testNegativeQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Requests(1, 5, 11, 0);
        });
        assertEquals("Quantity requested must be more than 0", exception.getMessage());
    }
}
