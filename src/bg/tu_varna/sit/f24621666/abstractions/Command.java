package bg.tu_varna.sit.f24621666.abstractions;

/**
 * Common interface for all executable commands in the application.
 * Part of the Command Pattern implementation to decouple command triggering from execution.
 */
public interface Command {
    /**
     * Executes the specific logic of the command with provided arguments.
     * @param args Array of strings representing command parameters.
     */
    void execute(String[] args);

    /**
     * Returns the unique name of the command used to trigger it.
     * @return String command name.
     */
    String getName();

    /**
     * Returns a brief description and usage instruction for the command.
     * @return String help information.
     */
    String getHelp();
}