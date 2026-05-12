package bg.tu_varna.sit.f24621666;

import java.time.LocalDate;

public class BusyDaysCommand implements Command {
    private final FileManager fileManager;
    private final CalendarManager calendarManager;

    public BusyDaysCommand(FileManager fileManager, CalendarManager calendarManager) {
        this.fileManager = fileManager;
        this.calendarManager = calendarManager;
    }

    @Override
    public void execute(String[] args) {
        if (!fileManager.isOpen()) {
            System.out.println("Error: Please open a file first.");
            return;
        }

        if (args.length < 2) {
            System.out.println("Usage: busydays <from> <to>");
            return;
        }

        try {
            LocalDate from = LocalDate.parse(args[0]);
            LocalDate to = LocalDate.parse(args[1]);
            calendarManager.showBusyDays(from, to);
        } catch (Exception e) {
            System.out.println("Error: Invalid date format. Use YYYY-MM-DD.");
        }
    }

    @Override
    public String getName() { return "busydays"; }

    @Override
    public String getHelp() { return "busydays <from> <to> - shows statistics of busy days in period"; }
}