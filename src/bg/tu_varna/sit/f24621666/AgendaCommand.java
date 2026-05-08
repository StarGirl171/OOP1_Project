package bg.tu_varna.sit.f24621666;

import java.time.LocalDate;

public class AgendaCommand implements Command {
    private final FileManager fileManager;
    private final CalendarManager calendarManager;

    public AgendaCommand(FileManager fileManager, CalendarManager calendarManager) {
        this.fileManager = fileManager;
        this.calendarManager = calendarManager;
    }

    @Override
    public void execute(String[] args) {
        if (!fileManager.isOpen()) {
            System.out.println("Грешка: Трябва първо да отворите файл!");
            return;
        }

        if (args.length < 1) {
            System.out.println("Употреба: agenda <date>");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(args[0]);
            calendarManager.showAgenda(date);
        } catch (Exception e) {
            System.out.println("Грешка: Невалиден формат на датата.");
        }
    }

    @Override
    public String getName() { return "agenda"; }

    @Override
    public String getHelp() { return "agenda <date> - извежда списък с ангажименти за деня"; }
}