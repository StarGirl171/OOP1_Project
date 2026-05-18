package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.abstractions.AbstractCommand;
import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;
import java.util.Arrays;

/**
 * Command to merge events from one or more external files into the current calendar.
 */
public class MergeCommand extends AbstractCommand {
    /**
     * Constructs MergeCommand.
     * @param fm FileManager reference.
     * @param cm CalendarManager reference.
     */
    public MergeCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    /** @return 1 (at least one file to merge). */
    @Override protected int getMinArgs() { return 1; }

    /** @return usage string. */
    @Override protected String getUsage() { return "Usage: merge <file1> [file2] ..."; }

    /**
     * Triggers the multi-file merge logic using the protected scanner for conflicts.
     * @param args List of file paths.
     */
    @Override
    protected void executeLogic(String[] args) {
        // Извикваме логиката за сливане, като предаваме скенера от AbstractCommand
        calendarManager.mergeWithCalendars(Arrays.asList(args), this.scanner);
        fileManager.markChanged();
    }

    /** @return "merge" */
    @Override public String getName() { return "merge"; }

    /** @return help string. */
    @Override public String getHelp() { return "merge <file> - merges external calendar into current"; }
}