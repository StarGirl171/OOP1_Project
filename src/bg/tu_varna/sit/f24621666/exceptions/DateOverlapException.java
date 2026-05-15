package bg.tu_varna.sit.f24621666.exceptions;

/**
 * Thrown when an attempt is made to book an event in a time slot
 * that is already occupied by another event.
 */
public class DateOverlapException extends CalendarException {
    /**
     * Constructs a DateOverlapException.
     * @param msg message describing the scheduling conflict.
     */
    public DateOverlapException(String msg) { super(msg); }
}
