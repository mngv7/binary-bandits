import com.example.protrack.products.BillOfMaterials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BillOfMaterialsTest {
    private BillOfMaterials billOfMaterials;

    @BeforeEach
    public void setUp () {
        billOfMaterials = new BillOfMaterials (128, 76921, 500);
    }

    @Test
    public void testGetPartsId() { assertEquals(128, billOfMaterials.getPartsId()); }

    @Test
    public void testGetProductId() { assertEquals(76921, billOfMaterials.getProductId()); }

    @Test
    public void testGetRequiredAmount() { assertEquals(500, billOfMaterials.getRequiredAmount()); }
}