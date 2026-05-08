package bg.tu_varna.sit.f24621666;

import java.io.*;

public class FileManager {

    private File currentFile;
    private boolean isOpen = false;
    private final CalendarManager calendarManager; // Връзка с данните

    public FileManager(CalendarManager calendarManager) {
        this.calendarManager = calendarManager;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open(String path) {
        try {
            currentFile = new File(path);

            if (!currentFile.exists()) {
                currentFile.createNewFile();
                System.out.println("Created new empty file:" + currentFile.getName());
            } else {
                // Четем съществуващите данни
                try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        calendarManager.loadEventFromLine(line);
                    }
                }
            }

            isOpen = true;
            System.out.println("Successfully opened " + currentFile.getName());

        } catch (IOException e) {
            System.out.println("Error: Could not open file.");
        }
    }

    public void close() {
        if (!isOpen) {
            System.out.println("No file is currently open");
            return;
        }
        // При затваряне изчистваме текущите данни от паметта
        calendarManager.clearEvents();
        currentFile = null;
        isOpen = false;

        System.out.println("Successfully closed file");
    }

    public void save() {

        if (!isOpen()) {
            System.out.println("Error: No file is currently open.");
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(currentFile))) {
            // Вземаме всички събития и ги записваме във формат: дата,старт,край,име,бележка
            for (Event e : calendarManager.getAllEvents()) {
                writer.println(e.getDate() + "," + e.getStartTime() + "," + e.getEndTime() + "," + e.getName() + "," + e.getNote());
            }
            System.out.println("Successfully saved to " + currentFile.getName());
        } catch (IOException e) {
            System.out.println("Error: Could not save file.");
        }
    }

    public void saveAs(String path) {
        if (isOpen) return;

        try {
            File newFile = new File(path);

            FileWriter writer = new FileWriter(newFile);
            writer.write("");
            writer.close();

            currentFile = newFile;

            System.out.println("Successfully saved " + newFile.getName());

        } catch (IOException e) {
            System.out.println("Error saving file");
        }
    }
}
