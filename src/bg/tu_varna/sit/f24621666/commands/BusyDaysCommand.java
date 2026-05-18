package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.abstractions.AbstractCommand;
import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;
import java.time.LocalDate;

/**
 * Command that generates a statistical report of busy time per day.
 * It sorts the days by total duration of events in descending order.
 */
public class BusyDaysCommand extends AbstractCommand {

    /**
     * Constructs the BusyDaysCommand.
     * @param fm Reference to the file manager.
     * @param cm Reference to the calendar manager.
     */
    public BusyDaysCommand(FileManager fm, CalendarManager cm) {
        super(fm, cm);
    }

    /**
     * Defines the minimum number of arguments.
     * @return 2 (start date and end date).
     */
    @Override
    protected int getMinArgs() {
        return 2;
    }

    /**
     * Returns the syntax for using this command.
     * @return Usage instruction string.
     */
    @Override
    protected String getUsage() {
        return "Usage: busydays <from> <to>";
    }

    /**
     * Triggers the calculation of busy hours for the given period.
     * @param args Array where [0] is start date and [1] is end date.
     * @throws Exception if dates are invalid.
     */
    @Override
    protected void executeLogic(String[] args) throws Exception {
        // Изчисляваме общото време на заетите слотове за всеки ден в периода
        calendarManager.showBusyDays(LocalDate.parse(args[0]), LocalDate.parse(args[1]));
    }

    /**
     * Returns the command name.
     * @return "busydays"
     */
    @Override
    public String getName() {
        return "busydays";
    }

    /**
     * Returns the help description.
     * @return Help string.
     */
    @Override
    public String getHelp() {
        return "busydays <from> <to> - shows statistics of busy time sorted by duration";
    }
}