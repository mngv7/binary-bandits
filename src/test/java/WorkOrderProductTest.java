import com.example.protrack.workorderproducts.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkOrderProductTest {

    @Test
    void testSetQuantity() {
        WorkOrderProduct product = new WorkOrderProduct(1, "Widget", "A standard widget", 10, 5.99, 59.90);
        product.setQuantity(20);

        assertEquals(20, product.getQuantity());
        assertEquals(20 * 5.99, product.getTotal(), 0.01);
    }

    @Test
    void testZeroValues() {
        WorkOrderProduct product = new WorkOrderProduct(2, "Gadget", "A basic gadget", 0, 0.0, 0.0);

        assertEquals(0, product.getQuantity());
        assertEquals(0.0, product.getPrice());
        assertEquals(0.0, product.getTotal());
    }

    @Test
    void testLargeValues() {
        // Test with large values, ensuring no overflow occurs
        try {
            WorkOrderProduct product = new WorkOrderProduct(3, "Product", "A product", Integer.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

            assertEquals(Integer.MAX_VALUE, product.getQuantity());
            assertTrue(Double.isInfinite(product.getTotal()));
        } catch (Exception e) {
            fail("Exception occurred while testing large values: " + e.getMessage());
        }
    }
}