package com.ichuvilin.discrodbot.commands.impl.music;

import com.ichuvilin.discrodbot.commands.Command;
import com.ichuvilin.discrodbot.lavaplayer.PlayerManager;
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
                .setDescriptionLocalization(DiscordLocale.RUSSIAN, "останавливает воспроизведение");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("stop")) {
            var guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
            var trackScheduler = guildMusicManager.getTrackScheduler();
            trackScheduler.getQueue().clear();
            trackScheduler.nextTrack();
            event.getGuild().getAudioManager().closeAudioConnection();
            event.reply("Stopped").queue();
        }
    }
}
