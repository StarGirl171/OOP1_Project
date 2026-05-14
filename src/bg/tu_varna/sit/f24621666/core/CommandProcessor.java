package bg.tu_varna.sit.f24621666.core;

import bg.tu_varna.sit.f24621666.commands.*;
import java.util.TreeMap;

/**
 * Orchestrator class that registers available commands and routes user input to them.
 * Handles the mapping between string input and Command objects.
 */
public class CommandProcessor {

    /** Map containing all registered commands, sorted alphabetically by name. */
    private final TreeMap<String, Command> commands = new TreeMap<>();

    /** Central instance of CalendarManager shared across all commands. */
    private final CalendarManager calendarManager = new CalendarManager();

    /** Central instance of FileManager shared across all commands. */
    private final FileManager fileManager = new FileManager(calendarManager);

    /**
     * Initializes the processor and registers all supported commands.
     */
    public CommandProcessor() {
        // Регистрация на всяка една команда в системата
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

        // HelpCommand приема списъка от вече регистрирани команди, за да ги покаже
        HelpCommand help = new HelpCommand(fileManager, calendarManager, commands.values());
        registerCommand(help);
    }

    /**
     * Adds a command to the internal registry.
     * @param command The command instance to register.
     */
    private void registerCommand(Command command) {
        // Използваме името на командата като ключ в речника
        commands.put(command.getName(), command);
    }

    /**
     * Parses raw input string and executes the corresponding command.
     * @param input Raw user input from console.
     * @return true if the application should continue running, false for exit.
     */
    public boolean processCommand(String input) {
        // Проверка за празен вход (ако потребителят само натисне Enter)
        if (input == null || input.trim().isEmpty()) return true;

        // Регулярен израз, който разделя по интервали, но запазва текста в кавички като един елемент
        String[] parts = input.trim().split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        // Премахваме кавичките от аргументите за чиста обработка
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].replace("\"", "");
        }

        // Правим името на командата малки букви за Case-Insensitivity
        String commandName = parts[0].toLowerCase();

        // Специална проверка за командата exit за прекратяване на цикъла
        if (commandName.equals("exit")) {
            Command exitCmd = commands.get("exit");
            if (exitCmd != null) exitCmd.execute(new String[0]);
            return false;
        }

        // Търсим командата в нашия регистър
        Command command = commands.get(commandName);
        if (command != null) {
            // Отделяме само аргументите (всичко след името на командата)
            String[] args = new String[parts.length - 1];
            System.arraycopy(parts, 1, args, 0, parts.length - 1);
            command.execute(args);
        } else {
            System.out.println("Unknown command. Type 'help' for info.");
        }
        return true;
    }
}