package com.ichuvilin.discrodbot.config;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LavaplayerConfig {
    @Bean
    public AudioPlayer audioPlayer() {
        return new DefaultAudioPlayerManager().createPlayer();
    }
}
