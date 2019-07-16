package com.dsoccer1980.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;
    private String adId;
    private String message;
    private String reward;
    private Integer expiresIn;
    private String probability;

    public Message(String adId, String message, String reward, Integer expiresIn, String probability) {
        this.adId = adId;
        this.message = message;
        this.reward = reward;
        this.expiresIn = expiresIn;
        this.probability = probability;
    }
}
