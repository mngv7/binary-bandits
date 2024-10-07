import com.example.protrack.warehouseutil.RealWarehouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseTest {

    private RealWarehouse realWarehouse;

    @BeforeEach
    public void setUp() {
        realWarehouse = new RealWarehouse(1, "Main Warehouse", 10000);
    }

    @Test
    public void testValidInitialization() {
        assertEquals(1, realWarehouse.getWarehouseLocationId());
        assertEquals("Main Warehouse", realWarehouse.getWarehouseLocationAlias());
        assertEquals(10000, realWarehouse.getWarehouseMaxParts());
    }

    @Test
    public void testNullLocation() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new RealWarehouse(1, null, 10000);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullPartsLinked() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new RealWarehouse(1, "Main Warehouse", 10000, null);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullWarehouseName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            realWarehouse.setWarehouseLocationAlias(null);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullWarehouseLocation() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            realWarehouse.setWarehouseLocation(null);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }
}