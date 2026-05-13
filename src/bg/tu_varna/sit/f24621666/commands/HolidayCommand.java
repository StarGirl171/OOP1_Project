package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

import java.time.LocalDate;

public class HolidayCommand extends AbstractCommand {
    public HolidayCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override
    protected int getMinArgs() { return 1; }

    @Override
    protected String getUsage() { return "Usage: holiday <date>"; }

    @Override
    protected void executeLogic(String[] args) {
        calendarManager.addHoliday(LocalDate.parse(args[0]));
        fileManager.markChanged();
    }

    @Override
    public String getName() { return "holiday"; }

    @Override
    public String getHelp() { return "holiday <date> - marks date as non-working"; }
}
