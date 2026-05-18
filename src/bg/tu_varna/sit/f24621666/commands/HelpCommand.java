package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.abstractions.AbstractCommand;
import bg.tu_varna.sit.f24621666.abstractions.Command;
import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;
import java.util.Collection;
import java.util.Comparator;

/**
 * Command that lists all available commands in the system with their descriptions.
 */
public class HelpCommand extends AbstractCommand {
    /** A collection of all command objects to be listed. */
    private final Collection<Command> allCommands;

    /**
     * Constructs HelpCommand.
     * @param fm FileManager reference.
     * @param cm CalendarManager reference.
     * @param allCommands List of all registered commands.
     */
    public HelpCommand(FileManager fm, CalendarManager cm, Collection<Command> allCommands) {
        super(fm, cm);
        this.allCommands = allCommands;
    }

    /**
     * Help can be shown even if no file is open.
     * @return false.
     */
    @Override protected boolean requiresOpenFile() { return false; }

    /** @return 0 arguments. */
    @Override protected int getMinArgs() { return 0; }

    /** @return usage string. */
    @Override protected String getUsage() { return "Usage: help"; }

    /**
     * Displays a sorted list of all commands.
     * @param args No arguments.
     */
    @Override
    protected void executeLogic(String[] args) {
        System.out.println("The following commands are supported:");
        // Сортираме командите по име за по-лесно четене
        allCommands.stream()
                .sorted(Comparator.comparing(Command::getName))
                .forEach(cmd -> System.out.println("  " + cmd.getHelp()));
    }

    /** @return "help" */
    @Override public String getName() { return "help"; }

    /** @return help string. */
    @Override public String getHelp() { return "help - prints this help message"; }
}