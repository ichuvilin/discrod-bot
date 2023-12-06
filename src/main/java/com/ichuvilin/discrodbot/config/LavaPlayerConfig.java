package com.ichuvilin.discrodbot.config;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class LavaPlayerConfig {

    @Value("${youtube.email}")
    private final String email;

    @Value("${youtube.password}")
    private final String password;

    @Bean
    public YoutubeAudioSourceManager youtubeAudioSourceManager() {
        return new YoutubeAudioSourceManager(true, email, password);
    }

    @Bean
    public AudioPlayer audioPlayer() {
        var player = new DefaultAudioPlayerManager().createPlayer();
        player.setVolume(100);
        return player;
    }
}
