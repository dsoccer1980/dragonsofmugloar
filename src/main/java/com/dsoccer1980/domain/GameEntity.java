package com.dsoccer1980.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameEntity {

    private String gameId;
    private Number lives;
    private Number gold;
    private Number level;
    private Number score;
    private Number highScore;
    private Number turn;
}
