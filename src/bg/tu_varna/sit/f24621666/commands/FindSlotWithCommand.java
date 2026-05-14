package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.Event;
import bg.tu_varna.sit.f24621666.core.FileManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Advanced command to find a free slot by considering multiple external calendars.
 */
public class FindSlotWithCommand extends AbstractCommand {
    /**
     * Constructs FindSlotWithCommand.
     * @param fm FileManager reference.
     * @param cm CalendarManager reference.
     */
    public FindSlotWithCommand(FileManager fm, CalendarManager cm) {
        super(fm, cm);
    }

    /** @return 3 (date, hours, and at least one external file). */
    @Override protected int getMinArgs() { return 3; }

    /** @return usage string. */
    @Override protected String getUsage() { return "Usage: findslotwith <fromDate> <hours> <externalFile1> [file2]..."; }

    /**
     * Loads external files and searches for a common free slot.
     * @param args Date, hours, and list of file paths.
     * @throws Exception if parsing fails.
     */
    @Override
    protected void executeLogic(String[] args) throws Exception {
        LocalDate fromDate = LocalDate.parse(args[0]);
        int hours = Integer.parseInt(args[1]);

        List<List<Event>> externalCalendars = new ArrayList<>();
        // Зареждаме събитията от всеки подаден външен файл
        for (int i = 2; i < args.length; i++) {
            externalCalendars.add(calendarManager.loadEventsFromFile(args[i]));
        }

        // Търсим слот, който е свободен едновременно в нашия календар и във всички външни
        calendarManager.findSlotWith(fromDate, hours, externalCalendars);
    }

    /** @return "findslotwith" */
    @Override public String getName() { return "findslotwith"; }

    /** @return help string. */
    @Override public String getHelp() { return "findslotwith <date> <hours> <files...> - finds a slot common for all files"; }
}