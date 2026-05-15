package bg.tu_varna.sit.f24621666.exceptions;

/**
 * Thrown when an event is being scheduled on a date that is
 * officially marked as a holiday or a non-working day.
 */
public class HolidayConflictException extends CalendarException {
    /**
     * Constructs a HolidayConflictException.
     * @param msg message explaining why the date is restricted.
     */
    public HolidayConflictException(String msg) {
        super(msg);
    }
}