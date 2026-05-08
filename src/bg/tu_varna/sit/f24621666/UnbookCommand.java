package bg.tu_varna.sit.f24621666;

import java.time.LocalDate;
import java.time.LocalTime;

public class UnbookCommand implements Command {
    private final FileManager fileManager;
    private final CalendarManager calendarManager;

    public UnbookCommand(FileManager fileManager, CalendarManager calendarManager) {
        this.fileManager = fileManager;
        this.calendarManager = calendarManager;
    }

    @Override
    public void execute(String[] args) {
        // Проверка дали е отворен файл
        if (!fileManager.isOpen()) {
            System.out.println("Error: Please open a file first.");
            return;
        }

        // Проверка за брой параметри
        if (args.length < 3) {
            System.out.println("Usage: unbook <date> <starttime> <endtime>");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(args[0]);
            LocalTime start = LocalTime.parse(args[1]);
            LocalTime end = LocalTime.parse(args[2]);

            calendarManager.removeEvent(date, start, end);
        } catch (Exception e) {
            System.out.println("Error: Invalid date or time format.");
        }
    }

    @Override
    public String getName() { return "unbook"; }

    @Override
    public String getHelp() { return "unbook <date> <starttime> <endtime> - removes an event"; }
}