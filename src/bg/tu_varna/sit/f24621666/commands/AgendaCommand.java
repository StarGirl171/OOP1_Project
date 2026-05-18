package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.abstractions.AbstractCommand;
import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;
import java.time.LocalDate;

/**
 * Command responsible for displaying the schedule (agenda) for a specific date.
 * It shows all events on that day and indicates if the day is a holiday.
 */
public class AgendaCommand extends AbstractCommand {

    /**
     * Constructs the AgendaCommand.
     * @param fm Reference to the file manager.
     * @param cm Reference to the calendar manager.
     */
    public AgendaCommand(FileManager fm, CalendarManager cm) {
        super(fm, cm);
    }

    /**
     * Defines the minimum number of arguments.
     * @return 1 (the date to be checked).
     */
    @Override
    protected int getMinArgs() {
        return 1;
    }

    /**
     * Returns the syntax for using this command.
     * @return Usage instruction string.
     */
    @Override
    protected String getUsage() {
        return "Usage: agenda <date>";
    }

    /**
     * Executes the logic to retrieve and display the agenda.
     * @param args Array where [0] is the date string.
     * @throws Exception if date parsing fails.
     */
    @Override
    protected void executeLogic(String[] args) throws Exception {
        // Извикваме метода в мениджъра, който филтрира събитията за конкретната дата
        calendarManager.showAgenda(LocalDate.parse(args[0]));
    }

    /**
     * Returns the command name.
     * @return "agenda"
     */
    @Override
    public String getName() {
        return "agenda";
    }

    /**
     * Returns a brief description of the command.
     * @return Help string.
     */
    @Override
    public String getHelp() {
        return "agenda <date> - lists all events and holiday status for a given date";
    }
}