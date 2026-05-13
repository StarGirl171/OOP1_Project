package bg.tu_varna.sit.f24621666.core;

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

    public boolean isValid() {
        if (startTime == null || endTime == null) return false;
        return startTime.isBefore(endTime);
    }

    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public String getName() { return name; }
    public String getNote() { return note; }
    public LocalDate getDate() { return date; }

    @Override
    public String toString() {
        return String.format("%s-%s | %s | %s", startTime, endTime, name, note);
    }
}