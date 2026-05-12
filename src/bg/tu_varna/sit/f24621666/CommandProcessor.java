package bg.tu_varna.sit.f24621666;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CommandProcessor {
    private final TreeMap<String, Command> commands = new TreeMap<>();
    private final CalendarManager calendarManager = new CalendarManager();
    private final FileManager fileManager = new FileManager(calendarManager);

    public CommandProcessor() {
        registerCommand(new OpenCommand(fileManager));
        registerCommand(new CloseCommand(fileManager));
        registerCommand(new SaveCommand(fileManager));
        registerCommand(new SaveAsCommand(fileManager));
        registerCommand(new BookCommand(fileManager, calendarManager));
        registerCommand(new AgendaCommand(fileManager, calendarManager));
        registerCommand(new UnbookCommand(fileManager, calendarManager));
        registerCommand(new ChangeCommand(fileManager, calendarManager));
        registerCommand(new FindCommand(fileManager, calendarManager));
        registerCommand(new HolidayCommand(fileManager, calendarManager));
        registerCommand(new BusyDaysCommand(fileManager, calendarManager));


        // HelpCommand има нужда от списъка с всички команди, за да ги показва
        registerCommand(new HelpCommand(commands.values()));
    }

    private void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public boolean processCommand(String input) {
        // Този израз разделя по интервали, но запазва текста в кавичките като един елемент
        String[] parts = input.trim().split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        // След това махаме кавичките от самите аргументи, за да не се пазят в базата
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].replace("\"", "");
        }        String commandName = parts[0];

        if (commandName.equals("exit")) {
            System.out.println("Exiting...");
            return false;
        }

        Command command = commands.get(commandName);
        if (command != null) {
            // Махаме първата част (името на командата) и пращаме само аргументите
            String[] args = new String[parts.length - 1];
            System.arraycopy(parts, 1, args, 0, parts.length - 1);
            command.execute(args);
        } else {
            System.out.println("Unknown command. Type 'help' for info.");
        }
        return true;
    }
}