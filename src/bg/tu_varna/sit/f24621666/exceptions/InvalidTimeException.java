package bg.tu_varna.sit.f24621666.exceptions;

/**
 * Thrown when the provided time range for an event is logically incorrect
 * (e.g., start time is after or equal to the end time).
 */
public class InvalidTimeException extends CalendarException {
    /**
     * Constructs an InvalidTimeException.
     * @param msg message detailing the time range error.
     */
    public InvalidTimeException(String msg) {
        super(msg);
    }
}