package bg.tu_varna.sit.f24621666;

import bg.tu_varna.sit.f24621666.core.CommandProcessor;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CommandProcessor processor = new CommandProcessor();

        System.out.println("Welcome!");
        boolean running = true;

        while (running) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break; // Защита от неочакван край на входа
            String input = scanner.nextLine();
            running = processor.processCommand(input);
        }
        scanner.close();
    }
}