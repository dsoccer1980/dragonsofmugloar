package com.dsoccer1980.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Solution {

    private Boolean success;
    private Number lives;
    private Number gold;
    private Number score;
    private Number highScore;
    private Number turn;
    private String message;

}
