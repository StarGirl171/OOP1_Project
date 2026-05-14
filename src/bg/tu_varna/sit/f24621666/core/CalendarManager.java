package bg.tu_varna.sit.f24621666.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class CalendarManager {
    private final List<Event> events = new ArrayList<>();
    private final Set<LocalDate> holidays = new HashSet<>();

    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }

    public Set<LocalDate> getAllHolidays() {
        return new HashSet<>(holidays);
    }

    public boolean addEvent(Event event) {
        if (!event.isValid()) {
            System.out.println("Error: Invalid event time range (start must be before end).");
            return false;
        }

        if (event.getDate().isBefore(LocalDate.now())) {
            System.out.println("Warning: You are booking an event in the past.");
        }

        if (isSlotFree(event.getDate(), event.getStartTime(), event.getEndTime())) {
            events.add(event);
            System.out.println("Event added successfully.");
            return true;
        } else {
            System.out.println("Error: The slot is already occupied or invalid.");
            return false;
        }
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

        // Проверяваме дали е празник
        if (isHoliday(date)) {
            System.out.println("[HOLIDAY / NON-WORKING DAY]");
            hasEvents = true;
        }

        for (Event event : events) {
            if (event.getDate().equals(date)) {
                System.out.println(event);
                hasEvents = true;
            }
        }
        if (!hasEvents) {
            System.out.println("No events or holidays scheduled for this day.");
        }
    }

    public void changeEvent(LocalDate date, LocalTime start, LocalTime end, String option, String newValue) {
        Event found = findEvent(date, start, end);
        if (found == null) {
            System.out.println("Error: Event not found.");
            return;
        }

        events.remove(found);
        Event updated = createUpdatedEvent(found, option, newValue);

        if (updated != null && isSlotFree(updated.getDate(), updated.getStartTime(), updated.getEndTime())) {
            events.add(updated);
            System.out.println("Event updated successfully.");
        } else {
            events.add(found); // Връщаме оригинала при грешка или заето място
            if (updated != null) System.out.println("Error: New slot is occupied.");
        }
    }

    private Event findEvent(LocalDate date, LocalTime start, LocalTime end) {
        return events.stream()
                .filter(e -> e.getDate().equals(date) && e.getStartTime().equals(start) && e.getEndTime().equals(end))
                .findFirst().orElse(null);
    }

    private Event createUpdatedEvent(Event e, String opt, String val) {
        try {
            LocalDate d = e.getDate();
            LocalTime s = e.getStartTime();
            LocalTime end = e.getEndTime();
            String name = e.getName();
            String note = e.getNote();

            switch (opt.toLowerCase()) {
                case "date": d = LocalDate.parse(val); break;
                case "starttime": s = LocalTime.parse(val); break;
                case "endtime": end = LocalTime.parse(val); break;
                case "name": name = val; break;
                case "note": note = val; break;
                default: return null;
            }
            return new Event(d, s, end, name, note);
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean isDayWorkable(LocalDate date) {
        return date.getDayOfWeek() != DayOfWeek.SATURDAY &&
                date.getDayOfWeek() != DayOfWeek.SUNDAY &&
                !isHoliday(date);
    }

    private boolean isSlotFree(LocalDate date, LocalTime start, LocalTime end) {
        return isSlotFreeInList(this.events, date, start, end);
    }

    public void findSlot(LocalDate fromDate, int hours) {
        findSlotWith(fromDate, hours, new ArrayList<>());
    }

    public void findSlotWith(LocalDate startDate, int durationHours, List<List<Event>> externalCalendars) {
        LocalDate currentDate = startDate;
        int daysSearched = 0;

        // Обединяваме всички събития от всички календари
        List<Event> allRelevantEvents = new ArrayList<>(this.events);
        for (List<Event> external : externalCalendars) {
            allRelevantEvents.addAll(external);
        }

        while (daysSearched < 365) {
            if (isDayWorkable(currentDate)) {
                // ИЗПОЛЗВАМЕ НОВИЯ ПОМОЩЕН МЕТОД ТУК
                LocalTime foundTime = findFreeHourInDay(currentDate, durationHours, allRelevantEvents);

                if (foundTime != null) {
                    System.out.println("Found slot: " + currentDate + " from " + foundTime + " to " + foundTime.plusHours(durationHours));
                    return;
                }
            }
            currentDate = currentDate.plusDays(1);
            daysSearched++;
        }
        System.out.println("No free slot found for the given duration.");
    }

    private LocalTime findFreeHourInDay(LocalDate date, int duration, List<Event> eventsToCheck) {
        LocalTime startLimit = LocalTime.of(8, 0);
        LocalTime endLimit = LocalTime.of(17, 0);

        for (int h = startLimit.getHour(); h <= endLimit.getHour() - duration; h++) {
            LocalTime potentialStart = LocalTime.of(h, 0);
            LocalTime potentialEnd = potentialStart.plusHours(duration);

            if (isSlotFreeInList(eventsToCheck, date, potentialStart, potentialEnd)) {
                return potentialStart; // Връщаме намерения час
            }
        }
        return null; // Нищо не сме намерили в този ден
    }

    // Помощен метод за проверка на конфликти в списък от събития
    private boolean isSlotFreeInList(List<Event> eventList, LocalDate date, LocalTime start, LocalTime end) {
        for (Event e : eventList) {
            if (e.getDate().equals(date)) {
                // Проверка за застъпване на часовете
                if (!(end.isBefore(e.getStartTime()) || start.isAfter(e.getEndTime()) ||
                        end.equals(e.getStartTime()) || start.equals(e.getEndTime()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void mergeWithCalendars(List<String> filePaths, Scanner scanner) {
        for (String path : filePaths) {
            System.out.println("Merging with: " + path);
            List<Event> externalEvents = loadEventsFromFile(path);

            for (Event other : externalEvents) {
                // ПЪРВО: Проверяваме дали събитието вече съществува 1 към 1
                if (isEventAlreadyPresent(other)) {
                    continue; // Прескачаме го тихо, защото е същото
                }

                // ВТОРО: Ако не е същото, търсим конфликт в часовете
                Event conflict = findConflictingEvent(other);

                if (conflict == null) {
                    events.add(other);
                } else {
                    handleConflict(conflict, other, scanner);
                }
            }
        }
        System.out.println("Merge process completed.");
    }

    // Помощен метод за проверка на 100% еднакви събития
    private boolean isEventAlreadyPresent(Event other) {
        for (Event e : events) {
            if (e.getDate().equals(other.getDate()) &&
                    e.getStartTime().equals(other.getStartTime()) &&
                    e.getEndTime().equals(other.getEndTime()) &&
                    e.getName().equals(other.getName()) &&
                    e.getNote().equals(other.getNote())) {
                return true;
            }
        }
        return false;
    }

    private void handleConflict(Event existing, Event other, Scanner scanner) {
        System.out.println("\n--- CONFLICT on " + other.getDate() + " ---");
        System.out.println("[E] Existing: " + existing);
        System.out.println("[N] New:      " + other);
        System.out.println("Options:");
        System.out.println("1. Keep Existing (Discard New)");
        System.out.println("2. Replace Existing with New");
        System.out.println("3. Move NEW event to another time");
        System.out.println("4. Move EXISTING event to another time and keep New here");
        System.out.println("5. Move BOTH to new times");
        System.out.print("Choice: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.println("Keeping existing.");
                break;
            case "2":
                events.remove(existing);
                events.add(other);
                System.out.println("Replaced.");
                break;
            case "3":
                moveEventWithRetry(other, scanner);
                break;
            case "4":
                if (moveEventWithRetry(existing, scanner)) {
                    events.remove(existing);
                    events.add(other);
                    System.out.println("Existing moved, New placed in its old slot.");
                }
                break;
            case "5":
                System.out.println("Moving both events...");
                if (moveEventWithRetry(existing, scanner)) {
                    events.remove(existing);
                    moveEventWithRetry(other, scanner);
                }
                break;
            default:
                System.out.println("Invalid choice. Skipping conflict.");
                break;
        }
    }

    // Подобрен метод с цикъл (Retry), за да не "изхвърля" при една грешка
    private boolean moveEventWithRetry(Event event, Scanner scanner) {
        while (true) {
            System.out.println("Enter new start and end time for [" + event.getName() + "] (HH:mm HH:mm) or 'cancel':");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("cancel")) return false;

            try {
                String[] times = input.split("\\s+");
                if (times.length < 2) throw new Exception();

                LocalTime newS = LocalTime.parse(times[0]);
                LocalTime newE = LocalTime.parse(times[1]);

                if (newS.isAfter(newE) || newS.equals(newE)) {
                    System.out.println("Error: Start time must be before end time.");
                    continue;
                }

                if (isSlotFree(event.getDate(), newS, newE)) {
                    events.add(new Event(event.getDate(), newS, newE, event.getName(), event.getNote()));
                    System.out.println("Successfully moved.");
                    return true;
                } else {
                    System.out.println("Error: That slot is already occupied. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: Invalid time format (use HH:mm HH:mm). Try again.");
            }
        }
    }

    // Помощен метод за намиране на конкретно застъпващо се събитие
    private Event findConflictingEvent(Event other) {
        for (Event e : events) {
            if (e.getDate().equals(other.getDate())) {
                if (other.getStartTime().isBefore(e.getEndTime()) && other.getEndTime().isAfter(e.getStartTime())) {
                    return e;
                }
            }
        }
        return null;
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

    public void addHoliday(LocalDate date) {
        if (!holidays.add(date)) {
            System.out.println("Error: " + date + " is already marked as a holiday.");
        } else {
            System.out.println("Date " + date + " is now marked as a holiday.");
        }
    }

    public boolean isHoliday(LocalDate date) {
        return holidays.contains(date);
    }

    public void showBusyDays(LocalDate from, LocalDate to) {
        Map<LocalDate, Long> busyHoursMap = new HashMap<>();

        // Обхождаме всички събития
        for (Event e : events) {
            // Проверяваме дали събитието е в периода [from, to]
            if ((e.getDate().isEqual(from) || e.getDate().isAfter(from)) &&
                    (e.getDate().isEqual(to) || e.getDate().isBefore(to))) {

                // Пресмятаме продължителността в минути
                long minutes = Duration.between(e.getStartTime(), e.getEndTime()).toMinutes();
                busyHoursMap.put(e.getDate(), busyHoursMap.getOrDefault(e.getDate(), 0L) + minutes);
            }
        }

        if (busyHoursMap.isEmpty()) {
            System.out.println("No events found in this period.");
        } else {
            busyHoursMap.entrySet().stream()
                    .sorted(Map.Entry.<LocalDate, Long>comparingByValue().reversed())
                    .forEach(entry -> System.out.println(entry.getKey() + ": " + (entry.getValue() / 60) + "h " + (entry.getValue() % 60) + "m"));
        }
    }

    public void loadLine(String line) {
        String[] parts = line.split(",");
        Event e = EventParser.parseEvent(parts);
        if (e != null) {
            events.add(e);
            return;
        }
        LocalDate h = EventParser.parseHoliday(parts);
        if (h != null) holidays.add(h);
    }

    public List<Event> loadEventsFromFile(String filePath) {
        List<Event> external = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Event e = EventParser.parseEvent(line.split(","));
                if (e != null) external.add(e);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
        }
        return external;
    }

    private Event parseEvent(String[] p) {
        try {
            return new Event(
                    LocalDate.parse(p[1]),
                    LocalTime.parse(p[2]),
                    LocalTime.parse(p[3]),
                    p[4],
                    p[5]
            );
        } catch (Exception e) {
            return null; // Връщаме null, ако данните са грешни
        }
    }

    public void clearEvents() {
        events.clear();
        holidays.clear();
    }
}