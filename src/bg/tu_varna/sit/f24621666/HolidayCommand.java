package bg.tu_varna.sit.f24621666;

import java.time.LocalDate;

public class HolidayCommand implements Command {
    private final FileManager fileManager;
    private final CalendarManager calendarManager;

    public HolidayCommand(FileManager fileManager, CalendarManager calendarManager) {
        this.fileManager = fileManager;
        this.calendarManager = calendarManager;
    }

    @Override
    public void execute(String[] args) {
        if (!fileManager.isOpen()) {
            System.out.println("Error: Please open a file first.");
            return;
        }

        if (args.length < 1) {
            System.out.println("Usage: holiday <date>");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(args[0]);
            calendarManager.addHoliday(date);
        } catch (Exception e) {
            System.out.println("Error: Invalid date format. Use YYYY-MM-DD.");
        }
    }

    @Override
    public String getName() { return "holiday"; }

    @Override
    public String getHelp() { return "holiday <date> - marks the date as non-working"; }
}
