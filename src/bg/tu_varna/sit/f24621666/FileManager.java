package bg.tu_varna.sit.f24621666;

import java.io.*;
import java.time.LocalDate;

public class FileManager {

    private File currentFile;
    private boolean isOpen = false;
    private boolean hasUnsavedChanges = false;
    private final CalendarManager calendarManager;

    public FileManager(CalendarManager calendarManager) {
        this.calendarManager = calendarManager;
    }

    public boolean isOpen() { return isOpen; }
    public void markChanged() { this.hasUnsavedChanges = true; }

    public void open(String path) {
        if (isOpen && hasUnsavedChanges) {
            System.out.println("Warning: You have unsaved changes. Please 'save' or 'close' first.");
            return;
        }

        try {
            currentFile = new File(path);
            calendarManager.clearEvents();

            if (!currentFile.exists()) {
                currentFile.createNewFile();
                System.out.println("Created new empty file: " + path);
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        calendarManager.loadEventFromLine(line);
                    }
                }
            }
            isOpen = true;
            hasUnsavedChanges = false;
            System.out.println("Successfully opened " + currentFile.getName());
        } catch (IOException e) {
            System.out.println("Error: Operation failed.");
        }
    }

    public void close() {
        if (hasUnsavedChanges) {
            System.out.println("Warning: Unsaved changes will be lost.");
        }
        calendarManager.clearEvents();
        currentFile = null;
        isOpen = false;
        hasUnsavedChanges = false;
        System.out.println("File closed.");
    }

    public void save() {
        if (!isOpen) return;
        try (PrintWriter writer = new PrintWriter(new FileWriter(currentFile))) {
            for (Event e : calendarManager.getAllEvents()) {
                writer.println("E," + e.getDate() + "," + e.getStartTime() + "," + e.getEndTime() + "," + e.getName() + "," + e.getNote());
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

    public void saveAs(String path) {
        if (!isOpen) return;
        currentFile = new File(path);
        save();
    }
}
