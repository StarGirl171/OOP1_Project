package bg.tu_varna.sit.f24621666;

public class SaveAsCommand implements Command {
    private final FileManager fileManager;

    public SaveAsCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void execute(String[] args) {
        if (!fileManager.isOpen()) {
            System.out.println("Error: Please open a file first.");
            return;
        }

        if (args.length < 1) {
            System.out.println("Usage: saveas <file path>");
            return;
        }
        fileManager.saveAs(args[0]);
    }

    @Override
    public String getName() { return "saveas"; }

    @Override
    public String getHelp() { return "saveas <file> - saves the currently open file in <file>"; }
}