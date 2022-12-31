package me.foxikle.foxbanv2;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public Main instance;

    @Override
    public void onEnable() {
        instance = this;
        this.saveResource("Config.yml", false);
        this.saveResource("Auditlog.yml", false);
        this.getCommand("ban").setExecutor(new BanCommand());
        this.getCommand("unban").setExecutor(new Unban());
    }
}