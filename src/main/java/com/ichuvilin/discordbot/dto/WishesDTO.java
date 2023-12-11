package com.ichuvilin.discordbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class WishesDTO {
    private String subject;
    private String body;
    private String userId;
    private String username;
}
