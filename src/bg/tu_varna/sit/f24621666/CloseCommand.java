package bg.tu_varna.sit.f24621666;

public class CloseCommand extends AbstractCommand {
    public CloseCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override
    protected int getMinArgs() { return 0; }

    @Override
    protected String getUsage() { return "Usage: close"; }

    @Override
    protected void executeLogic(String[] args) { fileManager.close(); }

    @Override
    public String getName() { return "close"; }

    @Override
    public String getHelp() { return "close - closes the current file"; }
}