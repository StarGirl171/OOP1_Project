package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.abstractions.AbstractCommand;
import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

/**
 * Command to persist current changes to the already opened file.
 */
public class SaveCommand extends AbstractCommand {
    /**
     * Constructs SaveCommand.
     * @param fm FileManager reference.
     * @param cm CalendarManager reference.
     */
    public SaveCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    /** @return 0 arguments. */
    @Override protected int getMinArgs() { return 0; }

    /** @return usage string. */
    @Override protected String getUsage() { return "Usage: save"; }

    /**
     * Triggers the save process in FileManager.
     * @param args No arguments needed.
     */
    @Override
    protected void executeLogic(String[] args) {
        // Записваме текущото състояние на календара
        fileManager.save();
    }

    /** @return "save" */
    @Override public String getName() { return "save"; }

    /** @return help string. */
    @Override public String getHelp() { return "save - saves the currently open file"; }
}