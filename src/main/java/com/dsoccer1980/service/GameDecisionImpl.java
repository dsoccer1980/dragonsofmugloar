package com.dsoccer1980.service;

import com.dsoccer1980.domain.GameEntity;
import com.dsoccer1980.domain.Message;
import com.dsoccer1980.domain.Probability;
import com.dsoccer1980.domain.Purchase;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameDecisionImpl implements GameDecision {

    private final RequestService requestService;
    private Probability[] probabilities = Probability.values();

    public GameDecisionImpl(RequestService requestService) {
        this.requestService = requestService;
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
    public Purchase purchaseOrNotItem(Message message, int lives, GameEntity gameEntity, int currentGold) {
        Purchase purchase = null;
        if (lives == 1) {
            purchase = purchaseItem("hpot", gameEntity, currentGold);
        } else if (message.getExpiresIn() > 1) {
            purchase = decidePurchaseItems(message.getMessage(), gameEntity, currentGold);
        }
        return purchase;
    }

    private Purchase purchaseItem(String itemId, GameEntity gameEntity, int currentGold) {
        Purchase purchase = null;
        int itemCost = requestService.getListItemsInShop(gameEntity.getGameId()).stream()
                .filter(shop -> shop.getId().equals(itemId))
                .mapToInt(shop -> shop.getCost().intValue()).findFirst().orElse(-1);


        if (itemCost != -1 && currentGold >= itemCost) {
            purchase = requestService.purchaseItem(gameEntity.getGameId(), itemId);
        }
        return purchase;
    }

    private Purchase decidePurchaseItems(String description, GameEntity gameEntity, int currentGold) {
        Purchase purchase = null;
        if (description != null) {
            if (description.contains("Escort") || description.contains("transport")) {
                purchase = purchaseItem("wingpot", gameEntity, currentGold);
            } else if (description.contains("advertisement") || description.contains("agreement")) {
                purchase = purchaseItem("mtrix", gameEntity, currentGold);
            } else if (description.contains("Steal")) {
                purchase = purchaseItem("tricks", gameEntity, currentGold);
            } else if (description.contains("defending")) {
                purchase = purchaseItem("iron", gameEntity, currentGold);
            }
        }
        return purchase;
    }
}
