package bg.tu_varna.sit.f24621666;

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

        System.out.println("Change command recognized. Updating logic...");
    }

    @Override
    public String getName() { return "change"; }

    @Override
    public String getHelp() { return "change <date> <start> <end> <option> <newvalue> - updates event details"; }
}