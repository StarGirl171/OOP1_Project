package bg.tu_varna.sit.f24621666;

public class CloseCommand implements Command {
    private final FileManager fileManager;

    public CloseCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void execute(String[] args) {
        fileManager.close();
    }

    @Override
    public String getName() { return "close"; }

    @Override
    public String getHelp() { return "close - closes currently opened file"; }
}