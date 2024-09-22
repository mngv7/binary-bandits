import com.example.protrack.workorderproducts.WorkOrderProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorkOrderProductTest {

    private WorkOrderProduct workOrderProduct;

    @BeforeEach
    public void setup() {
        workOrderProduct = new WorkOrderProduct(1, 1, "Product Name", 5, 20.0);
    }

    @Test
    public void testGetWorkOrderId() {
        assertEquals(1, workOrderProduct.getWorkOrderId());
    }

    @Test
    public void testGetProductId() {
        assertEquals(1, workOrderProduct.getProductId());
    }

    @Test
    public void testGetProductName() {
        assertEquals("Product Name", workOrderProduct.getProductName());
    }

    @Test
    public void testGetQuantity() {
        assertEquals(5, workOrderProduct.getQuantity());
    }

    @Test
    public void testGetPrice() {
        assertEquals(20.0, workOrderProduct.getPrice());
    }

    @Test
    public void testGetTotal() {
        assertEquals(100.0, workOrderProduct.getTotal());
    }

    @Test
    public void testSetQuantity() {
        workOrderProduct.setQuantity(10);
        assertEquals(10, workOrderProduct.getQuantity());
        assertEquals(200.0, workOrderProduct.getTotal());
    }

    @Test
    public void testSetPrice() {
        workOrderProduct.setPrice(25.0);
        assertEquals(25.0, workOrderProduct.getPrice());
        assertEquals(125.0, workOrderProduct.getTotal());
    }

    @Test
    public void testNullProductName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WorkOrderProduct(1, 1, null, 10, 100.0);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNegativeWorkOrderIdInConstructor() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WorkOrderProduct(-1, 1, "Product", 10, 100.0);
        });
        assertEquals("Work order ID cannot be negative", exception.getMessage());
    }

    @Test
    public void testNegativeProductIdInConstructor() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WorkOrderProduct(1, -1, "Product", 10, 100.0);
        });
        assertEquals("Product ID cannot be negative", exception.getMessage());
    }

    @Test
    public void testNegativeQuantityInConstructor() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WorkOrderProduct(1, 1, "Product", -5, 100.0);
        });
        assertEquals("Quantity cannot be negative", exception.getMessage());
    }

    @Test
    public void testNegativePriceInConstructor() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WorkOrderProduct(1, 1, "Product", 10, -100.0);
        });
        assertEquals("Price cannot be negative", exception.getMessage());
    }

    @Test
    public void testNegativeQuantitySet() {
        WorkOrderProduct product = new WorkOrderProduct(1, 1, "Product", 10, 100.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            product.setQuantity(-5);
        });
        assertEquals("Quantity cannot be negative", exception.getMessage());
    }

    @Test
    public void testNegativePriceSet() {
        WorkOrderProduct product = new WorkOrderProduct(1, 1, "Product", 10, 100.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            product.setPrice(-100.0);
        });
        assertEquals("Price cannot be negative", exception.getMessage());
    }

    @Test
    public void testTotalCalculation() {
        WorkOrderProduct product = new WorkOrderProduct(1, 1, "Product", 5, 20.0);
        assertEquals(100.0, product.getTotal());
        product.setQuantity(10);
        assertEquals(200.0, product.getTotal());
        product.setPrice(15.0);
        assertEquals(150.0, product.getTotal());
    }
}