package dev.etery.litebow.discord.discord.command;

import dev.etery.litecosmetics.Category;
import dev.etery.litecosmetics.LiteCosmetics;
import dev.etery.litecosmetics.cosmetic.Cosmetic;
import dev.etery.litecosmetics.data.CosmeticPlayer;
import dev.etery.litebow.discord.discord.DiscordBot;
import dev.etery.litebow.discord.util.MojangAPI;
import me.stephenminer.litecoin.LiteCoin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class DCStatsCommand implements DiscordBot.Command {
    private final LiteCoin coin;
    private final LiteCosmetics cosmetics;

    public DCStatsCommand(LiteCoin coin, LiteCosmetics cosmetics) {
        this.coin = coin;
        this.cosmetics = cosmetics;
    }

    private MessageEmbed from(UUID uuid, String username) {

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        CosmeticPlayer cosmeticPlayer = cosmetics.player(player);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(username + "'s stats")
                .setDescription("```json\n" +
                        "ʟɪᴛᴇᴄᴏɪɴ : " + coin.getBalance(player) + "```")
                .setThumbnail("https://skins.mcstats.com/bust/" + player.getUniqueId())
                .setFooter("litebow.net")
                .setTimestamp(LocalDateTime.now());

        Collection<Category<?>> categories = cosmetics.categories().stream().sorted(Comparator.comparing(Category::getDisplayName)).collect(Collectors.toList());

        categories.forEach(it -> {
            List<? extends Cosmetic> c = it.boughtCosmetics(cosmeticPlayer);
            StringBuilder value = new StringBuilder();
            c.forEach(cosmetic -> {
                boolean isSelected = cosmeticPlayer.getSelected(it) == cosmetic;
                String mod = isSelected ? "**" : "";
                String prefix = isSelected ? "▮" : "▯";
                String rawName = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', cosmetic.displayName())) + "           ";

                value.append("  ")
                        .append(prefix)
                        .append(" ").append(mod)
                        .append(rawName)
                        .append(mod)
                        .append("\n");
            });
            embed.addField("`" +it.getDisplayName() + "  →`", value.toString(), false);
        });
        return embed.build();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        String username = event.getOption("username").getAsString();
        Player player = Bukkit.getPlayer(username);
        MessageEmbed embed;
        if (player == null) {
            MojangAPI.UserResponse res = MojangAPI.getUUIDFromUsername(username);
            embed = from(res.getUUID(), res.name);
        } else {
            embed = from(player.getUniqueId(), player.getName());
        }

        event.getHook().sendMessageEmbeds(embed).queue();
    }
}
