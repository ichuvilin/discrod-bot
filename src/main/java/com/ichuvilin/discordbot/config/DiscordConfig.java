package com.ichuvilin.discordbot.config;

import com.ichuvilin.discordbot.commands.Command;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DiscordConfig {

    @Value("${bot.token}")
    private final String token;
    @Value("${youtube.email}")
    private final String email;
    @Value("${youtube.password}")
    private final String password;
    private final List<Command> commands;

    @Bean
    public YoutubeAudioSourceManager youtubeAudioSourceManager() {
        return new YoutubeAudioSourceManager(true, email, password);
    }

    @Bean
    public JDA builder() {
        var builder = JDABuilder
                .create(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .setActivity(Activity.playing("VALORANT"));

        commands.forEach(builder::addEventListeners);

        var build = builder.build();

        var list = commands.stream().map(Command::getCommands).toList();
        build.updateCommands().addCommands(list).queue();

        return build;
    }

}
