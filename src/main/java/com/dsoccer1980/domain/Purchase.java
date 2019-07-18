package com.dsoccer1980.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Purchase {

    private String shoppingSuccess;
    private Number gold;
    private Number lives;
    private Number level;
    private Number turn;
}
