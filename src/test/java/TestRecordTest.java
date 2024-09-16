import com.example.protrack.products.TestRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRecordTest {
    private TestRecord testRecord;

    @BeforeEach
    public void setUp() {
        testRecord = new TestRecord(1, 45021, 1, "Inspect the exterior for any visible damage or scratches.", "CheckBox", "NULL");
    }

    @Test
    public void testGetStepId() {
        assertEquals(1, testRecord.getStepId());
    }

    @Test
    public void testGetStepNumber() {
        assertEquals(1, testRecord.getStepNumber());
    }

    @Test
    public void testGetProductId() {
        assertEquals(45021, testRecord.getProductId());
    }

    @Test
    public void testGetStepDescription() {
        assertEquals("Inspect the exterior for any visible damage or scratches.", testRecord.getStepDescription());
    }

    @Test
    public void testGetCheckType() {
        assertEquals("CheckBox", testRecord.getStepCheckType());
    }

    @Test
    public void testCheckCriteria() {
        assertEquals("NULL", testRecord.getStepCheckCriteria());
    }
}
