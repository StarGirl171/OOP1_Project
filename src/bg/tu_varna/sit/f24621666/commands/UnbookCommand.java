package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

import java.time.LocalDate;
import java.time.LocalTime;

public class UnbookCommand extends AbstractCommand {
    public UnbookCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override
    protected int getMinArgs() { return 3; }

    @Override
    protected String getUsage() { return "Usage: unbook <date> <starttime> <endtime>"; }

    @Override
    protected void executeLogic(String[] args) {
        LocalDate date = LocalDate.parse(args[0]);
        LocalTime start = LocalTime.parse(args[1]);
        LocalTime end = LocalTime.parse(args[2]);
        calendarManager.removeEvent(date, start, end);
        fileManager.markChanged();
    }

    @Override
    public String getName() { return "unbook"; }

    @Override
    public String getHelp() { return "unbook <date> <start> <end> - removes an appointment"; }
}