package bg.tu_varna.sit.f24621666.core;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a single calendar event with a date, time range, name, and note.
 * Provides validation and identity checks for events.
 */
public class Event {
    /** The specific date of the event. */
    private final LocalDate date;

    /** The time the event is scheduled to begin. */
    private final LocalTime startTime;

    /** The time the event is scheduled to end. */
    private final LocalTime endTime;

    /** The title or name of the event. */
    private final String name;

    /** Additional details or comments regarding the event. */
    private final String note;

    /**
     * Constructs a new Event with full details.
     * * @param date The date of the event.
     * @param startTime Starting time.
     * @param endTime Ending time.
     * @param name Name of the appointment.
     * @param note Personal notes.
     */
    public Event(LocalDate date, LocalTime startTime, LocalTime endTime, String name, String note) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.note = note;
    }

    /**
     * Represents a calendar entry.
     * Checks if the start time is strictly before the end time using custom validation logic.
     * * @return true if the time range is valid, false otherwise.
     */
    public boolean isValid() {
        // Проверяваме дали началният час е преди крайния
        if (startTime == null || endTime == null) return false;
        return startTime.isBefore(endTime);
    }

    /**
     * Gets the starting time of the event.
     * @return LocalTime object of the start.
     */
    public LocalTime getStartTime() { return startTime; }
    /**
     * Gets the ending time of the event.
     * @return LocalTime object of the end.
     */
    public LocalTime getEndTime() { return endTime; }
    /**
     * Gets the name of the event.
     * @return String containing the name.
     */
    public String getName() { return name; }
    /**
     * Gets the additional notes of the event.
     * @return String containing the notes.
     */
    public String getNote() { return note; }
    /**
     * Gets the date of the event.
     * @return LocalDate object of the event date.
     */
    public LocalDate getDate() { return date; }

    /**
     * Compares this event to another object for equality.
     * Two events are equal if all their fields (date, times, name, note) match.
     * * @param o Object to compare with.
     * @return true if objects are identical in content.
     */
    @Override
    public boolean equals(Object o) {
        // Сравняваме съдържанието на обектите, а не само техните референции в паметта
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return date.equals(event.date) &&
                startTime.equals(event.startTime) &&
                endTime.equals(event.endTime) &&
                name.equals(event.name) &&
                note.equals(event.note);
    }

    /**
     * Generates a hash code for the event.
     * @return int representing the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(date, startTime, endTime, name, note);
    }

    /**
     * Returns a formatted string.
     * Uses StringBuilder to avoid unnecessary object creation in memory.
     * @return Formatted string.
     */
    @Override
    public String toString() {
        // StringBuilder е по-бърз и по-оптимизиран за слепване на текст
        StringBuilder sb = new StringBuilder();
        sb.append(startTime)
                .append(" - ")
                .append(endTime)
                .append(" | ")
                .append(name);

        if (note != null && !note.isEmpty()) {
            sb.append(" [").append(note).append("]");
        }
        return sb.toString();
    }
}