package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.abstractions.AbstractCommand;
import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

/**
 * Command responsible for searching events by a specific keyword or string.
 * It scans both the titles and the notes of all scheduled events.
 */
public class FindCommand extends AbstractCommand {
    /**
     * Constructs the FindCommand with necessary managers.
     * @param fm Reference to the file manager.
     * @param cm Reference to the calendar manager.
     */
    public FindCommand(FileManager fm, CalendarManager cm) {
        super(fm, cm);
    }

    /**
     * Defines the minimum number of arguments required for searching.
     * @return 1 (the search string).
     */
    @Override
    protected int getMinArgs() {
        return 1;
    }

    /**
     * Returns the syntax for using this command.
     * @return A string showing the correct usage.
     */
    @Override
    protected String getUsage() {
        return "Usage: find <string>";
    }

    /**
     * Executes the search logic by calling the calendar manager.
     * @param args Array where [0] is the search query.
     */
    @Override
    protected void executeLogic(String[] args) {
        // Извикваме метода за търсене в мениджъра, който обхожда всички събития
        calendarManager.findEvents(args[0]);
    }

    /**
     * Returns the unique name of the command.
     * @return "find"
     */
    @Override
    public String getName() {
        return "find";
    }

    /**
     * Returns a brief description of what the command does.
     * @return Help information string.
     */
    @Override
    public String getHelp() {
        return "find <string> - searches for events containing the given string";
    }
}