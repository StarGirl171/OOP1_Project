package bg.tu_varna.sit.f24621666.core;

import bg.tu_varna.sit.f24621666.commands.*;

import java.util.TreeMap;

public class CommandProcessor {
    private final TreeMap<String, Command> commands = new TreeMap<>();
    private final CalendarManager calendarManager = new CalendarManager();
    private final FileManager fileManager = new FileManager(calendarManager);

    public CommandProcessor() {
        registerCommand(new OpenCommand(fileManager, calendarManager));
        registerCommand(new CloseCommand(fileManager, calendarManager));
        registerCommand(new SaveCommand(fileManager, calendarManager));
        registerCommand(new SaveAsCommand(fileManager, calendarManager));
        registerCommand(new BookCommand(fileManager, calendarManager));
        registerCommand(new AgendaCommand(fileManager, calendarManager));
        registerCommand(new UnbookCommand(fileManager, calendarManager));
        registerCommand(new ChangeCommand(fileManager, calendarManager));
        registerCommand(new FindCommand(fileManager, calendarManager));
        registerCommand(new HolidayCommand(fileManager, calendarManager));
        registerCommand(new BusyDaysCommand(fileManager, calendarManager));
        registerCommand(new FindSlotWithCommand(fileManager, calendarManager));
        registerCommand(new FindSlotCommand(fileManager, calendarManager));
        registerCommand(new MergeCommand(fileManager, calendarManager));
        registerCommand(new ExitCommand(fileManager, calendarManager));

        // Подаваме колекцията от команди на HelpCommand
        HelpCommand help = new HelpCommand(fileManager, calendarManager, commands.values());
        registerCommand(help);
    }

    private void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public boolean processCommand(String input) {
        if (input == null || input.trim().isEmpty()) return true;

        String[] parts = input.trim().split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].replace("\"", "");

        String commandName = parts[0].toLowerCase();

        // Специална проверка за exit, за да излезем елегантно от цикъла в Main
        if (commandName.equals("exit")) {
            commands.get("exit").execute(new String[0]);
            return false;
        }

        Command command = commands.get(commandName);
        if (command != null) {
            String[] args = new String[parts.length - 1];
            System.arraycopy(parts, 1, args, 0, parts.length - 1);
            command.execute(args);
        } else {
            System.out.println("Unknown command. Type 'help' for info.");
        }
        return true;
    }
}