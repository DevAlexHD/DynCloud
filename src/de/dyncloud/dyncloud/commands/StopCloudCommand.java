package de.dyncloud.dyncloud.commands;

import de.dyncloud.dyncloud.DynCloud;
        import net.md_5.bungee.api.CommandSender;
        import net.md_5.bungee.api.plugin.Command;
        import net.md_5.bungee.command.ConsoleCommandSender;

/**
 * Created by Alexander on 14.04.2017.
 */
public class StopCloudCommand extends Command {

    public StopCloudCommand(String name) {
        super("stopcloud");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(commandSender instanceof ConsoleCommandSender) {
            DynCloud.cloudShutdown = true;
            DynCloud.getServerManager().stopAllServers();
            commandSender.sendMessage(DynCloud.prefix + "Stopped all servers !");
        }
    }
}
