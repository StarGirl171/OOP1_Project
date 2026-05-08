package bg.tu_varna.sit.f24621666;

public class FindCommand implements Command {
    private final FileManager fileManager;
    private final CalendarManager calendarManager;

    public FindCommand(FileManager fileManager, CalendarManager calendarManager) {
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
            System.out.println("Usage: find <string>");
            return;
        }

        calendarManager.findEvents(args[0]);
    }

    @Override
    public String getName() { return "find"; }

    @Override
    public String getHelp() { return "find <string> - searches for events containing the string"; }
}