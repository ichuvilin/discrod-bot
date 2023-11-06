package com.ichuvilin.discrodbot.commands.impl.music;

import com.ichuvilin.discrodbot.commands.Command;
import com.ichuvilin.discrodbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class QueueCommand extends ListenerAdapter implements Command {

    private final PlayerManager playerManager;

    @Override
    public SlashCommandData getCommands() {
        return Commands.slash("queue", "view current track list")
                .setDescriptionLocalization(DiscordLocale.RUSSIAN, "посмотреть текущий список треков");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("queue")) {
            var guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
            List<AudioTrack> queue = new ArrayList<>(guildMusicManager.getTrackScheduler().getQueue());
            var embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Current queue");
            if (queue.isEmpty()) {
                embedBuilder.setDescription("Queue is empty");
            }
            for (int i = 0; i < queue.size(); i++) {
                AudioTrackInfo info = queue.get(i).getInfo();
                embedBuilder.addField(String.format("%d:", i + 1), info.title, true);
            }

            event.replyEmbeds(embedBuilder.build()).queue();
        }
    }
}
