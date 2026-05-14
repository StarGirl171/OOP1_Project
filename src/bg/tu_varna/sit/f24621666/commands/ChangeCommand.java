package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Command to update a specific property of an existing event.
 */
public class ChangeCommand extends AbstractCommand {
    /**
     * Constructs ChangeCommand.
     * @param fm FileManager reference.
     * @param cm CalendarManager reference.
     */
    public ChangeCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    /** @return 5 (date, start, end, option, newValue). */
    @Override protected int getMinArgs() { return 5; }

    /** @return usage string. */
    @Override protected String getUsage() { return "Usage: change <date> <start> <end> <option> <newValue>"; }

    /**
     * Executes the event update.
     * @param args Original identifiers and new data.
     * @throws Exception if parsing or validation fails.
     */
    @Override
    protected void executeLogic(String[] args) throws Exception {
        // Търсим събитието по дата и часове и променяме избраното поле
        calendarManager.changeEvent(
                LocalDate.parse(args[0]),
                LocalTime.parse(args[1]),
                LocalTime.parse(args[2]),
                args[3],
                args[4]
        );
        fileManager.markChanged();
    }

    /** @return "change" */
    @Override public String getName() { return "change"; }

    /** @return help string. */
    @Override public String getHelp() { return "change <date> <start> <end> <option> <value> - edits event"; }
}