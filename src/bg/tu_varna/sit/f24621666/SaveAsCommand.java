package bg.tu_varna.sit.f24621666;

public class SaveAsCommand extends AbstractCommand {

    public SaveAsCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override
    protected int getMinArgs() { return 1; }

    @Override
    protected String getUsage() { return "Usage: saveas <path>"; }

    @Override
    protected void executeLogic(String[] args) { fileManager.saveAs(args[0]); }

    @Override
    public String getName() { return "saveas"; }

    @Override
    public String getHelp() { return "saveas <path> - saves current data to a new file"; }
}