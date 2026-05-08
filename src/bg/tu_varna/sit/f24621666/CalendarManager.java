package bg.tu_varna.sit.f24621666;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CalendarManager {
    private final List<Event> events = new ArrayList<>();

    public void addEvent(Event event) {
        events.add(event);
        System.out.println("Event added successfully.");
    }

    // Метод за изтриване на събитие
    public void removeEvent(LocalDate date, LocalTime startTime, LocalTime endTime) {

        Iterator<Event> iterator = events.iterator();
        boolean found = false;

        while (iterator.hasNext()) {
            Event e = iterator.next();
            // Проверяваме дали съвпадат датата и часовете
            if (e.getDate().equals(date) && e.getStartTime().equals(startTime) && e.getEndTime().equals(endTime)) {
                iterator.remove();
                found = true;
                break;
            }
        }

        if (found) {
            System.out.println("Event unbooked successfully.");
        } else {
            System.out.println("Error: No such event found on this date and time.");
        }
    }

    public void showAgenda(LocalDate date) {

        System.out.println("Agenda for " + date + ":");
        boolean hasEvents = false;

        for (Event event : events) {
            if (event.getDate().equals(date)) {
                System.out.println(event);
                hasEvents = true;
            }
        }
        if (!hasEvents) {
            System.out.println("No events scheduled for this day.");
        }
    }

    public void clearEvents() {
        events.clear();
    }
}