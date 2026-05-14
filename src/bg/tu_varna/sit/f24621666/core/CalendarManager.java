package bg.tu_varna.sit.f24621666.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Manages the collection of events and holidays.
 * Handles the core business logic including scheduling, conflict resolution,
 * searching, and agenda generation.
 */
public class CalendarManager {
    /** List of all scheduled events in the current session. */
    private final List<Event> events = new ArrayList<>();

    /** Set of non-working days (holidays). */
    private final Set<LocalDate> holidays = new HashSet<>();

    /**
     * Returns a copy of all currently loaded events.
     * @return A new list containing all events.
     */
    public List<Event> getAllEvents() {
        // Връщаме нов списък, за да не може външен клас да променя оригиналния списък директно
        return new ArrayList<>(events);
    }

    /**
     * Returns a copy of all marked holidays.
     * @return A set of holiday dates.
     */
    public Set<LocalDate> getAllHolidays() {
        return new HashSet<>(holidays);
    }

    /**
     * Adds a new event to the calendar if the time slot is available.
     * * @param event The event to be added.
     * @return true if addition was successful, false if slot is taken or invalid.
     */
    public boolean addEvent(Event event) {
        // Валидираме събитието преди всичко останало
        if (!event.isValid()) {
            System.out.println("Error: Invalid event time range (start must be before end).");
            return false;
        }

        // Проверка дали потребителят не планира нещо в миналото
        if (event.getDate().isBefore(LocalDate.now())) {
            System.out.println("Warning: You are booking an event in the past.");
        }

        // Проверяваме за застъпване с други събития
        if (isSlotFree(event.getDate(), event.getStartTime(), event.getEndTime())) {
            events.add(event);
            System.out.println("Event added successfully.");
            return true;
        } else {
            System.out.println("Error: The slot is already occupied or invalid.");
            return false;
        }
    }

    /**
     * Removes an event from the calendar based on its unique time and date.
     * * @param LocalDate date of the event.
     * @param startTime Start time of the event.
     * @param endTime End time of the event.
     */
    public void removeEvent(LocalDate date, LocalTime startTime, LocalTime endTime) {
        // Използваме ламбда израз, за да намерим и премахнем съвпадащото събитие
        boolean removed = events.removeIf(e -> e.getDate().equals(date) &&
                e.getStartTime().equals(startTime) &&
                e.getEndTime().equals(endTime));

        if (removed) {
            System.out.println("Event unbooked successfully.");
        } else {
            System.out.println("Error: No such event found on this date and time.");
        }
    }

    /**
     * Displays all events and holiday status for a specific date.
     * @param date The date to inspect.
     */
    public void showAgenda(LocalDate date) {
        System.out.println("Agenda for " + date + ":");
        boolean hasEvents = false;

        // Първо проверяваме дали денят е официален празник
        if (isHoliday(date)) {
            System.out.println("[HOLIDAY / NON-WORKING DAY]");
            hasEvents = true;
        }

        // Обхождаме и принтираме всички събития за тази дата
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

    /**
     * Updates an existing event with new details.
     * Replaces the event only if the new time slot is free.
     * @param date Date of the original event.
     * @param start Start time of the original event.
     * @param end End time of the original event.
     * @param option The field to update (date, starttime, endtime, name, note).
     * @param newValue The new value for the field.
     */
    public void changeEvent(LocalDate date, LocalTime start, LocalTime end, String option, String newValue) {
        // Търсим оригиналното събитие в списъка
        Event found = findEvent(date, start, end);
        if (found == null) {
            System.out.println("Error: Event not found.");
            return;
        }

        // Временно премахваме старото събитие, за да не пречи при проверката за свободен слот
        events.remove(found);
        Event updated = createUpdatedEvent(found, option, newValue);

        // Ако новото събитие е валидно и слотът му е свободен, го добавяме
        if (updated != null && isSlotFree(updated.getDate(), updated.getStartTime(), updated.getEndTime())) {
            events.add(updated);
            System.out.println("Event updated successfully.");
        } else {
            // Ако има проблем (заето място или грешна дата), връщаме оригинала
            events.add(found);
            if (updated != null) System.out.println("Error: New slot is occupied.");
        }
    }

    /**
     * Internal helper to find an event by its primary keys (date and time).
     * @param date Targeted date.
     * @param start Targeted start time.
     * @param end Targeted end time.
     * @return The Event object if found, otherwise null.
     */
    private Event findEvent(LocalDate date, LocalTime start, LocalTime end) {
        // Използваме Stream API за бързо намиране на първото съвпадащо събитие
        return events.stream()
                .filter(e -> e.getDate().equals(date) && e.getStartTime().equals(start) && e.getEndTime().equals(end))
                .findFirst().orElse(null);
    }

    /**
     * Factory-style method to create a new Event based on an existing one with one modified field.
     * @param e Original event.
     * @param opt Field to change.
     * @param val New value as string.
     * @return New Event object or null if parsing fails.
     */
    private Event createUpdatedEvent(Event e, String opt, String val) {
        try {
            LocalDate d = e.getDate();
            LocalTime s = e.getStartTime();
            LocalTime end = e.getEndTime();
            String name = e.getName();
            String note = e.getNote();

            // Използваме switch, за да определим коя част от обекта се редактира
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

    /**
     * Checks if a specific day is suitable for business appointments.
     * A day is workable if it's not a weekend and not a holiday.
     * @param date Date to check.
     * @return true if it's a working day.
     */
    private boolean isDayWorkable(LocalDate date) {
        // Проверяваме дали денят е събота, неделя или е в списъка с празници
        return date.getDayOfWeek() != DayOfWeek.SATURDAY &&
                date.getDayOfWeek() != DayOfWeek.SUNDAY &&
                !isHoliday(date);
    }

    /**
     * Checks if a time slot is free in the current calendar's event list.
     * @param date Targeted date.
     * @param start Targeted start time.
     * @param end Targeted end time.
     * @return true if no overlap is found.
     */
    private boolean isSlotFree(LocalDate date, LocalTime start, LocalTime end) {
        return isSlotFreeInList(this.events, date, start, end);
    }

    /**
     * Finds the first available slot in the current calendar starting from a date.
     * @param fromDate Starting search date.
     * @param hours Required duration in hours.
     */
    public void findSlot(LocalDate fromDate, int hours) {
        // Извикваме по-общия метод без външни календари
        findSlotWith(fromDate, hours, new ArrayList<>());
    }

    /**
     * Advanced search for a free slot considering multiple external calendars.
     * @param startDate Starting search date.
     * @param durationHours Required duration in hours.
     * @param externalCalendars List of event lists from other files.
     */
    public void findSlotWith(LocalDate startDate, int durationHours, List<List<Event>> externalCalendars) {
        LocalDate currentDate = startDate;
        int daysSearched = 0;

        // Създаваме временен списък с всички събития от всички източници за проверка на конфликти
        List<Event> allRelevantEvents = new ArrayList<>(this.events);
        for (List<Event> external : externalCalendars) {
            allRelevantEvents.addAll(external);
        }

        // Търсим в рамките на една година напред
        while (daysSearched < 365) {
            if (isDayWorkable(currentDate)) {
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

    /**
     * Attempts to find a continuous time block within standard working hours (08:00-17:00).
     * @param date Date to search in.
     * @param duration Duration in hours.
     * @param eventsToCheck List of events to avoid overlap with.
     * @return Starting LocalTime if found, else null.
     */
    private LocalTime findFreeHourInDay(LocalDate date, int duration, List<Event> eventsToCheck) {
        LocalTime startLimit = LocalTime.of(8, 0);
        LocalTime endLimit = LocalTime.of(17, 0);

        // Обхождаме всеки възможен начален час от 08:00 нататък
        for (int h = startLimit.getHour(); h <= endLimit.getHour() - duration; h++) {
            LocalTime potentialStart = LocalTime.of(h, 0);
            LocalTime potentialEnd = potentialStart.plusHours(duration);

            if (isSlotFreeInList(eventsToCheck, date, potentialStart, potentialEnd)) {
                return potentialStart;
            }
        }
        return null;
    }

    /**
     * Logic for detecting overlaps between a time slot and a list of events.
     * @param eventList List of existing events.
     * @param date Date of the new slot.
     * @param start Start time of the new slot.
     * @param end End time of the new slot.
     * @return true if the slot does not overlap with any event in the list.
     */
    private boolean isSlotFreeInList(List<Event> eventList, LocalDate date, LocalTime start, LocalTime end) {
        for (Event e : eventList) {
            if (e.getDate().equals(date)) {
                // Математическа проверка за застъпване на два интервала
                if (!(end.isBefore(e.getStartTime()) || start.isAfter(e.getEndTime()) ||
                        end.equals(e.getStartTime()) || start.equals(e.getEndTime()))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Merges events from external files into the current calendar with conflict resolution.
     * @param filePaths List of paths to external calendar files.
     * @param scanner Scanner for user input during conflict resolution.
     */
    public void mergeWithCalendars(List<String> filePaths, Scanner scanner) {
        for (String path : filePaths) {
            System.out.println("Merging with: " + path);
            List<Event> externalEvents = loadEventsFromFile(path);

            for (Event other : externalEvents) {
                // Пропускаме, ако събитието е идентично с вече съществуващо
                if (isEventAlreadyPresent(other)) {
                    continue;
                }

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

    /**
     * Checks if an identical event exists in the calendar using object equality.
     * @param other The event to check.
     * @return true if an identical event is already present.
     */
    private boolean isEventAlreadyPresent(Event other) {
        // Използваме equals() метода на класа Event
        return events.contains(other);
    }

    /**
     * Handles a scheduling conflict by providing multiple resolution options to the user.
     * @param existing The event already in the calendar.
     * @param other The new event trying to be added.
     * @param scanner Input source for user choice.
     */
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
            case "1": break;
            case "2":
                events.remove(existing);
                events.add(other);
                break;
            case "3":
                moveEventWithRetry(other, scanner);
                break;
            case "4":
                if (moveEventWithRetry(existing, scanner)) {
                    events.remove(existing);
                    events.add(other);
                }
                break;
            case "5":
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

    /**
     * Interactive loop to re-schedule an event to a free slot until success or cancellation.
     * @param event The event to be moved.
     * @param scanner Input source for new times.
     * @return true if the event was successfully moved.
     */
    private boolean moveEventWithRetry(Event event, Scanner scanner) {
        while (true) {
            System.out.println("Enter new start and end time for [" + event.getName() + "] (HH:mm HH:mm) or 'cancel':");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("cancel")) return false;

            try {
                String[] times = input.split("\\s+");
                LocalTime newS = LocalTime.parse(times[0]);
                LocalTime newE = LocalTime.parse(times[1]);

                if (newS.isAfter(newE) || newS.equals(newE)) {
                    System.out.println("Error: Start time must be before end time.");
                    continue;
                }

                if (isSlotFree(event.getDate(), newS, newE)) {
                    events.add(new Event(event.getDate(), newS, newE, event.getName(), event.getNote()));
                    return true;
                } else {
                    System.out.println("Error: That slot is already occupied.");
                }
            } catch (Exception e) {
                System.out.println("Error: Invalid time format. Try again.");
            }
        }
    }

    /**
     * Finds which existing event overlaps with a new proposed event.
     * @param other The new event.
     * @return The first conflicting Event object found, or null.
     */
    private Event findConflictingEvent(Event other) {
        for (Event e : events) {
            if (e.getDate().equals(other.getDate())) {
                // Проверка за застъпване на времето
                if (other.getStartTime().isBefore(e.getEndTime()) && other.getEndTime().isAfter(e.getStartTime())) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * Searches for events containing a specific keyword in their name or note.
     * @param search The search query string.
     */
    public void findEvents(String search) {
        String query = search.toLowerCase();
        boolean found = false;
        System.out.println("Search results for '" + search + "':");

        for (Event event : events) {
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

    /**
     * Marks a date as a holiday.
     * @param date Date to mark.
     */
    public void addHoliday(LocalDate date) {
        if (!holidays.add(date)) {
            System.out.println("Error: " + date + " is already marked as a holiday.");
        } else {
            System.out.println("Date " + date + " is now marked as a holiday.");
        }
    }

    /**
     * Checks if a date is present in the holidays set.
     * @param date Date to check.
     * @return true if it's a holiday.
     */
    public boolean isHoliday(LocalDate date) {
        return holidays.contains(date);
    }

    /**
     * Displays a report of busy days within a range, sorted by total duration.
     * @param from Start date of the range.
     * @param to End date of the range.
     */
    public void showBusyDays(LocalDate from, LocalDate to) {
        Map<LocalDate, Long> busyHoursMap = new HashMap<>();

        for (Event e : events) {
            if ((e.getDate().isEqual(from) || e.getDate().isAfter(from)) &&
                    (e.getDate().isEqual(to) || e.getDate().isBefore(to))) {

                long minutes = Duration.between(e.getStartTime(), e.getEndTime()).toMinutes();
                busyHoursMap.put(e.getDate(), busyHoursMap.getOrDefault(e.getDate(), 0L) + minutes);
            }
        }

        if (busyHoursMap.isEmpty()) {
            System.out.println("No events found in this period.");
        } else {
            // Сортираме резултатите по натовареност (от най-зает към най-малко зает ден)
            busyHoursMap.entrySet().stream()
                    .sorted(Map.Entry.<LocalDate, Long>comparingByValue().reversed())
                    .forEach(entry -> System.out.println(entry.getKey() + ": " + (entry.getValue() / 60) + "h " + (entry.getValue() % 60) + "m"));
        }
    }

    /**
     * Processes a single data line from a file and populates internal collections.
     * @param line Raw string from file.
     */
    public void loadLine(String line) {
        String[] parts = line.split(",");
        if (parts[0].equals("E")) {
            Event e = EventParser.parseEvent(parts);
            if (e != null) events.add(e);
        } else if (parts[0].equals("H")) {
            LocalDate h = EventParser.parseHoliday(parts);
            if (h != null) holidays.add(h);
        }
    }

    /**
     * Reads all events from a specific file without merging them into the current state.
     * Useful for findSlotWith or preliminary merge checks.
     * @param filePath Path to the file.
     * @return List of Event objects extracted from the file.
     */
    public List<Event> loadEventsFromFile(String filePath) {
        List<Event> external = new ArrayList<>();
        // Ползваме UTF-8 за съвместимост с кирилица
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals("E")) {
                    Event e = EventParser.parseEvent(parts);
                    if (e != null) external.add(e);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
        }
        return external;
    }

    /**
     * Resets the calendar by clearing all events and holidays.
     */
    public void clearEvents() {
        events.clear();
        holidays.clear();
    }
}