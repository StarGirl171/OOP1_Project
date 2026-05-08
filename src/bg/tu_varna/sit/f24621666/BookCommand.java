package bg.tu_varna.sit.f24621666;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookCommand implements Command {
    private final FileManager fileManager;
    private final CalendarManager calendarManager;

    public BookCommand(FileManager fileManager, CalendarManager calendarManager) {
        this.fileManager = fileManager;
        this.calendarManager = calendarManager;
    }

    @Override
    public void execute(String[] args) {
        if (!fileManager.isOpen()) {
            System.out.println("Грешка: Трябва първо да отворите файл!");
            return;
        }

        if (args.length < 5) {
            System.out.println("Употреба: book <date> <starttime> <endtime> <name> <note>");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(args[0]);
            LocalTime start = LocalTime.parse(args[1]);
            LocalTime end = LocalTime.parse(args[2]);
            String name = args[3];
            String note = args[4];

            Event event = new Event(date, start, end, name, note);
            calendarManager.addEvent(event);
        } catch (Exception e) {
            System.out.println("Грешка: Невалиден формат на данните.");
        }
    }

    @Override
    public String getName() { return "book"; }

    @Override
    public String getHelp() { return "book <date> <starttime> <endtime> <name> <note> - запазва час за среща"; }
}