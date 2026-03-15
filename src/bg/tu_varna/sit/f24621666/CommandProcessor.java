package bg.tu_varna.sit.f24621666;

import java.time.LocalDate;
import java.time.LocalTime;

public class CommandProcessor {

    private final FileManager fileManager = new FileManager();
    private final CalendarManager calendarManager = new CalendarManager();

    public boolean processCommand(String input) {

        String[] parts = input.split(" ");
        String command = parts[0];

        switch (command) {

            case "open":
                if (parts.length < 2) {
                    System.out.println("Usage: open <file>");
                    break;
                }
                fileManager.open(parts[1]);
                break;

            case "close":
                fileManager.close();
                break;

            case "save":
                fileManager.save();
                break;

            case "saveAs":
                if (parts.length < 2) {
                    System.out.println("Usage: saveAs <file>");
                    break;
                }
                fileManager.saveAs(parts[1]);
                break;

            case "help":
                printHelp();
                break;

            case "exit":
                System.out.println("Exiting the program...");
                return false;

            case "book":
                if (parts.length < 6) {
                    System.out.println("Usage: book <date> <starttime> <endtime> <name> <note>");
                    break;
                }

                try {
                    LocalDate date = LocalDate.parse(parts[1]);
                    LocalTime start = LocalTime.parse(parts[2]);
                    LocalTime end = LocalTime.parse(parts[3]);

                    String name = parts[4];
                    String note = parts[5];

                    Event event = new Event(date, start, end, name, note);

                    calendarManager.addEvent(event);

                } catch (Exception e) {
                    System.out.println("Invalid date or time format.");
                }
                break;

            case "agenda":
                if (parts.length < 2) {
                    System.out.println("Usage: agenda <date>");
                    break;
                }

                try {
                    LocalDate date = LocalDate.parse(parts[1]);

                    calendarManager.showAgenda(date);

                } catch (Exception e) {
                    System.out.println("Invalid date format.");
                }
                break;

            default:
                System.out.println("Unknown command");
        }
        return true;
    }

    private void printHelp() {
        System.out.println("The following commands are supported:");

        System.out.println("open <file>      opens <file>");
        System.out.println("close            closes currently opened file");
        System.out.println("save             saves the currently open file");
        System.out.println("saveAs <file>    saves the currently open file in <file>");
        System.out.println("help             prints this information");
        System.out.println("exit             exits the program");
        System.out.println("book <date> <start> <end> <name> <note>  adds event");
        System.out.println("agenda <date>                            shows events for date");
    }
}
