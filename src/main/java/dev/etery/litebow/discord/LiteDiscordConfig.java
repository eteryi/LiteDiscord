package dev.etery.litebow.discord;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LiteDiscordConfig {
    private final JavaPlugin plugin;
    private final FileConfiguration configuration;

    public LiteDiscordConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        configuration = this.plugin.getConfig();
    }

    public void init() {
        configuration.options().copyDefaults(true);
        configuration.addDefault("token", "discord-bot-token");
        plugin.saveConfig();
    }

    public String getToken() {
        return configuration.getString("token");
    }
}
