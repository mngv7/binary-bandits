import com.example.protrack.users.ProductionUser;
import com.example.protrack.customer.Customer;
import com.example.protrack.workorder.WorkOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WorkOrderTest {
    private ProductionUser productionUser;
    private Customer customer;
    private WorkOrder workOrder;

    @BeforeEach
    public void setUp() {
        productionUser = new ProductionUser(1, "productionUserFirstName", "productionUserLastName", Date.valueOf("2000-01-01"), "production@user.com", "0444444444", "Female", "password");
        customer = new Customer(1, "customerFirstName", "customerLastName", "customer@email.com", "555-5555", "billingAddress", "shippingAddress", "Active");
        workOrder = new WorkOrder(1, productionUser, customer, LocalDateTime.now(), null, "shippingAddress", 1, "pending", 40.87);
    }

    @Test
    void testGetWorkOrderId() {
        assertEquals(1, workOrder.getWorkOrderId());
    }

    @Test
    void testGetOrderOwner() {
        assertEquals(productionUser, workOrder.getOrderOwner());
    }

    @Test
    void testGetCustomer() {
        assertEquals(customer, workOrder.getCustomer());
    }

    @Test
    void testGetOrderDate() {
        assertEquals(LocalDateTime.now(), workOrder.getOrderDate());
    }

    @Test
    void testGetDeliveryDate() {
        assertNull(workOrder.getDeliveryDate());
    }

    @Test
    void testGetShippingAddress() {
        assertEquals("shippingAddress", workOrder.getShippingAddress());
    }

    @Test
    void testGetProducts() {
        assertEquals(1, workOrder.getProducts());
    }

    @Test
    void testGetStatus() { assertEquals("pending", workOrder.getStatus()); }

    @Test
    void testGetSubtotal() {
        assertEquals(40.87, workOrder.getSubtotal());
    }


    @Test
    void testSetDeliveryDate() {
        workOrder.setDeliveryDate(LocalDateTime.MAX);
        assertEquals(LocalDateTime.MAX, workOrder.getDeliveryDate());
    }

    @Test
    void testSetOrderOwner() {
        ProductionUser productionUser2 = new ProductionUser(2, "productionUser2FirstName", "productionUser2LastName", Date.valueOf("2000-02-02"), "production@user2.com", "0444444442", "Male", "password2");
        workOrder.setOrderOwner(productionUser2);
        assertEquals(productionUser2, workOrder.getOrderOwner());
    }

    @Test
    void testSetShippingAddress() {
        workOrder.setShippingAddress("alteredShippingAddress");
        assertEquals("alteredShippingAddress", workOrder.getShippingAddress());
    }

    @Test
    void testSetProducts() {
        workOrder.setProducts(69);
        assertEquals(69, workOrder.getProducts());
    }

    @Test
    void testSetStatus() {
        workOrder.setStatus("Production");
        assertEquals("Production", workOrder.getStatus());
    }
}
