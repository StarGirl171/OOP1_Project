package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

/**
 * Command to save the current calendar data to a specifically provided new path.
 */
public class SaveAsCommand extends AbstractCommand {
    /**
     * Constructs SaveAsCommand.
     * @param fm FileManager reference.
     * @param cm CalendarManager reference.
     */
    public SaveAsCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    /** @return 1 (new path). */
    @Override protected int getMinArgs() { return 1; }

    /** @return usage string. */
    @Override protected String getUsage() { return "Usage: saveas <path>"; }

    /**
     * Executes save logic for a new destination.
     * @param args The new file path.
     */
    @Override
    protected void executeLogic(String[] args) {
        // Пренасочваме данните към нов файл на диска
        fileManager.saveAs(args[0]);
    }

    /** @return "saveas" */
    @Override public String getName() { return "saveas"; }

    /** @return help string. */
    @Override public String getHelp() { return "saveas <file> - saves the open file in <file>"; }
}