package dev.etery.litebow.discord.discord.listener;

import dev.etery.litebow.discord.discord.DiscordBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BasicListener extends ListenerAdapter {
    private final DiscordBot bot;

    public BasicListener(DiscordBot bot) {
        this.bot = bot;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        DiscordBot.Command command = bot.getCommand(commandName);
        if (command != null) command.execute(event);
    }
}
