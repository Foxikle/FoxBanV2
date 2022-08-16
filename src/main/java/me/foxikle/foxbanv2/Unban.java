package me.foxikle.foxbanv2;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static me.foxikle.foxrank.FoxRank.getRank;

public class Unban implements CommandExecutor, TabExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("fban")) {
            if (sender instanceof Player) {
                Player unBaner = (Player) sender;
                if (getRank(unBaner).getPowerLevel() >= 90) {
                    if (args.length >=1) {
                        Bukkit.getServer().getOfflinePlayer(args[0]);
                        OfflinePlayer unbanee = Bukkit.getServer().getOfflinePlayer(args[0]);
                        if (unbanee.isBanned()) {
                            Bukkit.getBanList(BanList.Type.NAME).pardon(unbanee.getName());
                            Bukkit.getBanList(BanList.Type.IP).pardon(unbanee.getName());
                        } else{
                            unBaner.sendMessage(ChatColor.RED + "This player is not banned.");
                        }
                    } else {
                        unBaner.sendMessage(ChatColor.RED + "Incorrect usage: /unban <PLAYER>");
                    }
                } else {
                    unBaner.sendMessage(ChatColor.RED + "You must have ADMIN rank for higher to use this command.");
                    unBaner.sendMessage(ChatColor.RED + "Please contact a server administrator is you think this is a mistake.");
                }
                return true;
            }
            sender.sendMessage("Hey, You cannot do this!");
        } else {
            return onCommand(sender, cmd, label, args);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){
            List<String> playerNames = new ArrayList<>();
            OfflinePlayer[] players = new Player[Bukkit.getServer().getBannedPlayers().size()];
            Bukkit.getServer().getBannedPlayers().toArray(players);
            for (int i = 0; i < players.length; i++){
                playerNames.add(players[i].getName());
            }
            return playerNames;
        }
        return null;
    }
}
