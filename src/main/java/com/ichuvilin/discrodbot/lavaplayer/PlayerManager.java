package com.ichuvilin.discrodbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class PlayerManager {
    private final Map<Long, GuildMusicManager> guildMusicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.guildMusicManagers = new ConcurrentHashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        return guildMusicManagers.computeIfAbsent(guild.getIdLong(), guildId -> {
            GuildMusicManager musicManager = new GuildMusicManager(audioPlayerManager, guild);

            guild.getAudioManager().setSendingHandler(musicManager.getAudioForwarder());

            return musicManager;
        });
    }

    public void play(SlashCommandInteractionEvent event, String trackUrl) {
        var guildMusicManager = getGuildMusicManager(Objects.requireNonNull(event.getGuild()));
        audioPlayerManager.loadItemOrdered(guildMusicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                var embedBuilder = new EmbedBuilder();
                var empty = guildMusicManager.getTrackScheduler().isEmpty();
                if (empty) {
                    embedBuilder.setDescription(String.format("Started playing %s", track.getInfo().title));
                } else {
                    embedBuilder.setTitle("Adding track to queue");

                    long songDurationInMillis = track.getInfo().length;
                    long seconds = songDurationInMillis / 1000;
                    long minutes = seconds / 60;
                    
                    embedBuilder.addField("Track", track.getInfo().title, false);
                    embedBuilder.addField("Track Length", String.format("%d:%02d", minutes, seconds % 60), false);
                }

                event.replyEmbeds(embedBuilder.build()).queue();
                guildMusicManager.getTrackScheduler().queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                var tracks = playlist.getTracks();
                var embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Playlist");
                embedBuilder.setDescription(String.format("Started playing %s", playlist.getName()));
                event.replyEmbeds(embedBuilder.build()).queue();
                for (var track :
                        tracks) {
                    guildMusicManager.getTrackScheduler().queue(track);
                }
            }

            @Override
            public void noMatches() {
                event.replyEmbeds(new EmbedBuilder().setDescription("Nothing found by " + trackUrl).build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.replyEmbeds(new EmbedBuilder().setDescription("Could not play: " + exception.getMessage()).build()).queue();
            }
        });
    }
}
