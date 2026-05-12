package bg.tu_varna.sit.f24621666;

import java.time.LocalDate;

public class FindSlotCommand implements Command {
    private final FileManager fileManager;
    private final CalendarManager calendarManager;

    public FindSlotCommand(FileManager fileManager, CalendarManager calendarManager) {
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
            System.out.println("Usage: findslot <fromdate> <hours>");
            return;
        }
        try {
            LocalDate date = LocalDate.parse(args[0]);
            int hours = Integer.parseInt(args[1]);
            calendarManager.findSlot(date, hours);
        } catch (Exception e) {
            System.out.println("Error: Invalid input. Ensure date is YYYY-MM-DD and hours is a number.");
        }
    }

    @Override
    public String getName() { return "findslot"; }

    @Override
    public String getHelp() { return "findslot <fromdate> <hours> - finds the first free slot of given duration"; }
}