package com.ichuvilin.discordbot.commands.impl.music;

import com.ichuvilin.discordbot.commands.Command;
import com.ichuvilin.discordbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StopCommand extends ListenerAdapter implements Command {

    private final PlayerManager playerManager;

    @Override
    public SlashCommandData getCommands() {
        return Commands.slash("stop", "stop the bot playing")
                .setGuildOnly(true)
                .setDescriptionLocalization(DiscordLocale.RUSSIAN, "останавливает воспроизведение");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("stop")) {
            var guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
            var audioPlayer = guildMusicManager.getAudioForwarder().getAudioPlayer();
            var trackScheduler = guildMusicManager.getTrackScheduler();
            trackScheduler.onTrackEnd(audioPlayer, null, AudioTrackEndReason.FINISHED);
            event.reply("Stopped").queue();
        }
    }
}
