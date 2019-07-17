package com.dsoccer1980;

import com.dsoccer1980.domain.*;
import com.dsoccer1980.repository.Repository;

import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameStart3 {

    private final Repository repository;
    private Game game;
    private int currentGold = 0;
    private Probability[] probabilities = Probability.values();


    public GameStart3(Repository repository) {
        this.repository = repository;
    }

    public void start() {
        game = repository.getGameStartParameters();
        int lives = game.getLives().intValue();
        currentGold = game.getGold().intValue();

        do {
            List<Message> messages = repository.getMessages(game.getGameId());
            decodeEncryptedMessages(messages);
            List<Message> bestMessages = getBestMessages(messages);

            for (Message message : bestMessages) {
                if (message.getExpiresIn() > 1 && lives != 1) {
                    decidePurchaseItems(message.getMessage());
                }

                Solution solution = repository.solveTask(game.getGameId(), message.getAdId());
                if (solution != null) {
                    currentGold = solution.getGold().intValue();
                    lives = solution.getLives().intValue();
                    if (lives == 1) {
                        purchaseItem("hpot");
                    }
                }

                if (lives == 0) {
                    System.out.println(solution);
                    break;
                }
            }
        } while (lives > 0);


    }

    private void decodeEncryptedMessages(List<Message> messages) {
        messages.stream()
                .filter(message -> message.getEncrypted() != null && message.getEncrypted().equals("1"))
                .forEach(message -> {
                    message.setMessage(new String(Base64.getDecoder().decode(message.getMessage())));
                    message.setProbability(new String(Base64.getDecoder().decode(message.getProbability())));
                    message.setAdId(new String(Base64.getDecoder().decode(message.getAdId())));
                });
    }


    private void purchaseItem(String itemId) {
        int itemCost = repository.getListItemsInShop(game.getGameId()).stream()
                .filter(shop -> shop.getId().equals(itemId))
                .mapToInt(shop -> shop.getCost().intValue()).findFirst().orElse(-1);


        if (itemCost != -1 && currentGold >= itemCost) {
            Purchase purchase = repository.purchaseItem(game.getGameId(), itemId);
            currentGold = purchase.getGold().intValue();
        }
    }

    private void decidePurchaseItems(String description) {
        if (description != null) {
            if (description.contains("Escort") || description.contains("transport")) {
                purchaseItem("wingpot");
            } else if (description.contains("advertisement") || description.contains("agreement")) {
                purchaseItem("mtrix");
            } else if (description.contains("Steal")) {
                purchaseItem("tricks");
            } else if (description.contains("defending")) {
                purchaseItem("iron");
                // purchaseItem("gas", 100, currentGold);
            }
        }
    }


    private List<Message> getBestMessages(List<Message> messages) {
        Comparator<Message> comparator = Comparator.comparing(
                Message::getExpiresIn)
                .thenComparing(Message::getReward).reversed();

        for (Probability probability : probabilities) {
            List<Message> messageList = messages.stream()
                    .filter(message -> probability.getProbability().equals(message.getProbability()))
                    .sorted(comparator)
                    .collect(Collectors.toList());
            if (messageList.size() != 0) {
                return messageList;
            }
        }
        return Collections.emptyList();
    }

}
