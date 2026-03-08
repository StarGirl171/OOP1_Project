package bg.tu_varna.sit.f24621666;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CommandProcessor processor = new CommandProcessor();

        System.out.println("Welcome!");
        System.out.println("Type 'help' to see commands.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            boolean continueProgram = processor.processCommand(input);

            if (!continueProgram) {
                break;
            }
        }
        scanner.close();
    }
}