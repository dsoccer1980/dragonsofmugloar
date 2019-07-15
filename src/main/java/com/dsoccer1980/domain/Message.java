package com.dsoccer1980.domain;

import lombok.Data;

@Data
public class Message {

    private Object[] messages;
    private String adId;
    private String message;
    private String reward;
    private Number expiresIn;
    private String probability;
}
