package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;

import java.util.Scanner;

public abstract class AbstractCommand implements Command {
    protected final FileManager fileManager;
    protected final CalendarManager calendarManager;
    protected final Scanner scanner;

    public AbstractCommand(FileManager fileManager, CalendarManager calendarManager) {
        this.fileManager = fileManager;
        this.calendarManager = calendarManager;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute(String[] args) {
        // Централизирана проверка за отворен файл
        if (requiresOpenFile() && !fileManager.isOpen()) {
            System.out.println("Error: Please open a file first.");
            return;
        }

        // Проверка за минимален брой аргументи
        if (args.length < getMinArgs()) {
            System.out.println(getUsage());
            return;
        }

        try {
            executeLogic(args);
        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("Error: Invalid date or time format. Please use YYYY-MM-DD and HH:mm.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Expected a number, but got text.");
        } catch (Exception e) {
            System.out.println("Error: An unexpected error occurred: " + e.getMessage());
        }
    }

    protected boolean requiresOpenFile() { return true; }
    protected abstract int getMinArgs();
    protected abstract String getUsage();
    protected abstract void executeLogic(String[] args) throws Exception;
}