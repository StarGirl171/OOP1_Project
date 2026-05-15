package bg.tu_varna.sit.f24621666.exceptions;

/**
 * Thrown when the user provides an incorrect number of arguments
 * or malformed data through the command line interface.
 */
public class InvalidCommandArgumentException extends CalendarException {
    /**
     * Constructs an InvalidCommandArgumentException.
     * @param msg message explaining the usage error.
     */
    public InvalidCommandArgumentException(String msg) {
        super(msg);
    }
}