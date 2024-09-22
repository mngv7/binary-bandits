import com.example.protrack.parts.Parts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PartsTest {

    private P
    arts part;

    @BeforeEach
    public void setUp() {
        part = new Parts(1, "Widget", "A useful widget", 1001, 9.99);
    }

    @Test
    public void testConstructorWithValidInputs() {
        assertEquals(1, part.getPartsId());
        assertEquals("Widget", part.getName());
        assertEquals("A useful widget", part.getDescription());
        assertEquals(1001, part.getSupplierId());
        assertEquals(9.99, part.getCost());
    }

    @Test
    public void testConstructorWithNullPartsId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(null, "Widget", "A useful widget", 1001, 9.99);
        });
        assertEquals("None of the fields can be null", exception.getMessage());
    }

    @Test
    public void testConstructorWithNullName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(1, null, "A useful widget", 1001, 9.99);
        });
        assertEquals("None of the fields can be null", exception.getMessage());
    }

    @Test
    public void testConstructorWithNullDescription() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(1, "Widget", null, 1001, 9.99);
        });
        assertEquals("None of the fields can be null", exception.getMessage());
    }

    @Test
    public void testConstructorWithNullSupplierId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(1, "Widget", "A useful widget", null, 9.99);
        });
        assertEquals("None of the fields can be null", exception.getMessage());
    }

    @Test
    public void testConstructorWithNullCost() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Parts(1, "Widget", "A useful widget", 1001, null);
        });
        assertEquals("None of the fields can be null", exception.getMessage());
    }
}