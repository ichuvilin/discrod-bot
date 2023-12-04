package com.ichuvilin.discrodbot.commands.impl.music;

import com.ichuvilin.discrodbot.commands.Command;
import com.ichuvilin.discrodbot.lavaplayer.PlayerManager;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class PlayCommand extends ListenerAdapter implements Command {

    private final PlayerManager playerManager;

    @Override
    public SlashCommandData getCommands() {
        return Commands.slash("play", "play music in current voice channel")
                .addOption(OptionType.STRING, "query", "link on music")
                .setDescriptionLocalization(DiscordLocale.RUSSIAN, "воспроизвести музыку в текущем голосовом канале");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("play")) {
            var member = event.getMember();
            var voiceState = Objects.requireNonNull(member).getVoiceState();

            if (!Objects.requireNonNull(voiceState).inAudioChannel()) {
                event.reply("You need to be in a voice channel").queue();
                return;
            }

            var self = Objects.requireNonNull(event.getGuild()).getSelfMember();
            var selfVoice = self.getVoiceState();

            if (!Objects.requireNonNull(selfVoice).inAudioChannel()) {
                event.getGuild().getAudioManager().openAudioConnection(voiceState.getChannel());
            } else {
                if (selfVoice.getChannel() != voiceState.getChannel()) {
                    event.reply("You need to be in the same channel as me").queue();
                    return;
                }
            }

            var query = Objects.requireNonNull(event.getOption("query")).getAsString();
            playerManager.play(event, query);
        }
    }
}