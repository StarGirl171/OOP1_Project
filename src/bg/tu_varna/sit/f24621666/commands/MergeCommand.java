package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

import java.util.Arrays;
import java.util.List;

public class MergeCommand extends AbstractCommand {
    public MergeCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override
    protected int getMinArgs() { return 1; }

    @Override
    protected String getUsage() { return "Usage: merge <file1> [file2]..."; }

    @Override
    protected void executeLogic(String[] args) {
        List<String> paths = Arrays.asList(args);
        calendarManager.mergeWithCalendars(paths, this.scanner);
        fileManager.markChanged();
    }

    @Override
    public String getName() { return "merge"; }

    @Override
    public String getHelp() { return "merge <file>... - merges external calendars"; }
}