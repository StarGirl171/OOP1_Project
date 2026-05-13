package bg.tu_varna.sit.f24621666;

public class OpenCommand extends AbstractCommand {

    public OpenCommand(FileManager fm, CalendarManager cm) { super(fm, cm); }

    @Override
    protected boolean requiresOpenFile() { return false; }

    @Override
    protected int getMinArgs() { return 1; }

    @Override
    protected String getUsage() { return "Usage: open <file>"; }

    @Override
    protected void executeLogic(String[] args) { fileManager.open(args[0]); }

    @Override
    public String getName() { return "open"; }

    @Override
    public String getHelp() { return "open <file> - opens a calendar file"; }
}