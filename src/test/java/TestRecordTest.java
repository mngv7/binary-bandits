import com.example.protrack.products.TestRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestRecordTest {

    private TestRecord testRecord;

    @BeforeEach
    public void setUp() {
        testRecord = new TestRecord(1, 101, 1, "Initial test step", "Type A", "Criteria A");
    }

    @Test
    public void testValidInputs() {
        assertEquals(1, testRecord.getStepId());
        assertEquals(101, testRecord.getProductId());
        assertEquals(1, testRecord.getStepNumber());
        assertEquals("Initial test step", testRecord.getStepDescription());
        assertEquals("Type A", testRecord.getStepCheckType());
        assertEquals("Criteria A", testRecord.getStepCheckCriteria());
    }

    @Test
    public void testNullStepId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new TestRecord(null, 101, 1, "Initial test step", "Type A", "Criteria A");
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullProductId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new TestRecord(1, null, 1, "Initial test step", "Type A", "Criteria A");
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullStepNumber() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new TestRecord(1, 101, null, "Initial test step", "Type A", "Criteria A");
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullStepDescription() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new TestRecord(1, 101, 1, null, "Type A", "Criteria A");
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullStepCheckType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new TestRecord(1, 101, 1, "Initial test step", null, "Criteria A");
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullStepCheckCriteria() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new TestRecord(1, 101, 1, "Initial test step", "Type A", null);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }
}