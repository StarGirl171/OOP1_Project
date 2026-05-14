package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;
import java.util.Scanner;

/**
 * Base abstract class that provides common functionality and validation for all commands.
 * Implements Template Method pattern for command execution.
 */
public abstract class AbstractCommand implements Command {

    /** Reference to the file manager for I/O operations. */
    protected final FileManager fileManager;

    /** Reference to the calendar manager for business logic. */
    protected final CalendarManager calendarManager;

    /** Scanner instance for commands that require additional user interaction (e.g., Merge). */
    protected final Scanner scanner;

    /**
     * Constructs the abstract command with necessary managers and initializes the scanner.
     * @param fileManager Reference to the system's file manager.
     * @param calendarManager Reference to the system's calendar manager.
     */
    public AbstractCommand(FileManager fileManager, CalendarManager calendarManager) {
        // Инициализираме общите ресурси, които всяка команда ще ползва
        this.fileManager = fileManager;
        this.calendarManager = calendarManager;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Template method that handles validation and error catching before calling specific logic.
     * @param args Array of command arguments.
     */
    @Override
    public void execute(String[] args) {
        // 1. Централизирана проверка дали командата изисква отворен файл
        if (requiresOpenFile() && !fileManager.isOpen()) {
            System.out.println("Error: Please open a file first.");
            return;
        }

        // 2. Проверка за минимален брой аргументи, за да избегнем ArrayIndexOutOfBounds
        if (args.length < getMinArgs()) {
            System.out.println(getUsage());
            return;
        }

        // 3. Изпълнение на логиката с общо прихващане на най-честите грешки
        try {
            executeLogic(args);
        } catch (java.time.format.DateTimeParseException e) {
            // Автоматично хващаме грешни формати на дати/час за всички команди
            System.out.println("Error: Invalid date or time format. Please use YYYY-MM-DD and HH:mm.");
        } catch (NumberFormatException e) {
            // Хващаме случаи, в които се очаква число (напр. часове), но е подаден текст
            System.out.println("Error: Expected a number, but got text.");
        } catch (Exception e) {
            // Защита от неочаквани грешки по време на изпълнение
            System.out.println("Error: An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Defines if a file must be open for this command to work. Defaults to true.
     * @return true if command requires an open file.
     */
    protected boolean requiresOpenFile() {
        // По подразбиране приемаме, че ни трябва файл (повечето команди са така)
        return true;
    }

    /**
     * Abstract method to define the minimum arguments required by the subclass.
     * @return Minimum number of arguments.
     */
    protected abstract int getMinArgs();

    /**
     * Abstract method to provide the correct usage syntax for the command.
     * @return Usage instruction string.
     */
    protected abstract String getUsage();

    /**
     * The specific logic of each command. Throws Exception to be caught by the template execute method.
     * @param args Array of validated arguments.
     * @throws Exception potentially thrown during execution.
     */
    protected abstract void executeLogic(String[] args) throws Exception;
}