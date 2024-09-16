import com.example.protrack.parts.Parts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewPartTest {
    private Parts parts;

    @BeforeEach
    public void setUp() {
        parts = new Parts(1, "Batteries", "Batteries in the size AAA", 12, 12.50);
    }

    @Test
    public void testGetPartId() {assertEquals(1, parts.getPartsId());}

    @Test
    public void testGetPartName() {assertEquals("Batteries", parts.getName());}

    @Test
    public void testGetPartDescription() {assertEquals("Batteries in the size AAA", parts.getDescription());}

    @Test
    public void testGetPartSupplierId() {assertEquals(12, parts.getSupplierId());}

    @Test
    public void testGetPartCost() {assertEquals(12.50, parts.getCost());}
}