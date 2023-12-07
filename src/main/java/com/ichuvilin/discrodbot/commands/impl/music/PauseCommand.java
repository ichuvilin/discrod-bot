package com.ichuvilin.discrodbot.commands.impl.music;

import com.ichuvilin.discrodbot.commands.Command;
import com.ichuvilin.discrodbot.lavaplayer.PlayerManager;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PauseCommand extends ListenerAdapter implements Command {

    private final PlayerManager playerManager;

    @Override
    public SlashCommandData getCommands() {
        return Commands.slash("pause", "pause the track or play it again");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("pause")) {
            var guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
            if (guildMusicManager == null) {
                event.reply("You don't listen to music").queue();
                return;
            }
            var audioPlayer = guildMusicManager.getAudioForwarder().getAudioPlayer();
            var paused = audioPlayer.isPaused();
            audioPlayer.setPaused(!paused);
            if (paused) {
                event.reply("Playback paused").queue();
            } else {
                event.reply("Unpause playing").queue();
            }
        }
    }
}
