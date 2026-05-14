package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

/**
 * Command to terminate the current file session and clear memory.
 */
public class CloseCommand extends AbstractCommand {
    /**
     * Constructs CloseCommand.
     * @param fm FileManager reference.
     * @param cm CalendarManager reference.
     */
    public CloseCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    /** @return 0 arguments. */
    @Override protected int getMinArgs() { return 0; }

    /** @return usage string. */
    @Override protected String getUsage() { return "Usage: close"; }

    /**
     * Executes the closure logic.
     * @param args No arguments.
     */
    @Override
    protected void executeLogic(String[] args) {
        // Затваряме файла и изчистваме текущите събития
        fileManager.close();
    }

    /** @return "close" */
    @Override public String getName() { return "close"; }

    /** @return help string. */
    @Override public String getHelp() { return "close - closes currently opened file"; }
}