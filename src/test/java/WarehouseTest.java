import com.example.protrack.warehouseutil.MockWarehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockWarehouseTest {

    private MockWarehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = new MockWarehouse(1, "Default Warehouse", "Spike Site A");
    }

    @Test
    void testGetWarehouseName() {
        assertEquals("Default Warehouse", warehouse.getWarehouseName());
    }

    @Test
    void testGetWarehouseLocation() {
        assertEquals("Spike Site A", warehouse.getWarehouseLocation());
    }

    @Test
    public void testGetWarehouseId() {
        assertEquals(1, warehouse.getWarehouseId());
    }
    //

}