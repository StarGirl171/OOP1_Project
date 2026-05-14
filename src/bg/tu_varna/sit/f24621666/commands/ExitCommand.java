package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

/**
 * Command to terminate the application.
 */
public class ExitCommand extends AbstractCommand {
    /**
     * Constructs ExitCommand.
     * @param fm FileManager reference.
     * @param cm CalendarManager reference.
     */
    public ExitCommand(FileManager fm, CalendarManager cm) {
        super(fm, cm);
    }

    /**
     * Exit does not require an open file to run.
     * @return false.
     */
    @Override protected boolean requiresOpenFile() { return false; }

    /** @return 0 arguments required. */
    @Override protected int getMinArgs() { return 0; }

    /** @return usage string. */
    @Override protected String getUsage() { return "Usage: exit"; }

    /**
     * Prints exit message.
     * The actual termination is handled by the loop control in CommandProcessor/Main.
     * @param args No arguments.
     */
    @Override
    protected void executeLogic(String[] args) {
        // Извеждаме прощално съобщение преди спиране
        System.out.println("Exiting the program. Goodbye!");
    }

    /** @return "exit" */
    @Override public String getName() { return "exit"; }

    /** @return help string. */
    @Override public String getHelp() { return "exit - exits the program"; }
}