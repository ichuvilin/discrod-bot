package com.ichuvilin.discrodbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;

import java.nio.ByteBuffer;

@Slf4j
public class AudioPlayerSendHandler implements AudioSendHandler {

    @Getter
    private final AudioPlayer audioPlayer;
    private final Guild guild;
    private final ByteBuffer buffer;
    private final MutableAudioFrame frame;
    private int time;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer, Guild guild) {
        this.audioPlayer = audioPlayer;
        this.guild = guild;
        this.buffer = ByteBuffer.allocate(1024);
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(this.buffer);
    }

    @Override
    public boolean canProvide() {
        boolean canProvide = audioPlayer.provide(frame);
        if (!canProvide) {
            time += 20;
            if (time >= 300_000) {
                time = 0;
//                guild.getDefaultChannel().asTextChannel().sendMessage("No tracks have been playing for the past 3 minutes, leaving :wave:").queue();
                guild.getAudioManager().closeAudioConnection();
            }
        } else {
            time = 0;
        }
        return canProvide;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return buffer.flip();
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
