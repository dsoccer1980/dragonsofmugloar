package com.dsoccer1980.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String shortMessage;
    private String probability;
    private boolean solution;
    private String purchase;

    public ShortMessage(String shortMessage, String probability, boolean solution, String purchase) {
        this.shortMessage = shortMessage;
        this.probability = probability;
        this.solution = solution;
        this.purchase = purchase;
    }
}
