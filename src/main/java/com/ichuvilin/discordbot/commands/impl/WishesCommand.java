package com.ichuvilin.discordbot.commands.impl;

import com.ichuvilin.discordbot.commands.Command;
import com.ichuvilin.discordbot.dto.WishesDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class WishesCommand extends ListenerAdapter implements Command {

    @Value("${topic.wishes}")
    private final String topic;
    private final KafkaTemplate<String, WishesDTO> template;

    @Override
    public SlashCommandData getCommands() {
        return Commands.slash("wishes", "...");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("wishes")) {
            var subject = TextInput.create("subject", "Subject", TextInputStyle.SHORT)
                    .setPlaceholder("Subject of this ticket")
                    .setMinLength(10)
                    .setMaxLength(100)
                    .build();
            var body = TextInput.create("body", "Body", TextInputStyle.PARAGRAPH)
                    .setMinLength(30)
                    .setMaxLength(1000)
                    .build();

            var modal = Modal.create("mod_wishes", "Wishes")
                    .addComponents(ActionRow.of(subject), ActionRow.of(body))
                    .build();
            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("mod_wishes")) {
            String subject = event.getValue("subject").getAsString();
            String body = event.getValue("body").getAsString();

            createSupportTicket(Map.of("subject", subject,
                    "body", body,
                    "user_id", event.getUser().getId(),
                    "username", event.getUser().getName()));

            event.reply("Thanks for your wishes!").setEphemeral(true).queue();
        }
    }

    private void createSupportTicket(Map<String, String> map) {
        var wishes = WishesDTO.builder()
                .subject(map.get("subject"))
                .body(map.get("body"))
                .username(map.get("username"))
                .userId(map.get("user_id"))
                .build();
        template.send(topic, wishes);
    }
}
