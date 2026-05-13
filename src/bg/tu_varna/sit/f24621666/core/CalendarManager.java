package bg.tu_varna.sit.f24621666.core;

import java.io.BufferedReader;
import java.io.FileReader;
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

    private void handleConflict(Event existing, Event other, Scanner scanner) {
        System.out.println("\nCONFLICT found for date " + other.getDate());
        System.out.println("Existing: " + existing);
        System.out.println("New from file: " + other);
        System.out.print("Choose: [1] Keep Existing, [2] Replace with New, [3] Move New to other time: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "2":
                events.remove(existing);
                events.add(other);
                System.out.println("Event replaced.");
                break;
            case "3":
                moveEventToNewTime(other, scanner);
                break;
            default:
                System.out.println("Keeping existing event.");
                break;
        }
    }

    private void moveEventToNewTime(Event event, Scanner scanner) {
        System.out.println("Enter new start and end time (HH:mm HH:mm):");
        try {
            String[] times = scanner.nextLine().split(" ");
            LocalTime newS = LocalTime.parse(times[0]);
            LocalTime newE = LocalTime.parse(times[1]);

            if (isSlotFree(event.getDate(), newS, newE)) {
                events.add(new Event(event.getDate(), newS, newE, event.getName(), event.getNote()));
                System.out.println("Moved and added.");
            } else {
                System.out.println("Error: New slot is occupied.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Skipping event.");
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

    void loadEventFromLine(String line) {
        try {
            String[] p = line.split(",");

            if (p[0].equalsIgnoreCase("E") && p.length == 6) {
                Event event = parseEvent(p);

                if (event != null) {
                    events.add(event);
                }
            } else if (p[0].equalsIgnoreCase("H") && p.length == 2) {
                holidays.add(LocalDate.parse(p[1]));
            }
        } catch (Exception e) {
            // Пропускаме невалидни редове
        }
    }

    // Помощен метод, който зарежда събития от ВЪНШЕН файл, без да променя текущите
    public List<Event> loadEventsFromFile(String filePath) {

        List<Event> externalEvents = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = reader.readLine()) != null) {

                String[] p = line.split(",");
                if (p[0].equalsIgnoreCase("E") && p.length == 6) {

                    Event event = parseEvent(p);
                    if (event != null) externalEvents.add(event);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading external calendar: " + filePath);
        }
        return externalEvents;
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