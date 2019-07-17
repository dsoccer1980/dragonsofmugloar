package com.dsoccer1980.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private Integer messageId;
    private String adId;
    private String message;
    private String reward;
    private String encrypted;
    private Integer expiresIn;
    private String probability;

    public Message(String adId, String message, String reward, String encrypted, Integer expiresIn, String probability) {
        this.adId = adId;
        this.message = message;
        this.reward = reward;
        this.encrypted = encrypted;
        this.expiresIn = expiresIn;
        this.probability = probability;
    }
}
