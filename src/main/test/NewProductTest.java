import com.example.protrack.products.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewProductTest {
    private Product product;

    long millis = System.currentTimeMillis();
    java.sql.Date date = new java.sql.Date(millis);

    @BeforeEach
    public void setUp() {
        product = new Product(1, "Computer", date);
    }

    @Test
    public void testGetProductId() {assertEquals(1, product.getProductId());}

    @Test
    public void testGetProductName() {assertEquals("Computer", product.getProductName());}

    @Test
    public void testGetDate() {assertEquals(date, product.getDateCreated());}
}