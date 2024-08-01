package dev.etery.litebow.discord;
import dev.etery.litebow.discord.discord.command.DCStatsCommand;
import dev.etery.litebow.discord.discord.command.TestCommand;
import dev.etery.litecosmetics.LiteCosmetics;
import dev.etery.litebow.discord.discord.DiscordBot;
import me.stephenminer.litecoin.LiteCoin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class LiteDiscord extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        LiteCoin liteCoin = JavaPlugin.getPlugin(LiteCoin.class);
        LiteCosmetics cosmetics = LiteCosmetics.get();
        LiteDiscordConfig config = new LiteDiscordConfig(this);
        config.init();

        DiscordBot bot = new DiscordBot(config.getToken());
        try {
            bot.load(Activity.ActivityType.COMPETING, "on GAMBLING");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        bot.addCommand(
                Commands.slash("stats", "Check players stats")
                .addOption(OptionType.STRING, "username", "Player's IGN", true),
                new DCStatsCommand(liteCoin, cosmetics)
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
