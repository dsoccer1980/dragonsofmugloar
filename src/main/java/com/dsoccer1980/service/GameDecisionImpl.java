package com.dsoccer1980.service;

import com.dsoccer1980.domain.Game;
import com.dsoccer1980.domain.Message;
import com.dsoccer1980.domain.Probability;
import com.dsoccer1980.domain.Purchase;
import com.dsoccer1980.repository.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameDecisionImpl implements GameDecision {


    private final Repository repository;
    private Probability[] probabilities = Probability.values();

    public GameDecisionImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Message getBestMessage(List<Message> messages) {
        Comparator<Message> comparator = Comparator.comparing(
                Message::getExpiresIn)
                .thenComparing(Message::getReward).reversed();

        for (Probability probability : probabilities) {
            List<Message> messageList = messages.stream()
                    .filter(message -> probability.getProbability().equals(message.getProbability()))
                    .sorted(comparator)
                    .collect(Collectors.toList());
            if (messageList.size() != 0) {
                return messageList.get(0);
            }
        }
        return null;
    }

    @Override
    public Purchase purchaseOrNotItem(Message message, int lives, Game game, int currentGold) {
        Purchase purchase = null;
        if (lives == 1) {
            purchase = purchaseItem("hpot", game, currentGold);
        } else if (message.getExpiresIn() > 1) {
            purchase = decidePurchaseItems(message.getMessage(), game, currentGold);
        }
        return purchase;
    }

    private Purchase purchaseItem(String itemId, Game game, int currentGold) {
        Purchase purchase = null;
        int itemCost = repository.getListItemsInShop(game.getGameId()).stream()
                .filter(shop -> shop.getId().equals(itemId))
                .mapToInt(shop -> shop.getCost().intValue()).findFirst().orElse(-1);


        if (itemCost != -1 && currentGold >= itemCost) {
            purchase = repository.purchaseItem(game.getGameId(), itemId);
        }
        return purchase;
    }

    private Purchase decidePurchaseItems(String description, Game game, int currentGold) {
        Purchase purchase = null;
        if (description != null) {
            if (description.contains("Escort") || description.contains("transport")) {
                purchase = purchaseItem("wingpot", game, currentGold);
            } else if (description.contains("advertisement") || description.contains("agreement")) {
                purchase = purchaseItem("mtrix", game, currentGold);
            } else if (description.contains("Steal")) {
                purchase = purchaseItem("tricks", game, currentGold);
            } else if (description.contains("defending")) {
                purchase = purchaseItem("iron", game, currentGold);
            }
        }
        return purchase;
    }
}
