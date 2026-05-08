package bg.tu_varna.sit.f24621666;

import java.util.Collection;

public class HelpCommand implements Command {
    private final Collection<Command> commands;

    public HelpCommand(Collection<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(String[] args) {
        System.out.println("The following commands are supported:");
        for (Command cmd : commands) {
            System.out.println(cmd.getHelp());
        }
        System.out.println("exit - exists the program");
    }

    @Override
    public String getName() { return "help"; }

    @Override
    public String getHelp() { return "help - prints this information"; }
}