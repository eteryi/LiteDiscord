package dev.etery.litebow.discord.discord.command;

import dev.etery.litebow.discord.discord.DiscordBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class TestCommand implements DiscordBot.Command {
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("pong").setEphemeral(true).queue();
    }
}
