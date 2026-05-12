package bg.tu_varna.sit.f24621666;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FindSlotWithCommand implements Command {
    private final FileManager fileManager;
    private final CalendarManager calendarManager;

    public FindSlotWithCommand(FileManager fileManager, CalendarManager calendarManager) {
        this.fileManager = fileManager;
        this.calendarManager = calendarManager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: findslotwith <fromdate> <hours> <calendar1> [calendar2]...");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(args[0]);
            int hours = Integer.parseInt(args[1]);

            List<List<Event>> externalCalendars = new ArrayList<>();
            for (int i = 2; i < args.length; i++) {
                externalCalendars.add(calendarManager.loadEventsFromFile(args[i]));
            }

            calendarManager.findSlotWith(date, hours, externalCalendars);
        } catch (Exception e) {
            System.out.println("Error: Invalid parameters for findslotwith.");
        }
    }

    @Override
    public String getName() { return "findslotwith"; }
    @Override
    public String getHelp() { return "findslotwith <date> <hours> <calendar> - finds free slot in multiple calendars"; }
}