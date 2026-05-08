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

    public void changeEvent(LocalDate date, LocalTime start, LocalTime end, String option, String newValue) {
        Event foundEvent = null;
        for (Event e : events) {
            if (e.getDate().equals(date) && e.getStartTime().equals(start) && e.getEndTime().equals(end)) {
                foundEvent = e;
                break;
            }
        }

        if (foundEvent == null) {
            System.out.println("Error: Event not found.");
            return;
        }

        // Премахваме старото
        events.remove(foundEvent);

        // Подготвяме данните за новото (копираме старите и променяме само избраното)
        LocalDate newDate = foundEvent.getDate();
        LocalTime newStart = foundEvent.getStartTime();
        LocalTime newEnd = foundEvent.getEndTime();
        String newName = foundEvent.getName();
        String newNote = foundEvent.getNote();

        try {
            switch (option.toLowerCase()) {
                case "date": newDate = LocalDate.parse(newValue); break;
                case "starttime": newStart = LocalTime.parse(newValue); break;
                case "endtime": newEnd = LocalTime.parse(newValue); break;
                case "name": newName = newValue; break;
                case "note": newNote = newValue; break;
                default:
                    System.out.println("Error: Invalid option. Use: date, starttime, endtime, name, note.");
                    events.add(foundEvent); // Връщаме го обратно, ако опцията е грешна
                    return;
            }

            Event updatedEvent = new Event(newDate, newStart, newEnd, newName, newNote);
            events.add(updatedEvent);
            System.out.println("Event updated successfully.");
        } catch (Exception e) {
            System.out.println("Error: Could not update event. Check your input format.");
            events.add(foundEvent); // Връщаме оригиналното събитие при грешка
        }
    }

    public void findEvents(String search) {
        String query = search.toLowerCase();
        boolean found = false;
        System.out.println("Search results for '" + search + "':");

        for (Event event : events) {
            // Проверяваме името и бележката (превръщаме в малки букви за по-лесно търсене)
            if (event.getName().toLowerCase().contains(query) ||
                    event.getNote().toLowerCase().contains(query)) {
                System.out.println(event.getDate() + ": " + event);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No events match your search criteria.");
        }
    }

    public void clearEvents() {
        events.clear();
    }
}