package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.Event;
import bg.tu_varna.sit.f24621666.core.FileManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FindSlotWithCommand extends AbstractCommand {
    public FindSlotWithCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override
    protected int getMinArgs() { return 3; }

    @Override
    protected String getUsage() { return "Usage: findslotwith <fromdate> <hours> <calendar1> [calendar2]..."; }

    @Override
    protected void executeLogic(String[] args) {
        LocalDate startDate = LocalDate.parse(args[0]);
        int hours = Integer.parseInt(args[1]);

        List<List<Event>> externalCalendars = new ArrayList<>();
        // Обхождаме всички подадени файлове след първите два аргумента
        for (int i = 2; i < args.length; i++) {
            List<Event> externalEvents = calendarManager.loadEventsFromFile(args[i]);
            externalCalendars.add(externalEvents);
        }

        calendarManager.findSlotWith(startDate, hours, externalCalendars);
    }

    @Override
    public String getName() { return "findslotwith"; }

    @Override
    public String getHelp() { return "findslotwith <date> <hours> <file>... - finds free slot in multiple calendars"; }
}