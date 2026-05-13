package bg.tu_varna.sit.f24621666.commands;
import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

import java.time.LocalDate;

public class FindSlotCommand extends AbstractCommand {
    public FindSlotCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override
    protected int getMinArgs() { return 2; }

    @Override
    protected String getUsage() { return "Usage: findslot <date> <hours>"; }

    @Override
    protected void executeLogic(String[] args) {
        calendarManager.findSlot(LocalDate.parse(args[0]), Integer.parseInt(args[1]));
    }

    @Override
    public String getName() { return "findslot"; }

    @Override
    public String getHelp() { return "findslot <date> <hours> - finds free slot in your calendar"; }
}