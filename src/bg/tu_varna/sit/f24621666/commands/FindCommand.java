package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

public class FindCommand extends AbstractCommand {
    public FindCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override
    protected int getMinArgs() { return 1; }

    @Override
    protected String getUsage() { return "Usage: find <string>"; }

    @Override
    protected void executeLogic(String[] args) { calendarManager.findEvents(args[0]); }

    @Override
    public String getName() { return "find"; }

    @Override
    public String getHelp() { return "find <string> - searches for events by string"; }
}