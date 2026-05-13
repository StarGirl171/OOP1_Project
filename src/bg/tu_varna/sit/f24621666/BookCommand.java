package bg.tu_varna.sit.f24621666;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookCommand extends AbstractCommand {

    public BookCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override
    protected int getMinArgs() { return 5; }

    @Override
    protected String getUsage() { return "Usage: book <date> <start> <end> <name> <note>"; }

    @Override
    protected void executeLogic(String[] args) {
        Event event = new Event(LocalDate.parse(args[0]), LocalTime.parse(args[1]),
                LocalTime.parse(args[2]), args[3], args[4]);

        if (!event.isValid()) {
            System.out.println("Error: Start time must be before end time.");
            return;
        }

        if (calendarManager.addEvent(event)) {
            fileManager.markChanged();
        }
    }

    @Override public String getName() { return "book"; }

    @Override public String getHelp() { return "book <date> <start> <end> <name> <note> - schedules an event"; }
}