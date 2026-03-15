package bg.tu_varna.sit.f24621666;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String name;
    private final String note;

    public Event(LocalDate date, LocalTime startTime, LocalTime endTime, String name, String note) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.note = note;
    }

    public LocalDate getDate() {
        return date;
    }

    public String toString() {
        return startTime + "-" + endTime + " | " + name + " | " + note;
    }
}