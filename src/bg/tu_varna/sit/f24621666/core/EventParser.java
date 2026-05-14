package bg.tu_varna.sit.f24621666.core;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Utility class responsible for converting strings from files into Event objects
 * and formatting Event objects back into CSV strings for storage.
 */
public class EventParser {
    /**
     * Parses an array of strings into an Event object.
     * Expects a specific order of elements based on the file format.
     * * @param p The array of string parts (e.g., ["E", "2023-10-10", ...]).
     * @return A valid Event object or null if data is corrupted.
     */
    public static Event parseEvent(String[] p) {
        try {
            // Опитваме се да превърнем текстовите данни от файла в реални обекти
            return new Event(
                    LocalDate.parse(p[1]),
                    LocalTime.parse(p[2]),
                    LocalTime.parse(p[3]),
                    p[4],
                    p[5]
            );
        } catch (Exception e) {
            // Ако форматът на датата или часа е грешен, уведомяваме потребителя
            System.out.println("Warning: Skipped invalid line.");
            return null;
        }
    }

    /**
     * Parses an array of strings into a holiday date.
     * * @param p The array of string parts (e.g., ["H", "2023-12-25"]).
     * @return A LocalDate object representing the holiday or null.
     */
    public static LocalDate parseHoliday(String[] p) {
        try {
            // Разчитаме само датата за редовете, маркирани с "H" (Holiday)
            return LocalDate.parse(p[1]);
        } catch (Exception e) {
            System.out.println("Warning: Skipped invalid line.");
            return null;
        }
    }

    /**
     * Converts an Event object into a CSV-compatible string for file saving.
     * * @param e The Event object to serialize.
     * @return A formatted string starting with "E,".
     */
    public static String toCSV(Event e) {
        // Тук дефинираме официалния формат за запис в CSV файла
        return String.format("E,%s,%s,%s,%s,%s",
                e.getDate(), e.getStartTime(), e.getEndTime(), e.getName(), e.getNote());
    }
}