import com.example.protrack.products.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product(1, "Product", Date.valueOf("2024-01-01"), 9.99);
    }

    @Test
    public void testGetProductId() {
        assertEquals(1, product.getProductId());
    }

    @Test
    public void testGetProductName() {
        assertEquals("Product", product.getProductName());
    }

    @Test
    public void testGetDateCreated() {
        assertEquals(Date.valueOf("2024-01-01"), product.getDateCreated());
    }

    @Test
    public void testGetPrice() {
        assertEquals(9.99, product.getPrice(), 0.001); // Using delta for double comparison
    }

    @Test
    public void testNullProductId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(null, "Product", Date.valueOf("2024-01-01"), 9.99);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullProductName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(1, null, Date.valueOf("2024-01-01"), 9.99);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullDateCreated() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(1, "Product", null, 9.99);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNegativeProductId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(-1, "Product", new Date(System.currentTimeMillis()), 9.99);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testProductNameExceedsLength() {
        String longName = "P".repeat(256);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(1, longName, new Date(System.currentTimeMillis()), 9.99);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testEmptyProductName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(1, "", Date.valueOf("2024-01-01"), 9.99);
        });
        assertEquals("Product name cannot be empty", exception.getMessage());
    }

    @Test
    public void testFutureDateCreated() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(1, "Product", Date.valueOf("3000-01-01"), 9.99);
        });
        assertEquals("Date created cannot be in the future", exception.getMessage());
    }

    @Test
    public void testNaNPrice() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(1, "Product", Date.valueOf("2024-01-01"), Double.NaN);
        });
        assertEquals("Price must be a non-negative number", exception.getMessage());
    }

    @Test
    public void testNullPrice() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(1, "Product", Date.valueOf("2024-01-01"), null); // Assuming price can't be null
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNegativePrice() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product(1, "Product", Date.valueOf("2024-01-01"), -1.0); // Assuming negative price is invalid
        });
        assertEquals("Price must be a non-negative number", exception.getMessage());
    }
}
