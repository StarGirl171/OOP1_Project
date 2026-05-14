package bg.tu_varna.sit.f24621666;

import bg.tu_varna.sit.f24621666.core.CommandProcessor;
import java.util.Scanner;

/**
 * Entry point of the Personal Calendar Application.
 * Initializes the command processor and handles the main application loop.
 */
public class Main {

    /**
     * Main method that runs the interactive console loop.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Използваме Scanner за четене на входа от потребителя
        Scanner scanner = new Scanner(System.in);
        // Централният процесор, който държи всички мениджъри и команди
        CommandProcessor processor = new CommandProcessor();

        System.out.println("Personal Calendar System Loaded.");
        System.out.println("Type 'help' to see available commands.");

        /** Flag to control the lifecycle of the application loop. */
        boolean running = true;

        // Основен цикъл на програмата
        while (running) {
            System.out.print("> ");

            // Защита при затваряне на входния поток
            if (!scanner.hasNextLine()) break;

            String input = scanner.nextLine();

            // Процесорът обработва командата и ни казва дали да продължим (false при 'exit')
            running = processor.processCommand(input);
        }

        // Чисто затваряне на ресурсите
        scanner.close();
        System.out.println("Application terminated.");
    }
}