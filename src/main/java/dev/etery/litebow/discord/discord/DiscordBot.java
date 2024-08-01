package dev.etery.litebow.discord.discord;

import dev.etery.litebow.discord.discord.listener.BasicListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class DiscordBot {
    public interface Command {
        void execute(SlashCommandInteractionEvent event);
    }

    private JDA instance;
    private final String token;
    private final HashMap<String, Command> commands;

    public DiscordBot(String token) {
        this.token = token;
        this.commands = new HashMap<>();
    }

    public void load(Activity.ActivityType type, String message) throws InterruptedException {
        instance = JDABuilder.createDefault(token)
                .addEventListeners(new BasicListener(this))
                .setActivity(Activity.of(type, message))
                .build().awaitReady();
    }

    public void addEvent(ListenerAdapter listenerAdapter) {
        instance.addEventListener(listenerAdapter);
    }

    public void addCommand(SlashCommandData command, Command event) {
        instance.updateCommands().addCommands(command).queue();
        this.commands.put(command.getName(), event);
    }

    public @Nullable Command getCommand(String commandName) {
        return this.commands.get(commandName);
    }
}
