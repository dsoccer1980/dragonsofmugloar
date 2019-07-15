package com.dsoccer1980;

import com.dsoccer1980.domain.*;
import com.dsoccer1980.repository.Repository;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GameStart {

    private final Repository repository;
    private Game game;
    private Reputation reputation;

    public GameStart(Repository repository) {
        this.repository = repository;
    }

    public void start() {
        game = repository.getGameStartParameters();
        reputation = repository.getReputation(game.getGameId());
        Set<String> probabilityList = new HashSet<>();
        probabilityList.add(Probability.PIECE_OF_CAKE);
        probabilityList.add(Probability.WALK_IN_THE_PARK);
        probabilityList.add(Probability.SURE_THING);
        probabilityList.add(Probability.QUITE_LIKELY);

        List<Shop> listItemsInShop = repository.getListItemsInShop(game.getGameId());


        int lives = game.getLives().intValue();
        int listEmptyCount = 0;
        do {
            if (listEmptyCount == 4) {
                probabilityList.add(Probability.HMMM);
            } else if (listEmptyCount == 5) {
                probabilityList.add(Probability.GAMBLE);
            } else {
                listEmptyCount = 0;
            }

            for (String probability : probabilityList) {
                List<Message> messages = repository.getMessages(game.getGameId());
                List<Message> messageList = getFilteredMessages(messages, probability);
                if (messageList.size() == 0) {
                    listEmptyCount++;
                }

                System.out.println(messageList);
                System.out.println();

                for (Message message : messageList) {
                    Solution solution = repository.solveMessage(game.getGameId(), message.getAdId());

                    if (solution != null) {
                        System.out.println("<<<<<<<<<<Reputation:" + repository.getReputation(game.getGameId()));
                        if (!solution.getSuccess()) {
                            System.out.println(">>>>>Fail: " + message);
                            System.out.println("Solution " + solution);
                            System.out.println();
                        }
                        lives = solution.getLives().intValue();
                        if (listItemsInShop.size() != 0 && lives > 0) {
                            purchaseItem(listItemsInShop, solution.getScore());
                        }
                    }
                    if (lives == 1) {
                        Purchase hpot = repository.purchaseItem(game.getGameId(), "hpot");
                        System.out.println("!!!!!!!!!" + hpot);
                    }
                }


            }
        } while (lives > 0);

    }

    private void purchaseItem(List<Shop> listItemsInShop, Number score) {
        Shop itemForPurchase = listItemsInShop.get(0);
        if (score.intValue() >= itemForPurchase.getCost().intValue()) {
            Purchase purchase = repository.purchaseItem(game.getGameId(), itemForPurchase.getId());
            if (purchase.getShoppingSuccess().equals("true")) {
                System.out.println(">>>>>>purchase: " + itemForPurchase);
                listItemsInShop.remove(itemForPurchase);
            }
        }
    }

    private List<Message> getFilteredMessages(List<Message> messages, String probability) {
        return messages.stream()
                .filter(mes -> mes.getProbability().equals(probability))
                .sorted(Comparator.comparing(mes -> (int) mes.getExpiresIn()))
                .collect(Collectors.toList());
    }

}
