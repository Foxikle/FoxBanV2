package me.foxikle.foxbanv2;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Unban implements CommandExecutor, TabExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("unban")) {
            if (sender instanceof Player) {
                Player staff = (Player) sender;
                if (staff.hasPermission("foxban.unban.use")) {
                    if (args.length >=1) {
                        Bukkit.getServer().getOfflinePlayer(args[0]);
                        OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(args[0]);
                        if (player.isBanned()) {
                            Bukkit.getBanList(BanList.Type.NAME).pardon(player.getName());
                            Bukkit.getBanList(BanList.Type.IP).pardon(player.getName());
                            staff.sendMessage(ChatColor.RED + player.getName() + " was unbanned.");
                            addAuditLogEntry(staff, player);
                        } else{
                            staff.sendMessage(ChatColor.RED + "This player is not banned.");
                        }
                    } else {
                        staff.sendMessage(ChatColor.RED + "Incorrect usage: /unban <PLAYER>");
                    }
                } else {
                    staff.sendMessage(ChatColor.RED + "You do not have the proper permissions to use this command.");
                    staff.sendMessage(ChatColor.RED + "Please contact a server administrator is you think this is a mistake.");
                }
                return true;
            }
            sender.sendMessage("Hey, You cannot do this! Use /pardon <player> if you really want to do this.");
        } else {
            return onCommand(sender, cmd, label, args);
        }
        return true;
    }

    private void addAuditLogEntry(Player s, OfflinePlayer p){


            File file = new File("plugins/FoxBanV2/AuditLog.yml");
            YamlConfiguration ymlConfig = YamlConfiguration.loadConfiguration(file);
            Map<String, String> map = new HashMap<>();
            map.put("Staff", s.getName());
            map.put("Player", p.getName());
            map.put("Time", Instant.now().toString());

            ymlConfig.createSection("UNBAN: " + p.getUniqueId(), map);

            try {
                ymlConfig.save(file);
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Failed to save audit log entry: UNBAN ", e);
            }
        }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){
            List<String> playerNames = new ArrayList<>();
            OfflinePlayer[] players = new OfflinePlayer[Bukkit.getServer().getBannedPlayers().size()];
            Bukkit.getServer().getBannedPlayers().toArray(players);
            for (OfflinePlayer player : players) {
                playerNames.add(player.getName());
            }
            return playerNames;
        }
        return null;
    }
}