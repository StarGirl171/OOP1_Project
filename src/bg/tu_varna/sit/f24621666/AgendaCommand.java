package bg.tu_varna.sit.f24621666;

import java.time.LocalDate;

public class AgendaCommand implements Command {
    private final FileManager fileManager;
    private final CalendarManager calendarManager;

    public AgendaCommand(FileManager fileManager, CalendarManager calendarManager) {
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
            System.out.println("Usage: agenda <date>");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(args[0]);
            calendarManager.showAgenda(date);
        } catch (Exception e) {
            System.out.println("Error: Invalid input format.");
        }
    }

    @Override
    public String getName() { return "agenda"; }

    @Override
    public String getHelp() { return "agenda <date> - lists all appointments for a specific date"; }
}