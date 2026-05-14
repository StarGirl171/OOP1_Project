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
    protected void executeLogic(String[] args) throws Exception {
        calendarManager.removeEvent(
                LocalDate.parse(args[0]),
                LocalTime.parse(args[1]),
                LocalTime.parse(args[2])
        );
        fileManager.markChanged();
    }

    @Override
    public String getName() { return "unbook"; }

    @Override
    public String getHelp() { return "unbook <date> <start> <end> - removes an appointment"; }
}