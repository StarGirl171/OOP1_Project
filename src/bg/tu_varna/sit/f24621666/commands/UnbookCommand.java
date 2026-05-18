package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.abstractions.AbstractCommand;
import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Command responsible for removing an existing event from the calendar.
 * Requires the exact date and time slot to identify the event.
 */
public class UnbookCommand extends AbstractCommand {

    /**
     * Constructs the UnbookCommand.
     * @param fm Reference to the file manager.
     * @param cm Reference to the calendar manager.
     */
    public UnbookCommand(FileManager fm, CalendarManager cm) {
        super(fm, cm);
    }

    /**
     * Defines the minimum number of arguments.
     * @return 3 (date, start time, end time).
     */
    @Override
    protected int getMinArgs() {
        return 3;
    }

    /**
     * Returns the syntax for using this command.
     * @return Usage instruction string.
     */
    @Override
    protected String getUsage() {
        return "Usage: unbook <date> <startTime> <endTime>";
    }

    /**
     * Executes the event removal logic.
     * @param args Date, start time, and end time.
     * @throws Exception if parsing fails.
     */
    @Override
    protected void executeLogic(String[] args) throws Exception {
        // Намираме и изтриваме събитието. Ако е намерено, маркираме промяна във файла.
        calendarManager.removeEvent(
                LocalDate.parse(args[0]),
                LocalTime.parse(args[1]),
                LocalTime.parse(args[2])
        );
        fileManager.markChanged();
    }

    /**
     * Returns the command name.
     * @return "unbook"
     */
    @Override
    public String getName() {
        return "unbook";
    }

    /**
     * Returns the help description.
     * @return Help string.
     */
    @Override
    public String getHelp() {
        return "unbook <date> <start> <end> - removes an event from the schedule";
    }
}