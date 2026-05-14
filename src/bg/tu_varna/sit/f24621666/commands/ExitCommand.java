package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

public class ExitCommand extends AbstractCommand {
    public ExitCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override protected boolean requiresOpenFile() { return false; }
    @Override protected int getMinArgs() { return 0; }
    @Override protected String getUsage() { return "Usage: exit"; }

    @Override
    protected void executeLogic(String[] args) {
        System.out.println("Exiting program...");
        System.exit(0); // Директно спира приложението
    }

    @Override public String getName() { return "exit"; }
    @Override public String getHelp() { return "exit - exits the program"; }
}