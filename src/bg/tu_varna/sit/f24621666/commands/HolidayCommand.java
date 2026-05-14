package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;
import java.time.LocalDate;

/**
 * Command to mark a specific date as a holiday or non-working day.
 */
public class HolidayCommand extends AbstractCommand {
    /**
     * Constructs HolidayCommand.
     * @param fm FileManager reference.
     * @param cm CalendarManager reference.
     */
    public HolidayCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    /**
     * Minimum arguments for marking a holiday.
     * @return 1 (the date).
     */
    @Override protected int getMinArgs() { return 1; }

    /**
     * Returns usage syntax.
     * @return usage string.
     */
    @Override protected String getUsage() { return "Usage: holiday <date>"; }

    /**
     * Executes the logic to add a holiday to the calendar.
     * @param args Array where [0] is the date.
     * @throws Exception if date parsing fails.
     */
    @Override
    protected void executeLogic(String[] args) throws Exception {
        // Добавяме датата в списъка с празници и маркираме промяна за запис
        calendarManager.addHoliday(LocalDate.parse(args[0]));
        fileManager.markChanged();
    }

    /** @return "holiday" */
    @Override public String getName() { return "holiday"; }

    /** @return help string. */
    @Override public String getHelp() { return "holiday <date> - marks a day as non-working"; }
}