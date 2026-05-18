package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.abstractions.AbstractCommand;
import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

/**
 * Command responsible for opening a calendar file and loading its data.
 */
public class OpenCommand extends AbstractCommand {
    /**
     * Constructs OpenCommand.
     * @param fm FileManager reference.
     * @param cm CalendarManager reference.
     */
    public OpenCommand(FileManager fm, CalendarManager cm) {
        super(fm, cm);
    }

    /**
     * Overrides to false because we can't require an open file to open a file.
     * @return false.
     */
    @Override
    protected boolean requiresOpenFile() {
        return false;
    }

    /**
     * Minimum arguments for opening.
     * @return 1 (the file path).
     */
    @Override
    protected int getMinArgs() { return 1; }

    /**
     * Returns usage syntax.
     * @return usage string.
     */
    @Override
    protected String getUsage() { return "Usage: open <path>"; }

    /**
     * Executes the file opening logic.
     * @param args Array where [0] is the path.
     */
    @Override
    protected void executeLogic(String[] args) {
        // Извикваме мениджъра да зареди файла от диска
        fileManager.open(args[0]);
    }

    /**
     * Gets the command name.
     * @return "open"
     */
    @Override public String getName() { return "open"; }

    /**
     * Gets the help description.
     * @return help string.
     */
    @Override public String getHelp() { return "open <file> - opens <file>"; }
}