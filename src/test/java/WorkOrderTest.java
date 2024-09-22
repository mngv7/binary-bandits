import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.customer.Customer;
import com.example.protrack.users.ProductionUser;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDateTime;

public class WorkOrderTest {
    private Customer customer;
    private ProductionUser orderOwner;
    private WorkOrder workOrder;

    @BeforeEach
    public void setup() {
        customer = new Customer(1, "John", "Doe", "john.doe@example.com", null, "123 Street", "456 Avenue", "Active");
        orderOwner = new ProductionUser(1, "Jane", "Smith", Date.valueOf("1985-01-01"), "jane.smith@example.com", null, "Female", "password");
        workOrder = new WorkOrder(1, orderOwner, customer, LocalDateTime.now(), null, null, "Pending", 100.0);
    }

    @Test
    public void testGetWorkOrderId() {
        assertEquals(1, workOrder.getWorkOrderId());
    }

    @Test
    public void testGetOrderOwner() {
        assertNotNull(workOrder.getOrderOwner());
    }

    @Test
    public void testGetCustomer() {
        assertNotNull(workOrder.getCustomer());
    }

    @Test
    public void testGetOrderDate() {
        assertNotNull(workOrder.getOrderDate());
    }

    @Test
    public void testGetDeliveryDate() {
        assertNull(workOrder.getDeliveryDate());
    }

    @Test
    public void testGetShippingAddress() {
        assertNull(workOrder.getShippingAddress());
    }

    @Test
    public void testGetStatus() {
        assertEquals("Pending", workOrder.getStatus());
    }

    @Test
    public void testGetSubtotal() {
        assertEquals(100.0, workOrder.getSubtotal());
    }

    @Test
    public void testSetShippingAddress() {
        workOrder.setShippingAddress("New Shipping Address");
        assertEquals("New Shipping Address", workOrder.getShippingAddress());
    }

    @Test
    public void testSetStatus() {
        workOrder.setStatus("Completed");
        assertEquals("Completed", workOrder.getStatus());
    }

    @Test
    public void testNullWorkOrderId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WorkOrder(null, orderOwner, customer, LocalDateTime.now(), null, null, null, 100.0);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullCustomer() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WorkOrder(1, orderOwner, null, LocalDateTime.now(), null, null, null, 100.0);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullOrderDate() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WorkOrder(1, orderOwner, customer, null, null, null, null, 100.0);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullSubtotal() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WorkOrder(1, orderOwner, customer, LocalDateTime.now(), null, null, null, null);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNegativeSubtotal() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WorkOrder(1, orderOwner, customer, LocalDateTime.now(), null, null, null, -100.0);
        });
        assertEquals("Subtotal cannot be negative", exception.getMessage());
    }
}