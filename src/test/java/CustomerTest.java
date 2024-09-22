import com.example.protrack.customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer(1, "John", "Doe", "john.doe@example.com", null, "Billing Address", "Shipping Address", "Active");
    }

    @Test
    public void testGetCustomerId() {
        assertEquals(1, customer.getCustomerId());
    }

    @Test
    public void testGetFirstName() {
        assertEquals("John", customer.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("Doe", customer.getLastName());
    }

    @Test
    public void testGetEmail() {
        assertEquals("john.doe@example.com", customer.getEmail());
    }

    @Test
    public void testGetPhoneNumber() {
        assertNull(customer.getPhoneNumber());
    }

    @Test
    public void testGetBillingAddress() {
        assertEquals("Billing Address", customer.getBillingAddress());
    }

    @Test
    public void testGetShippingAddress() {
        assertEquals("Shipping Address", customer.getShippingAddress());
    }

    @Test
    public void testGetStatus() {
        assertEquals("Active", customer.getStatus());
    }

    @Test
    public void testSetFirstName() {
        customer.setFirstName("Jane");
        assertEquals("Jane", customer.getFirstName());
    }

    @Test
    public void testSetLastName() {
        customer.setLastName("Smith");
        assertEquals("Smith", customer.getLastName());
    }

    @Test
    public void testNegativeCustomerId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Customer(-1, "John", "Doe", "email@example.com", "1234567890", "Billing Address", "Shipping Address", "Active");
        });
        assertEquals("No field can be null", exception.getMessage());
    }

    @Test
    public void testEmailExceedsLength() {
        String longEmail = "a".repeat(256) + "@example.com";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Customer(1, "John", "Doe", longEmail, "1234567890", "Billing Address", "Shipping Address", "Active");
        });
        assertEquals("Email must not exceed 255 characters", exception.getMessage());
    }

    @Test
    public void testSetPhoneNumberValid() {
        customer.setPhoneNumber("0412345678");
        assertEquals("0412345678", customer.getPhoneNumber());
    }

    @Test
    public void testSetPhoneNumberExceedingMaxLength() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customer.setPhoneNumber("041234567890");
        });
        assertEquals("Phone number cannot exceed 11 characters.", exception.getMessage());
    }

    @Test
    public void testNullOrders() {
        customer.setOrders(null);
        assertNull(customer.getOrders());
    }

    @Test
    public void testInvalidStatus() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customer.setStatus("Unknown");
        });
        assertEquals("Status must be 'Active' or 'Inactive'", exception.getMessage());
    }

    @Test
    public void testValidStatus() {
        customer.setStatus("Inactive");
        assertEquals("Inactive", customer.getStatus());
    }

    @Test
    public void testSetPhoneNumberNull() {
        customer.setPhoneNumber(null);
        assertNull(customer.getPhoneNumber());
    }
}