package bg.tu_varna.sit.f24621666.commands;

import bg.tu_varna.sit.f24621666.core.CalendarManager;
import bg.tu_varna.sit.f24621666.core.FileManager;
import bg.tu_varna.sit.f24621666.exceptions.*;

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
       try {
            // 1. Централизирана проверка дали командата изисква отворен файл
            if (requiresOpenFile() && !fileManager.isOpen()) {
                throw new CalendarException("Error: Please open a file first.");
            }

            // 2. Проверка за минимален брой аргументи, за да избегнем ArrayIndexOutOfBounds
            if (args.length < getMinArgs()) {
                throw new InvalidCommandArgumentException("Error: Not enough arguments.\n" + getUsage());
            }

            // 3. Изпълнение на логиката с прихващане на грешките от най-специфичните към най-общите
               executeLogic(args);
       } catch (InvalidCommandArgumentException e) {
           System.out.println(e.getMessage());
       } catch (DateOverlapException e) {
           System.out.println("CONFLICT: " + e.getMessage());
       } catch (HolidayConflictException e) {
           System.out.println("CALENDAR RESTRICTION: " + e.getMessage());
       } catch (InvalidTimeException e) {
           System.out.println("TIME ERROR: " + e.getMessage());
       } catch (CalendarException e) {
           // Общ хендлър за всички останали наши грешки
           System.out.println(e.getMessage());
       } catch (java.time.format.DateTimeParseException e) {
           System.out.println("FORMAT ERROR: Date/Time must be YYYY-MM-DD / HH:mm.");
       } catch (NumberFormatException e) {
           // Хващаме случаи, в които се очаква число (напр. часове), но е подаден текст
           System.out.println("Error: Expected a number, but got text.");
       } catch (Exception e) {
           // Защита от неочаквани грешки по време на изпълнение
           String msg = (e.getMessage() != null) ? e.getMessage() : "An unknown error occurred.";
           System.out.println("UNEXPECTED ERROR: " + msg);
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