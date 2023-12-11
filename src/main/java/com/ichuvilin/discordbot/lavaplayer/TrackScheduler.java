package com.ichuvilin.discordbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RequiredArgsConstructor
public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    @Getter
    private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.add(track);
        }
    }

    public boolean isEmpty() {
        return player.getPlayingTrack() == null && queue.isEmpty();
    }

    private void nextTrack() {
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        } else if (endReason.equals(AudioTrackEndReason.STOPPED)) {
            player.stopTrack();
        }
    }
}
