package com.dsoccer1980.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {

    private String adId;
    private String message;
    private String reward;
    private String encrypted;
    private Integer expiresIn;
    private String probability;

}
