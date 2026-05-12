package bg.tu_varna.sit.f24621666;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class CalendarManager {
    private final List<Event> events = new ArrayList<>();
    private final Set<LocalDate> holidays = new HashSet<>();

    public void addEvent(Event event) {
        if (isSlotFree(event.getDate(), event.getStartTime(), event.getEndTime())) {
            events.add(event);
            System.out.println("Event added successfully.");
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
        if (holidays.contains(date)) {
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

            if (isSlotFree(newDate, newStart, newEnd)) {
                events.add(new Event(newDate, newStart, newEnd, newName, newNote));
                System.out.println("Event updated successfully.");
            } else {
                // Ако мястото е заето, връщаме оригиналното събитие
                events.add(foundEvent);
            }
        } catch (Exception e) {
            System.out.println("Error: Could not update event. Check your input format.");
            events.add(foundEvent); // Връщаме оригиналното събитие при грешка
        }
    }

    private boolean isSlotFree(LocalDate date, LocalTime start, LocalTime end) {
        // Валидация на самия интервал
        if (start.isAfter(end) || start.equals(end)) {
            System.out.println("Error: Start time must be before end time.");
            return false;
        }

        for (Event e : events) {
            if (e.getDate().equals(date)) {
                // Проверка за застъпване:
                // (StartA < EndB) AND (EndA > StartB)
                if (start.isBefore(e.getEndTime()) && end.isAfter(e.getStartTime())) {
                    System.out.println("Error: The slot " + start + "-" + end + " on " + date + " is already occupied by: " + e.getName());
                    return false;
                }
            }
        }
        return true;
    }

    public void findSlotWith(LocalDate fromDate, int hours, List<List<Event>> allExternalEvents) {
        LocalDate current = fromDate;
        int daysChecked = 0;

        while (daysChecked < 30) {
            if (current.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    current.getDayOfWeek() != DayOfWeek.SUNDAY && !holidays.contains(current)) {

                for (int h = 8; h <= 17 - hours; h++) {
                    LocalTime start = LocalTime.of(h, 0);
                    LocalTime end = start.plusHours(hours);

                    // Проверка в текущия календар
                    boolean freeLocally = isSlotFree(current, start, end);

                    // Проверка във ВСИЧКИ външни календари
                    boolean freeExternally = true;

                    for (List<Event> externalList : allExternalEvents) {
                        if (!isSlotFree(current, start, end, externalList)) {
                            freeExternally = false;
                            break;
                        }
                    }

                    if (freeLocally && freeExternally) {
                        System.out.println("Suggested synchronized slot: " + current + " from " + start + " to " + end);
                        return;
                    }
                }
            }
            current = current.plusDays(1);
            daysChecked++;
        }
        System.out.println("No synchronized free slots found.");
    }

    // Проверява дали слотът е свободен в КОНКРЕТЕН списък (твоя или чужд)
    public boolean isSlotFree(LocalDate date, LocalTime start, LocalTime end, List<Event> listToSearch) {
        if (start.isAfter(end) || start.equals(end)) {
            return false;
        }

        for (Event e : listToSearch) {
            if (e.getDate().equals(date)) {
                // Стандартна проверка за застъпване
                if (start.isBefore(e.getEndTime()) && end.isAfter(e.getStartTime())) {
                    return false;
                }
            }
        }
        return true;
    }

    public void findSlot(LocalDate fromDate, int hours) {
        LocalDate current = fromDate;
        int daysChecked = 0;

        // Търсим в рамките на следващите 30 дни, за да не въртим безкраен цикъл
        while (daysChecked < 30) {
            // Проверяваме дали денят е работен
            if (current.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    current.getDayOfWeek() != DayOfWeek.SUNDAY &&
                    !holidays.contains(current)) {

                // Проверяваме всеки кръгъл час от 08:00 до (17 - hours)
                for (int h = 8; h <= 17 - hours; h++) {
                    LocalTime start = LocalTime.of(h, 0);
                    LocalTime end = start.plusHours(hours);

                    if (isSlotFree(current, start, end)) {
                        System.out.println("Suggested slot: " + current + " from " + start + " to " + end);
                        return; // Намерихме първия свободен и спираме
                    }
                }
            }
            current = current.plusDays(1);
            daysChecked++;
        }
        System.out.println("No free slots found in the next 30 days.");
    }

    public void mergeWithCalendars(List<String> filePaths) {
        Scanner scanner = new Scanner(System.in);

        for (String path : filePaths) {
            System.out.println("Merging with: " + path);
            List<Event> externalEvents = loadEventsFromFile(path);

            for (Event other : externalEvents) {
                // Търсим дали има конфликт с текущите ни събития
                Event conflict = findConflictingEvent(other);

                if (conflict == null) {
                    // Няма конфликт, добавяме го директно
                    events.add(other);
                } else {
                    // Има конфликт - потребителят избира
                    System.out.println("\nCONFLICT found for date " + other.getDate());
                    System.out.println("Existing: " + conflict);
                    System.out.println("New from file: " + other);
                    System.out.print("Choose: [1] Keep Existing, [2] Replace with New, [3] Move New to other time: ");

                    String choice = scanner.nextLine();
                    switch (choice) {
                        case "1":
                            // Нищо не правим, остава старото
                            break;
                        case "2":
                            events.remove(conflict);
                            events.add(other);
                            System.out.println("Event replaced.");
                            break;
                        case "3":
                            System.out.println("Enter new start and end time (HH:mm HH:mm):");
                            String[] times = scanner.nextLine().split(" ");
                            try {
                                LocalTime newS = LocalTime.parse(times[0]);
                                LocalTime newE = LocalTime.parse(times[1]);
                                if (isSlotFree(other.getDate(), newS, newE)) {
                                    events.add(new Event(other.getDate(), newS, newE, other.getName(), other.getNote()));
                                    System.out.println("Moved and added.");
                                }
                            } catch (Exception e) {
                                System.out.println("Invalid input. Skipping event.");
                            }
                            break;
                    }
                }
            }
        }
        System.out.println("Merge process completed.");
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

    public List<Event> getAllEvents() {
        return events;
    }

    public void addHoliday(LocalDate date) {
        if (holidays.contains(date)) {
            System.out.println("Error: " + date + " is already marked as a holiday.");
            return;
        }
        holidays.add(date);
        System.out.println("Date " + date + " is now marked as a holiday.");
    }

    // Помощен метод
    public boolean isHoliday(LocalDate date) {
        return holidays.contains(date);
    }

    public Set<LocalDate> getAllHolidays() {
        return holidays;
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
            return;
        }

        // Сортираме по брой заети минути (в низходящ ред)
        List<Map.Entry<LocalDate, Long>> sortedDays = busyHoursMap.entrySet().stream()
                .sorted(Map.Entry.<LocalDate, Long>comparingByValue().reversed())
                .collect(Collectors.toList());

        System.out.println("Busy days from " + from + " to " + to + " (sorted by load):");
        for (Map.Entry<LocalDate, Long> entry : sortedDays) {
            long hours = entry.getValue() / 60;
            long mins = entry.getValue() % 60;
            System.out.println(entry.getKey() + " (" + entry.getKey().getDayOfWeek() + "): " + hours + "h " + mins + "m occupied");
        }
    }

    public void loadEventFromLine(String line) {
        try {
            String[] p = line.split(",");
            String type = p[0]; // Първият елемент ни казва типа (E за Event, H за Holiday)

            if (type.equalsIgnoreCase("E") && p.length == 6) {
                events.add(new Event(
                        LocalDate.parse(p[1]), LocalTime.parse(p[2]),
                        LocalTime.parse(p[3]), p[4], p[5]
                ));
            } else if (type.equalsIgnoreCase("H") && p.length == 2) {
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
                    externalEvents.add(new Event(
                            LocalDate.parse(p[1]), LocalTime.parse(p[2]),
                            LocalTime.parse(p[3]), p[4], p[5]
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading external calendar: " + filePath);
        }
        return externalEvents;
    }

    public void clearEvents() {
        events.clear();
    }
}