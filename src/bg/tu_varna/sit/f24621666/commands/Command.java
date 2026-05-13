package bg.tu_varna.sit.f24621666.commands;

public interface Command {
    void execute(String[] args); // Всеки клас ще описва тук какво прави
    String getName();            // Име на командата (напр. "open")
    String getHelp();            // Описание за командата help
}
