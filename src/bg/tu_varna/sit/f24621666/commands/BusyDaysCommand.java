package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

import java.time.LocalDate;

public class BusyDaysCommand extends AbstractCommand {
    public BusyDaysCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override
    protected int getMinArgs() { return 2; }

    @Override
    protected String getUsage() { return "Usage: busydays <from> <to>"; }

    @Override
    protected void executeLogic(String[] args) {
        calendarManager.showBusyDays(LocalDate.parse(args[0]), LocalDate.parse(args[1]));
    }

    @Override
    public String getName() { return "busydays"; }

    @Override
    public String getHelp() { return "busydays <from> <to> - shows busy days statistics"; }
}