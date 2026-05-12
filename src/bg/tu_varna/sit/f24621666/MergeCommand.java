package bg.tu_varna.sit.f24621666;

import java.util.ArrayList;
import java.util.List;

public class MergeCommand implements Command {
    private final FileManager fileManager;
    private final CalendarManager calendarManager;

    public MergeCommand(FileManager fileManager, CalendarManager calendarManager) {
        this.fileManager = fileManager;
        this.calendarManager = calendarManager;
    }

    @Override
    public void execute(String[] args) {
        if (!fileManager.isOpen()) {
            System.out.println("Error: Open a file first.");
            return;
        }
        if (args.length < 1) {
            System.out.println("Usage: merge <calendar1> [calendar2]...");
            return;
        }

        List<String> paths = new ArrayList<>();
        for (String arg : args) paths.add(arg);

        calendarManager.mergeWithCalendars(paths);
    }

    @Override
    public String getName() { return "merge"; }
    @Override
    public String getHelp() { return "merge <calendar>... - merges multiple calendars into current one"; }
}