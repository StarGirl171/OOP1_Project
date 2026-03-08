package bg.tu_varna.sit.f24621666;

public class CommandProcessor {

    private final FileManager fileManager = new FileManager();
    public boolean processCommand(String input) {

        String[] parts = input.split(" ");
        String command = parts[0];

        switch (command) {

            case "open":
                if (parts.length < 2) {
                    System.out.println("Usage: open <file>");
                    break;
                }
                fileManager.open(parts[1]);
                break;

            case "close":
                fileManager.close();
                break;

            case "save":
                fileManager.save();
                break;

            case "saveAs":
                if (parts.length < 2) {
                    System.out.println("Usage: saveAs <file>");
                    break;
                }
                fileManager.saveAs(parts[1]);
                break;

            case "help":
                printHelp();
                break;

            case "exit":
                System.out.println("Exiting the program...");
                return false;

            default:
                System.out.println("Unknown command");
        }
        return true;
    }

    private void printHelp() {
        System.out.println("The following commands are supported:");

        System.out.println("open <file>      opens <file>");
        System.out.println("close            closes currently opened file");
        System.out.println("save             saves the currently open file");
        System.out.println("saveAs <file>    saves the currently open file in <file>");
        System.out.println("help             prints this information");
        System.out.println("exit             exits the program");
    }
}
