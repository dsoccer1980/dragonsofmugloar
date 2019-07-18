package com.dsoccer1980.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Shop {

    private String id;
    private String name;
    private Number cost;

}
