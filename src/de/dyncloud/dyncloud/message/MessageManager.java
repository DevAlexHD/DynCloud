package de.dyncloud.dyncloud.message;

import de.dyncloud.dyncloud.DynCloud;
import net.md_5.bungee.api.CommandSender;

/**
 * Created by Alexander on 13.04.2017.
 */
public class MessageManager {

    public static void sendMessage(CommandSender commandSender, String string) {
        commandSender.sendMessage(DynCloud.getInstance().prefix + string.replace("&", "§"));
    }

    public static void noPermission(CommandSender commandSender) {
        MessageManager.sendMessage(commandSender, "&cSchade, aber trotzdem danke für das Eingeben des Commands!");
    }

    public static void onlyForPlayers(CommandSender commandSender) {
        MessageManager.sendMessage(commandSender, "&cNene, du bist kein Spieler!");
    }
}
