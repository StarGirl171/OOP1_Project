package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

import java.time.LocalDate;

public class AgendaCommand extends AbstractCommand {
    public AgendaCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override
    protected int getMinArgs() { return 1; }

    @Override
    protected String getUsage() { return "Usage: agenda <date>"; }

    @Override
    protected void executeLogic(String[] args) throws Exception {
        calendarManager.showAgenda(LocalDate.parse(args[0]));
    }

    @Override
    public String getName() { return "agenda"; }

    @Override
    public String getHelp() { return "agenda <date> - lists appointments for a specific day"; }
}