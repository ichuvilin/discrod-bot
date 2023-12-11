package com.ichuvilin.discordbot.commands;

import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;


public interface Command extends EventListener {
    SlashCommandData getCommands();
}
