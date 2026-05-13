package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

import java.time.LocalDate;

public class AgendaCommand extends AbstractCommand {
    public AgendaCommand(FileManager fileManager, CalendarManager calendarManager) {
        super(fileManager, calendarManager);
    }

    @Override
    protected int getMinArgs() { return 1; }

    @Override
    protected void executeLogic(String[] args) {
        LocalDate date = LocalDate.parse(args[0]);
        calendarManager.showAgenda(date);
    }

    @Override
    protected String getUsage() { return "Usage: agenda <date>"; }

    @Override
    public String getName() { return "agenda"; }

    @Override
    public String getHelp() { return "agenda <date> - lists appointments"; }
}