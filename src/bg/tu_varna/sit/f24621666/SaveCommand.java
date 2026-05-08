package bg.tu_varna.sit.f24621666;

public class SaveCommand implements Command {
    private final FileManager fileManager;

    public SaveCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void execute(String[] args) {
        if (!fileManager.isOpen()) {
            System.out.println("Error: Please open a file first.");
            return;
        }
        fileManager.save();
    }

    @Override
    public String getName() { return "save"; }

    @Override
    public String getHelp() { return "save - saves the currently open file"; }
}