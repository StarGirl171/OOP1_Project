package bg.tu_varna.sit.f24621666;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarManager {
    private final List<Event> events = new ArrayList<>();

    public void addEvent(Event event) {
        events.add(event);
        System.out.println("Event added successfully.");
    }

    public void showAgenda(LocalDate date) {
        System.out.println("Agenda for " + date);

        for (Event event : events) {
            if (event.getDate().equals(date)) {
                System.out.println(event);
            }
        }
    }
}