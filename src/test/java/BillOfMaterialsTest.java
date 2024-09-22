import com.example.protrack.products.BillOfMaterials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BillOfMaterialsTest {

    private BillOfMaterials bom;

    @BeforeEach
    public void setUp() {
        bom = new BillOfMaterials(1, 100, 10);
    }

    @Test
    public void testGetPartsId() {
        assertEquals(1, bom.getPartsId());
    }

    @Test
    public void testGetProductId() {
        assertEquals(100, bom.getProductId());
    }

    @Test
    public void testGetRequiredAmount() {
        assertEquals(10, bom.getRequiredAmount());
    }

    @Test
    public void testValidInputs() {
        assertEquals(1, bom.getPartsId());
        assertEquals(100, bom.getProductId());
        assertEquals(10, bom.getRequiredAmount());
    }

    @Test
    public void testNullPartsId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new BillOfMaterials(null, 100, 10);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullProductId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new BillOfMaterials(1, null, 10);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullRequiredAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new BillOfMaterials(1, 100, null);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testRequiredAmountExceedsLimit() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new BillOfMaterials(1, 1, 1001);
        });
        assertEquals("Required amount must be between 1 and 1000", exception.getMessage());
    }

    @Test
    public void testRequiredAmountBelowLimit() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new BillOfMaterials(1, 1, 0);
        });
        assertEquals("Required amount must be between 1 and 1000", exception.getMessage());
    }
}