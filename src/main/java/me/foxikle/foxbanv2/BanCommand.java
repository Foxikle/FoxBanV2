package me.foxikle.foxbanv2;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.logging.Level;

import static java.util.Objects.hash;
import static me.foxikle.foxrank.FoxRank.getRank;


public class BanCommand implements CommandExecutor, TabExecutor {

    private String reason = "No reason specified.";
    private String bumper = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
    private String rawDuration = "1";
    private boolean silent = false;
    private File file = new File("plugins/FoxBanV2/Config.yml");
    private YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("fban")) {
            if (sender instanceof Player) {
                Player banner = (Player) sender;
                if (getRank(banner).getPowerLevel() >= 80) {
                    if (args.length < 4) {
                        if (Bukkit.getServer().getPlayer(args[0]) != null) {
                            Player banee = Bukkit.getServer().getPlayer(args[0]);
                            if(args[1].equalsIgnoreCase("SECURITY")){
                                reason = yml.getConfigurationSection("BanReasons".toString()).getString(args[1]);
                            } else if (args[1].equalsIgnoreCase("HACKING")) {
                                reason = yml.getConfigurationSection("BanReasons".toString()).getString(args[1]);
                            } else if (args[1].equalsIgnoreCase("DUPING")) {
                                reason = yml.getConfigurationSection("BanReasons".toString()).getString(args[1]);
                            } else if (args[1].equalsIgnoreCase("BUG ABUSE")) {
                                reason = yml.getConfigurationSection("BanReasons".toString()).getString(args[1]);
                            } else if (args[1].equalsIgnoreCase("INAPPROPRIATE COSMETICS")) {
                                reason = yml.getConfigurationSection("BanReasons".toString()).getString(args[1]);
                            } else if (args[1].equalsIgnoreCase("INAPPROPRIATE BUILD")) {
                                reason = yml.getConfigurationSection("BanReasons".toString()).getString(args[1]);
                            } else if (args[1].equalsIgnoreCase("BOOSTING")) {
                                reason = yml.getConfigurationSection("BanReasons".toString()).getString(args[1]);
                            } else {
                                reason = args[1];
                            }
                                if(args[2].equalsIgnoreCase("1d")){
                                    rawDuration = "1";
                                } else if(args[2].equalsIgnoreCase("7d")){
                                    rawDuration = "7";
                                } else if(args[2].equalsIgnoreCase("30d")){
                                    rawDuration = "30";
                            }    else if(args[2].equalsIgnoreCase("60d")){
                                    rawDuration = "60";
                                } else if(args[2].equalsIgnoreCase("90d")){
                                    rawDuration = "90";
                                } else if(args[2].equalsIgnoreCase("180d")){
                                    rawDuration = "180";
                                } else if(args[2].equalsIgnoreCase("270d")){
                                    rawDuration = "270";
                                } else if(args[2].equalsIgnoreCase("360d")){
                                    rawDuration = "360";
                                } else if(args[2].equalsIgnoreCase("PERMANANT")){
                                    rawDuration = "PERMANANT";
                                } else {
                                    banner.sendMessage(ChatColor.RED + "Invalid duration, use tab completion for a list of valid durations.");
                                }
                                if(args[3].equalsIgnoreCase("SILENT")){
                                    silent = true;
                                } else if(args[3].equalsIgnoreCase("PUBLIC")){
                                    silent = false;
                                } else{
                                    banner.sendMessage(ChatColor.RED + "Invalid Argument: <SILENT/PUBLIC>");
                                }

                                banPlayer(banner, banee, silent, reason, rawDuration, args[1], yml.getString("ServerName"), yml.getString("AppealLink"));


                        } else {
                            banner.sendMessage(ChatColor.RED + "Could not find that player");
                        }
                    } else {
                        banner.sendMessage(ChatColor.RED + "Incorrect usage: /ban <PLAYER> <REASON> <DURATION> <SILENT/PUBLIC>");
                    }
                } else {
                    banner.sendMessage(ChatColor.RED + "You must have MODERATOR rank for higher to use this command.");
                    banner.sendMessage(ChatColor.RED + "Please contact a server administrator is you think this is a mistake.");
                }
                return true;
            }
            sender.sendMessage("Hey, You cannot do this!");
        } else {
            return onCommand(sender, cmd, label, args);
        }
        return true;
    }

    private void banPlayer(Player banner, Player banee, boolean silent, String reasonStr, String duration, String broadcastReason, String serverName, String appealLink){
        Date expires = null;
        if(duration.equalsIgnoreCase("PERMANANT")){
            expires = null;
        } else {
            int durInt = Integer.parseInt(duration);
            expires = Date.from(new Date().toInstant().plusSeconds(durInt * 24 * 60 * 60));
        }
        if(!duration.equalsIgnoreCase("PERMANANT")) {
            Bukkit.getBanList(BanList.Type.NAME).addBan(banee.getName(), bumper + ChatColor.RED + "You are banned from " + serverName + ". \n" + ChatColor.AQUA + "Reason: " + ChatColor.RESET + reasonStr + ChatColor.AQUA + "\n Your ban will last for " + duration + "days." + bumper, expires, banner.getName());
            banee.kickPlayer(bumper + ChatColor.RED + "You are banned from " + serverName + ". \n" + ChatColor.AQUA + "Reason: " + ChatColor.RESET + reasonStr + ChatColor.AQUA + "\n Your ban will last for " + duration + "days." + bumper);
            addAuditLogEntry(banner, banee, reason, duration, getBanID(banee));
        } else {
            Bukkit.getBanList(BanList.Type.NAME).addBan(banee.getName(), bumper + ChatColor.RED + "You are permanantly banned from " + serverName + ". \n" + ChatColor.AQUA + "Reason: " + ChatColor.RESET + reasonStr + ChatColor.AQUA + "\n Your ban will last indefinetly." + bumper, expires, banner.getName());
            banee.kickPlayer(bumper + ChatColor.RED + "You are permanantly banned from " + serverName + ". \n" + ChatColor.AQUA + "Reason: " + ChatColor.RESET + reasonStr + ChatColor.AQUA + "\n Your ban will last indefinetly." + bumper);
            addAuditLogEntry(banner, banee, reason, duration, getBanID(banee));
        }

        if(!broadcastReason.equalsIgnoreCase("SECURITY")) {
            if (silent) {
                banner.getWorld().playSound(banner.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1000f, 1);
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "A player was removed from your game for " + broadcastReason);
                banner.sendMessage(ChatColor.RED + "" +  ChatColor.BOLD +  banee.getName() + " was banned silently for " + reasonStr + ".");
            } else {
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + banee.getName() + " was removed from your game for " + broadcastReason);
                banner.getWorld().playSound(banner.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1000f, 1);
                banner.sendMessage(ChatColor.RED + "" +  ChatColor.BOLD +  banee.getName() + " was banned for " + reasonStr + ".");
            }
        } else{
            banner.sendMessage(ChatColor.RED + "" +  ChatColor.BOLD +  banee.getName() + " was banned for SECURITY.");
        }
    }

    private void addAuditLogEntry(Player banner, Player bannee, String reason, String duration, String banID){
        File file = new File("plugins/FoxBanV2/AuditLog.yml");
        YamlConfiguration ymlConfig = YamlConfiguration.loadConfiguration(file);
        String type = "NAME";
        Map<Integer, String> map = new HashMap<>();
        map.put(1, banner.getName());
        map.put(2, bannee.getUniqueId().toString());
        map.put(3, reason);
        map.put(4, duration);
        map.put(5, type);
        map.put(6, banID);
        ymlConfig.createSection("", map);

        try {
            ymlConfig.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to save audit log entry " + banID, e);
        }
    }

    private String getBanID(Player banee){
        String banId = Integer.toString(hash("FoxBan:" + banee.getName() + ":" + Instant.now()), 16).toUpperCase(Locale.ROOT);
        return  banId;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){
            List<String> playerNames = new ArrayList<>();
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (int i = 0; i < players.length; i++){
                playerNames.add(players[i].getName());
            }

            return playerNames;
        } else if (args.length == 2){
            List<String> arguments = new ArrayList<>();
            arguments.add("SECURITY");
            arguments.add("HACKING");
            arguments.add("DUPING");
            arguments.add("BUG ABUSE");
            arguments.add("INAPPROPRIATE COSMETICS");
            arguments.add("INAPPROPRIATE BUILD");
            arguments.add("BOOSTING");

            return arguments;
        } else if (args.length == 3){
            List<String> arguments = new ArrayList<>();
            arguments.add("1d");
            arguments.add("7d");
            arguments.add("30d");
            arguments.add("60d");
            arguments.add("90d");
            arguments.add("180d");
            arguments.add("270d");
            arguments.add("360d");
            arguments.add("PERMANANT");

            return arguments;
        }
        return null;
    }
}
