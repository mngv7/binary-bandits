import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.example.protrack.customer.Customer;
import com.example.protrack.workorder.WorkOrder;

import java.util.ArrayList;

public class CustomerTest {
    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer(1, "John", "Doe", "john@doe.com", "0414444111", "billingAddress", "shippingAddress", "pending");
    }

    @Test
    void testGetCustomerId() {
        assertEquals(1, customer.getCustomerId());
    }

    @Test
    void testGetFirstName() {
        assertEquals("John", customer.getFirstName());
    }

    @Test
    void testGetLastName() {
        assertEquals("Doe", customer.getLastName());
    }

    @Test
    void testGetEmail() {
        assertEquals("john@doe.com", customer.getEmail());
    }

    @Test
    void testGetPhoneNumber() {
        assertEquals("0414444111", customer.getPhoneNumber());
    }

    @Test
    void testGetBillingAddress() {
        assertEquals("billingAddress", customer.getBillingAddress());
    }

    @Test
    void testGetShippingAddress() {
        assertEquals("shippingAddress", customer.getShippingAddress());
    }

    @Test
    void testGetStatus() {
        assertEquals("pending", customer.getStatus());
    }


    @Test
    void setFirstName() {
        customer.setFirstName("Jonathon");
        assertEquals("Jonathon", customer.getFirstName());
    }

    @Test
    void setLastName() {
        customer.setLastName("Doh");
        assertEquals("Doh", customer.getLastName());
    }

    @Test
    void setEmail() {
        customer.setEmail("jonathon@doe.com");
        assertEquals("jonathon@doe.com", customer.getEmail());
    }

    @Test
    void setPhoneNumber() {
        customer.setPhoneNumber("0444696969");
        assertEquals("0444696969", customer.getPhoneNumber());
    }

    @Test
    void setBillingAddress() {
        customer.setBillingAddress("newBillingAddress");
        assertEquals("newBillingAddress", customer.getBillingAddress());
    }

    @Test
    void setShippingAddress() {
        customer.setShippingAddress("newShippingAddress");
        assertEquals("newShippingAddress", customer.getShippingAddress());
    }

    @Test
    void setStatus() {
        customer.setStatus("inactive");
        assertEquals("inactive", customer.getStatus());
    }
}