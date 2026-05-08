package bg.tu_varna.sit.f24621666;

import java.time.LocalDate;
import java.time.LocalTime;

public class ChangeCommand implements Command {
    private final FileManager fileManager;
    private final CalendarManager calendarManager;

    public ChangeCommand(FileManager fileManager, CalendarManager calendarManager) {
        this.fileManager = fileManager;
        this.calendarManager = calendarManager;
    }

    @Override
    public void execute(String[] args) {
        if (!fileManager.isOpen()) {
            System.out.println("Error: Please open a file first.");
            return;
        }

        if (args.length < 5) {
            System.out.println("Usage: change <date> <start> <end> <option> <newvalue>");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(args[0]);
            LocalTime start = LocalTime.parse(args[1]);
            LocalTime end = LocalTime.parse(args[2]);
            String option = args[3];
            String newValue = args[4];

            calendarManager.changeEvent(date, start, end, option, newValue);
        } catch (Exception e) {
            System.out.println("Error: Invalid input format.");
        }
    }

    @Override
    public String getName() { return "change"; }

    @Override
    public String getHelp() { return "change <date> <start> <end> <option> <newvalue> - updates event details"; }
}