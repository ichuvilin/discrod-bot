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
public class SkipCommand extends ListenerAdapter implements Command {

    private final PlayerManager playerManager;

    @Override
    public SlashCommandData getCommands() {
        return Commands.slash("skip", "skip the current song")
                .setGuildOnly(true)
                .setDescriptionLocalization(DiscordLocale.RUSSIAN, "пропускает текущий трек");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("skip")) {
            var guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
            var audioPlayer = guildMusicManager.getAudioForwarder().getAudioPlayer();
            guildMusicManager.getTrackScheduler().onTrackEnd(audioPlayer, null, AudioTrackEndReason.FINISHED);
            event.reply("Skipped").queue();
        }
    }
}
