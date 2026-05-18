package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.abstractions.AbstractCommand;
import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.Event;
import bg.tu_varna.sit.f24621666.core.FileManager;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Command responsible for scheduling new events in the calendar.
 */
public class BookCommand extends AbstractCommand {
    /**
     * Constructs BookCommand.
     * @param fm FileManager reference.
     * @param cm CalendarManager reference.
     */
    public BookCommand(FileManager fm, CalendarManager cm) {
        super(fm, cm);
    }

    /**
     * Minimum arguments for booking.
     * @return 5 (date, start, end, name, note).
     */
    @Override protected int getMinArgs() { return 5; }

    /**
     * Returns usage syntax.
     * @return usage string.
     */
    @Override protected String getUsage() {
        return "Usage: book <date> <startTime> <endTime> <name> <note>";
    }

    /**
     * Parses input and adds the event to the manager.
     * @param args Parsed user inputs.
     * @throws Exception if data types are invalid.
     */
    @Override
    protected void executeLogic(String[] args) throws Exception {
        // Парсваме аргументите към съответните типове
        LocalDate date = LocalDate.parse(args[0]);
        LocalTime start = LocalTime.parse(args[1]);
        LocalTime end = LocalTime.parse(args[2]);
        String name = args[3];
        String note = args[4];

        Event newEvent = new Event(date, start, end, name, note);

        // 3. Просто извикваме addEvent.
        // Не ни трябва if-else тук, защото ако има проблем,
        // addEvent ще "хвърли" изключение, което AbstractCommand ще хване автоматично.
        calendarManager.addEvent(newEvent);

        // Ако addEvent не хвърли грешка, кодът продължава тук и маркираме промяна за запис
        fileManager.markChanged();
    }

    /**
     * Gets the command identifier.
     * @return "book"
     */
    @Override public String getName() { return "book"; }

    /**
     * Gets the documentation help.
     * @return help string.
     */
    @Override public String getHelp() { return "book <date> <start> <end> <name> <note> - creates an event"; }
}