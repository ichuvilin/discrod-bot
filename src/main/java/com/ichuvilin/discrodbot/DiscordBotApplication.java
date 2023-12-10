package com.ichuvilin.discrodbot;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DiscordBotApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(DiscordBotApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
