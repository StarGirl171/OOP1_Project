package bg.tu_varna.sit.f24621666.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * Manages file I/O operations and tracks the state of the currently opened file.
 * Ensures data integrity by forcing UTF-8 encoding and tracking unsaved changes.
 */
public class FileManager {

    /** The file currently being manipulated. */
    private File currentFile;

    /** Status flag indicating if a file is currently loaded in the system. */
    private boolean isOpen = false;

    /** Status flag indicating if modifications were made since the last save. */
    private boolean hasUnsavedChanges = false;

    /** Reference to the calendar manager to load/save events. */
    private final CalendarManager calendarManager;

    /**
     * Constructs the file manager with a reference to the calendar logic.
     * @param calendarManager The calendar manager to interact with.
     */
    public FileManager(CalendarManager calendarManager) {
        // Свързваме файловия мениджър с календара, за да може да пълни данните му
        this.calendarManager = calendarManager;
    }

    /**
     * Checks if a file is currently open.
     * @return true if a file session is active.
     */
    public boolean isOpen() { return isOpen; }

    /**
     * Marks the current session as containing unsaved data.
     * Should be called by any command that modifies the calendar.
     */
    public void markChanged() { this.hasUnsavedChanges = true; }

    /**
     * Opens a file from the disk and loads its content into the calendar.
     * @param path The system path to the file.
     */
    public void open(String path) {
        // Проверяваме дали потребителят не се опитва да отвори нов файл, докато има незапазени промени
        if (isOpen && hasUnsavedChanges) {
            System.out.println("Warning: You have unsaved changes. Please 'save' or 'close' first.");
            return;
        }

        try {
            currentFile = new File(path);
            calendarManager.clearEvents();

            // Ако файлът не съществува, създаваме нов празен файл
            if (currentFile.createNewFile()) {
                System.out.println("Created new empty file: " + path);
            } else {
                // Използваме InputStreamReader с UTF-8, за да поддържаме правилно кирилица
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(currentFile), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        calendarManager.loadLine(line);
                    }
                }
            }
            isOpen = true;
            hasUnsavedChanges = false;
            System.out.println("Successfully opened " + path);
        } catch (IOException e) {
            System.out.println("Error: Operation failed. Check path or permissions.");
        }
    }

    /**
     * Closes the current file session and clears the calendar data.
     * Prevents closing if there are unsaved changes.
     */
    public void close() {
        if (hasUnsavedChanges) {
            System.out.println("Warning: Unsaved changes will be lost. Save first. Aborting close.");
            return;
        }
        // Изчистваме паметта и състоянието на мениджъра
        calendarManager.clearEvents();
        currentFile = null;
        isOpen = false;
        System.out.println("File closed.");
    }

    /**
     * Persists the current calendar data back to the disk in CSV format.
     */
    public void save() {
        if (!isOpen) return;
        // Записваме данните обратно в оригиналния файл
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(currentFile), StandardCharsets.UTF_8))) {
            for (Event e : calendarManager.getAllEvents()) {
                writer.println(EventParser.toCSV(e));
            }
            for (LocalDate date : calendarManager.getAllHolidays()) {
                writer.println("H," + date);
            }
            hasUnsavedChanges = false;
            System.out.println("Successfully saved changes.");
        } catch (IOException e) {
            System.out.println("Error: Save failed.");
        }
    }

    /**
     * Saves the current data to a new file path and updates the current file pointer.
     * @param path The new path for the file.
     */
    public void saveAs(String path) {
        if (!isOpen) return;
        currentFile = new File(path);
        save();
    }
}