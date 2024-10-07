import com.example.protrack.timesheets.Timesheets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TimesheetTest {
    private Timesheets timesheet;

    @BeforeEach
    public void setUp() {timesheet = new Timesheets(LocalDateTime.of(2024, 10, 4, 10, 30, 00, 00), LocalDateTime.of(2024, 10, 4, 15, 30, 00, 00), 100, 1);}

    @Test
    public void testGetStartTime() { assertEquals(LocalDateTime.of(2024, 10, 4, 10, 30, 00, 00), timesheet.getStartTime());}

    @Test
    public void testGetEndTime() { assertEquals(LocalDateTime.of(2024, 10, 4, 15, 30, 00, 00), timesheet.getEndTime());}

    @Test
    public void testGetEmployeeId() { assertEquals(100, timesheet.getEmployeeID());}

    @Test
    public void testGetProductOrderId() { assertEquals(1, timesheet.getProductOrderID());}

    @Test
    public void testNullStartTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Timesheets(null, LocalDateTime.of(2024, 10, 4, 15, 30, 00, 00), 100, 1);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullEndTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Timesheets(LocalDateTime.of(2024, 10, 4, 10, 30, 00, 00), null, 100, 1);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullEmployeeId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Timesheets(LocalDateTime.of(2024, 10, 4, 10, 30, 00, 00), LocalDateTime.of(2024, 10, 4, 15, 30, 00, 00), null, 1);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testNullProductOrderId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Timesheets(LocalDateTime.of(2024, 10, 4, 10, 30, 00, 00), LocalDateTime.of(2024, 10, 4, 15, 30, 00, 00), 100, null);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    @Test
    public void testFutureEndTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Timesheets(LocalDateTime.of(2024, 10, 4, 10, 30, 00, 00), LocalDateTime.now().plusDays(1), 100, 1);
        });
        assertEquals("End Time must be past/current time", exception.getMessage());
    }
}
