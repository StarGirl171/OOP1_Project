package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.abstractions.AbstractCommand;
import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;
import java.time.LocalDate;

/**
 * Command to find the first available free slot for a given duration.
 */
public class FindSlotCommand extends AbstractCommand {
    /**
     * Constructs FindSlotCommand.
     * @param fm FileManager reference.
     * @param cm CalendarManager reference.
     */
    public FindSlotCommand(FileManager fm, CalendarManager cm) {
        super(fm, cm);
    }

    /**
     * Defines the minimum number of arguments.
     * @return 2 (date and hours).
     */
    @Override protected int getMinArgs() { return 2; }

    /**
     * Returns usage instructions.
     * @return usage string.
     */
    @Override protected String getUsage() { return "Usage: findslot <fromDate> <hours>"; }

    /**
     * Executes the logic to search for a free slot.
     * @param args Date to start from and duration in hours.
     * @throws Exception if date or number parsing fails.
     */
    @Override
    protected void executeLogic(String[] args) throws Exception {
        // Търсим първия свободен интервал в рамките на работното време
        calendarManager.findSlot(LocalDate.parse(args[0]), Integer.parseInt(args[1]));
    }

    /**
     * Gets the command name.
     * @return "findslot"
     */
    @Override public String getName() { return "findslot"; }

    /**
     * Gets the help description.
     * @return help string.
     */
    @Override public String getHelp() { return "findslot <date> <hours> - finds the first free slot"; }
}