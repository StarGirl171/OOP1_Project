package bg.tu_varna.sit.f24621666.core;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventParser {
    public static Event parseEvent(String[] p) {
        try {
            // Очакваме формат: E,дата,старт,край,име,бележка
            return new Event(
                    LocalDate.parse(p[1]),
                    LocalTime.parse(p[2]),
                    LocalTime.parse(p[3]),
                    p[4],
                    p[5]
            );
        } catch (Exception e) {
            System.out.println("Warning: Skipped invalid line.");
            return null;
        }
    }

    public static LocalDate parseHoliday(String[] p) {
        try {
            // Очакваме формат: H,дата
            return LocalDate.parse(p[1]);
        } catch (Exception e) {
            System.out.println("Warning: Skipped invalid line.");
            return null;
        }
    }

    public static String toCSV(Event e) {
        return String.format("E,%s,%s,%s,%s,%s",
                e.getDate(), e.getStartTime(), e.getEndTime(), e.getName(), e.getNote());
    }
}