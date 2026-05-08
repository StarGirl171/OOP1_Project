package bg.tu_varna.sit.f24621666;

public class OpenCommand implements Command {
    private final FileManager fileManager;

    public OpenCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: open <file>");
            return;
        }
        fileManager.open(args[0]);
    }

    @Override
    public String getName() { return "open"; }

    @Override
    public String getHelp() { return "open <file> - opens a file"; }
}