package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

import java.util.Collection;

public class HelpCommand extends AbstractCommand {
    private final Collection<Command> allCommands;

    public HelpCommand(FileManager fm, CalendarManager cm, Collection<Command> commands) {
        super(fm, cm); // не ни трябват но ги подаваме, за да спазим структурата
        this.allCommands = commands;
    }

    @Override
    protected boolean requiresOpenFile() { return false; }

    @Override
    protected int getMinArgs() { return 0; }

    @Override
    protected String getUsage() { return "Usage: help"; }

    @Override
    protected void executeLogic(String[] args) {
        System.out.println("The following commands are supported:");
        allCommands.forEach(cmd -> System.out.println(cmd.getHelp()));
        System.out.println("exit - exists the program");
    }

    @Override
    public String getName() { return "help"; }

    @Override
    public String getHelp() { return "help - prints this information"; }
}