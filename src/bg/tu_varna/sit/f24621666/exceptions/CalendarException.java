package bg.tu_varna.sit.f24621666.exceptions;

/**
 * Base exception class for all calendar-related business logic errors.
 * Serves as a parent for more specific exceptions in the system.
 */
public class CalendarException extends Exception {
    /**
     * Constructs a new CalendarException with the specified detail message.
     * @param message the detail message.
     */
    public CalendarException(String message) { super(message); }
}