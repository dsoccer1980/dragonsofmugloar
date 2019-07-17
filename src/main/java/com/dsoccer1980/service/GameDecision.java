package com.dsoccer1980.service;

import com.dsoccer1980.domain.GameEntity;
import com.dsoccer1980.domain.Message;
import com.dsoccer1980.domain.Purchase;

import java.util.List;

public interface GameDecision {
    Message getBestMessage(List<Message> messages);

    Purchase purchaseOrNotItem(Message message, int lives, GameEntity gameEntity, int currentGold);
}
