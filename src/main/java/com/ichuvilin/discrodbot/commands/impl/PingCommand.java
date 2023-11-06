package com.ichuvilin.discrodbot.commands.impl;

import com.ichuvilin.discrodbot.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.stereotype.Component;

@Component
public class PingCommand extends ListenerAdapter implements Command {

    @Override
    public SlashCommandData getCommands() {
        return Commands.slash("ping", "just test");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            event.reply("PONG!!!").queue();
        }
    }
}
