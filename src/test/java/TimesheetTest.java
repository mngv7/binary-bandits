import com.example.protrack.timesheets.Timesheets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Timesheets} class.
 * This test class checks the proper functionality of the Timesheets constructor and its getter methods.
 * It also verifies that the constructor throws the appropriate exceptions for invalid input.
 */
public class TimesheetTest {
    private Timesheets timesheet;

    /**
     * Sets up a valid {@link Timesheets} object before each test.
     * The test object has specific start and end times, employee ID, and product order ID.
     */
    @BeforeEach
    public void setUp() {timesheet = new Timesheets(LocalDateTime.of(2024, 10, 4, 10, 30, 00, 00), LocalDateTime.of(2024, 10, 4, 15, 30, 00, 00), 100, 1);}

    /**
     * Tests that the start time of the timesheet is correctly returned by {@link Timesheets#getStartTime()}.
     */
    @Test
    public void testGetStartTime() {
        assertEquals(LocalDateTime.of(2024, 10, 4, 10, 30, 00, 00), timesheet.getStartTime());
    }

    /**
     * Tests that the end time of the timesheet is correctly returned by {@link Timesheets#getEndTime()}.
     */
    @Test
    public void testGetEndTime() {
        assertEquals(LocalDateTime.of(2024, 10, 4, 15, 30, 00, 00), timesheet.getEndTime());
    }

    /**
     * Tests that the employee ID of the timesheet is correctly returned by {@link Timesheets#getEmployeeID()}.
     */
    @Test
    public void testGetEmployeeId() {
        assertEquals(100, timesheet.getEmployeeID());
    }

    /**
     * Tests that the product order ID of the timesheet is correctly returned by {@link Timesheets#getProductOrderID()}.
     */
    @Test
    public void testGetProductOrderId() {
        assertEquals(1, timesheet.getProductOrderID());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the start time is null.
     * Verifies that the exception message matches "No fields can be null".
     */
    @Test
    public void testNullStartTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Timesheets(null, LocalDateTime.of(2024, 10, 4, 15, 30, 00, 00), 100, 1);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the end time is null.
     * Verifies that the exception message matches "No fields can be null".
     */
    @Test
    public void testNullEndTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Timesheets(LocalDateTime.of(2024, 10, 4, 10, 30, 00, 00), null, 100, 1);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the employee ID is null.
     * Verifies that the exception message matches "No fields can be null".
     */
    @Test
    public void testNullEmployeeId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Timesheets(LocalDateTime.of(2024, 10, 4, 10, 30, 00, 00),
                    LocalDateTime.of(2024, 10, 4, 15, 30, 00, 00), null, 1);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the product order ID is null.
     * Verifies that the exception message matches "No fields can be null".
     */
    @Test
    public void testNullProductOrderId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Timesheets(LocalDateTime.of(2024, 10, 4, 10, 30, 00, 00),
                    LocalDateTime.of(2024, 10, 4, 15, 30, 00, 00), 100, null);
        });
        assertEquals("No fields can be null", exception.getMessage());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the end time is set in the future.
     * Verifies that the exception message matches "End Time must be past/current time".
     */
    @Test
    public void testFutureEndTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Timesheets(LocalDateTime.of(2024, 10, 4, 10, 30, 00, 00),
                    LocalDateTime.now().plusDays(1), 100, 1);
        });
        assertEquals("End Time must be past/current time", exception.getMessage());
    }
}
