package bg.tu_varna.sit.f24621666;

public class SaveCommand extends AbstractCommand {
    public SaveCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override
    protected int getMinArgs() { return 0; }

    @Override
    protected String getUsage() { return "Usage: save"; }

    @Override
    protected void executeLogic(String[] args) { fileManager.save(); }

    @Override
    public String getName() { return "save"; }

    @Override
    public String getHelp() { return "save - saves changes to the current file"; }
}