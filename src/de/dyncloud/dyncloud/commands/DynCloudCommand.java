package de.dyncloud.dyncloud.commands;

import de.dyncloud.dyncloud.DynCloud;
import de.dyncloud.dyncloud.message.MessageManager;
import de.dyncloud.dyncloud.server.ServerGroup;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.io.File;
import java.util.List;

/**
 * Created by Alexander on 13.04.2017.
 */
public class DynCloudCommand extends Command {

    public DynCloudCommand() {
        super("DynCloud","",new String[]{"cloud", "cloudsystem","klaudsystem","klaudsistähm"});
    }

    public void execute(CommandSender commandSender, String[] arrstring) {
        String string;
        if (arrstring.length < 1) {
            MessageManager.sendMessage(commandSender, "&bWillkommen bei DynCloud. Welchen Server möchten sie bestellen ?");
            return;
        }
        if (!commandSender.hasPermission("dyncloud.admin")) {
            MessageManager.noPermission(commandSender);
            return;
        }
        if (arrstring[0].equalsIgnoreCase("listgroups")) {
            List<ServerGroup> list = DynCloud.getInstance().getServerManager().getGroups();
            if (list.size() == 0) {
                MessageManager.sendMessage(commandSender, "&cÄhmmm.. Es gibt noch keine Gruppen :D");
                return;
            }
            MessageManager.sendMessage(commandSender, "&6Es gibt (" + list.size() + ") Gruppen:");
            for (ServerGroup serverGroup : list) {
                MessageManager.sendMessage(commandSender, "  &b" + serverGroup.getName() + " &e(&7RAM: &6" + serverGroup.getRam() + "G&e, &7Amount: &6" + serverGroup.getStartupAmount() + "&e)");
            }
            return;
        }
        if (arrstring.length < 2) {
            MessageManager.sendMessage(commandSender, "&cDas ist leider falsch. Deutsch sollte man können ^^");
            return;
        }
        if (arrstring[0].equalsIgnoreCase("removegroup")) {
            string = arrstring[1];
            try {
                DynCloud.getInstance().getServerManager().removeGroup(string);
            }
            catch (Exception var4_6) {
                MessageManager.sendMessage(commandSender, "&cÖhmmm... Es gab da nen kleinen Fehler beim Speichern. Da hat Alex wohl scheiße gebaut.");
                var4_6.printStackTrace();
            }
        }
        if (arrstring.length < 4) {
            MessageManager.sendMessage(commandSender, "&cDas ist leider falsch. Deutsch sollte man können ^^");
            return;
        }
        if (arrstring[0].equalsIgnoreCase("addgroup")) {
            string = arrstring[1];
            File file = new File(DynCloud.getInstance().getFileManager().getTemplatesDirectory() + string);
            if (!file.exists()) {
                MessageManager.sendMessage(commandSender, "&cDa hat wohl jemand vergessen &b" + file.toString() + " &czu erstellen! Schalamper!");
                return;
            }
            int n = Integer.parseInt(arrstring[2]);
            int n2 = Integer.parseInt(arrstring[3]);
            ServerGroup serverGroup = new ServerGroup(string, n, n2);
            if (DynCloud.getInstance().getServerManager().groupExists(serverGroup)) {
                MessageManager.sendMessage(commandSender, "&cDiese Gruppe gibt's leider schon.");
                return;
            }
            try {
                DynCloud.getInstance().getServerManager().addGroup(serverGroup);
            }
            catch (Exception var8_12) {
                MessageManager.sendMessage(commandSender, "&cÖhmmm... Es gab da nen kleinen Fehler beim Speichern. Da hat Alex wohl scheiße gebaut.");
                var8_12.printStackTrace();
            }
        }
    }
}

