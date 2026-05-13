package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

import java.time.LocalDate;
import java.time.LocalTime;

public class ChangeCommand extends AbstractCommand {
    public ChangeCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override
    protected int getMinArgs() { return 5; }

    @Override
    protected String getUsage() { return "Usage: change <date> <start> <end> <option> <newvalue>"; }

    @Override
    protected void executeLogic(String[] args) {
        calendarManager.changeEvent(
                LocalDate.parse(args[0]),
                LocalTime.parse(args[1]),
                LocalTime.parse(args[2]),
                args[3],
                args[4]
        );
        fileManager.markChanged();
    }

    @Override
    public String getName() { return "change"; }

    @Override
    public String getHelp() { return "change <date> <start> <end> <option> <newvalue> - updates event details"; }
}