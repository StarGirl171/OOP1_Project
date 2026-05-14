package bg.tu_varna.sit.f24621666.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

            if (currentFile.createNewFile()) {
                System.out.println("Created new empty file: " + path);
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
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
            System.out.println("Error: Operation failed.");
        }
    }

    public void close() {
        if (hasUnsavedChanges) {
            System.out.println("Warning: Unsaved changes will be lost.");
            return;
        }
        calendarManager.clearEvents();
        currentFile = null;
        isOpen = false;
        hasUnsavedChanges = false;
        System.out.println("File closed.");
    }

    public void save() {
        if (!isOpen) return;
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

    public void saveAs(String path) {
        if (!isOpen) return;
        currentFile = new File(path);
        save();
    }
}
