package com.dsoccer1980;

import com.dsoccer1980.domain.*;
import com.dsoccer1980.repository.AdSolutionRepository;
import com.dsoccer1980.repository.MessageRepository;
import com.dsoccer1980.repository.Repository;
import com.dsoccer1980.repository.ShortMessageRepository;

import java.util.*;
import java.util.stream.Collectors;

public class GameStart {

    private final Repository repository;
    private final MessageRepository messageRepository;
    private final AdSolutionRepository adSolutionRepository;
    private final ShortMessageRepository shortMessageRepository;
    private Game game;
    private Reputation reputation;
    private String purchaseItemId = "";
    private int currentGold = 0;

    private String[] probabilities = new String[]{Probability.PIECE_OF_CAKE, Probability.WALK_IN_THE_PARK, Probability.SURE_THING, Probability.QUITE_LIKELY, Probability.HMMM, Probability.GAMBLE, Probability.RISKY, Probability.PLAYING_WITH_FIRE, Probability.RATHER_DETRIMENTAL, Probability.IMPOSSIBLE, Probability.SUICIDE_MISSION};

    public GameStart(Repository repository, MessageRepository messageRepository, AdSolutionRepository adSolutionRepository, ShortMessageRepository shortMessageRepository) {
        this.repository = repository;
        this.messageRepository = messageRepository;
        this.adSolutionRepository = adSolutionRepository;
        this.shortMessageRepository = shortMessageRepository;
    }

    public void start2() {
        game = repository.getGameStartParameters();
        List<Shop> listItemsInShop = repository.getListItemsInShop(game.getGameId());
        int lives = game.getLives().intValue();


        currentGold = game.getGold().intValue();
        do {
            List<Message> messages = repository.getMessages(game.getGameId());
            System.out.println(">>>encrypt:");
            messages.stream()
                    .filter(mes -> mes.getEncrypted() != null && mes.getEncrypted().equals("1"))
                    .forEach(mes -> {
                        mes.setMessage(new String(Base64.getDecoder().decode(mes.getMessage())));
                        mes.setProbability(new String(Base64.getDecoder().decode(mes.getProbability())));
                        mes.setAdId(new String(Base64.getDecoder().decode(mes.getAdId())));
                    });

            List<Message> messageList = getFilteredMessages3(messages);
            System.out.println("------------------------");
            messages.forEach(System.out::println);
            System.out.println("------------------------");
//            System.out.println("Filtered messaged");
//            messageList.forEach(System.out::println);
//            System.out.println("------------------------");


            for (Message message : messageList) {
                purchaseItemId = "";
                if (message.getExpiresIn() > 1 && (lives != 1 )) {
                    if (message.getMessage().contains("Escort") || message.getMessage().contains("transport")) {
                        purchaseItem("wingpot", 100, currentGold);
                    } else if (message.getMessage().contains("advertisement") || message.getMessage().contains("agreement")) {
                        purchaseItem("mtrix", 300, currentGold);
                    } else if (message.getMessage().contains("Steal")) {
                        purchaseItem("tricks", 100, currentGold);
                    } else if (message.getMessage().contains("defending")) {
                        purchaseItem("iron", 300, currentGold);
                        purchaseItem("gas", 100, currentGold);
                        // purchaseItem("cs", 100, currentGold);
                    }

                }

                Solution solution = repository.solveMessage(game.getGameId(), message.getAdId());
                if (solution != null) {
                    System.out.println(message);
                    System.out.println(solution);
                    currentGold = solution.getGold().intValue();
                    //  saveToDB(solution, message);

                    List<String> listShortMessages = Arrays.asList("advertisement", "Escort", "defending", "to sell", "to write", "to clean", "to reach", "to fix", "Steal", "Investigate");

                    for (String listShortMessage : listShortMessages) {
                        if (message.getMessage().contains(listShortMessage)) {
                            shortMessageRepository.save(new ShortMessage(listShortMessage, message.getMessage(), solution.getSuccess(), purchaseItemId));
                            break;
                        }
                    }


                    lives = solution.getLives().intValue();
                    if (lives == 1) {
                        Purchase hpot = repository.purchaseItem(game.getGameId(), "hpot");
                        if (hpot.getShoppingSuccess().equals("true")) {
                            currentGold -= 100;
                        }
                        //System.out.println("!!!!!!!!!" + hpot);
                        System.out.println(">>>>>>Reputation: " + repository.getReputation(game.getGameId()));
                    }
                } else {
                    System.out.println("solution = null");
                }

                if (lives == 0) {
                    System.out.println(solution);
                }


            }
        } while (lives > 0);

    }

    private void saveToDB(Solution solution, Message message) {
        Optional<AdSolution> adSolutionOptional = adSolutionRepository.findById(message.getAdId());
        AdSolution adSolution = adSolutionOptional.orElse(null);
        if (!solution.getSuccess()) {
            System.out.println(">>>>>Fail: " + message);
            if (adSolution != null) {
                adSolution.setSuccess(adSolution.getSuccess() + 1);
            } else {
                adSolution = new AdSolution(message.getAdId(), 1, 0);
            }
        } else {
            if (adSolution != null) {
                adSolution.setSuccess(adSolution.getFail() + 1);
            } else {
                adSolution = new AdSolution(message.getAdId(), 0, 1);
            }
        }
        adSolutionRepository.save(adSolution);
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

        Integer currentGold = game.getGold().intValue();

        int lives = game.getLives().intValue();
        int listEmptyCount = 0;
        do {
//            if (listEmptyCount == 4) {
//                probabilityList.add(Probability.HMMM);
//            } else if (listEmptyCount == 5) {
//                probabilityList.add(Probability.GAMBLE);
//            } else {
//                listEmptyCount = 0;
//            }


            List<Message> messages = repository.getMessages(game.getGameId());
            messages.forEach(messageRepository::save);


            List<Message> messageList = getFilteredMessages2(messages, probabilityList);
            if (messageList.size() == 0) {
                if (purchaseItem("wingpot", 100, currentGold)) {
                    currentGold -= 100;
                }
                probabilityList.add(Probability.HMMM);
                messageList = getFilteredMessages2(messages, probabilityList);
            }
            if (messageList.size() == 0) {
                if (purchaseItem("mtrix", 300, currentGold)) {
                    currentGold -= 300;
                }
                probabilityList.add(Probability.GAMBLE);
                messageList = getFilteredMessages2(messages, probabilityList);
            }
            if (messageList.size() == 0) {
                if (purchaseItem("wingpotmax", 300, currentGold)) {
                    currentGold -= 300;
                }
                probabilityList.add(Probability.RISKY);
                messageList = getFilteredMessages2(messages, probabilityList);
            }
            if (messageList.size() == 0) {
                if (purchaseItem("ch", 300, currentGold)) {
                    currentGold -= 300;
                }
                probabilityList.add(Probability.PLAYING_WITH_FIRE);
                messageList = getFilteredMessages2(messages, probabilityList);
            }
            if (messageList.size() == 0) {
                if (purchaseItem("tricks", 300, currentGold)) {
                    currentGold -= 300;
                }
                probabilityList.add(Probability.RATHER_DETRIMENTAL);
                messageList = getFilteredMessages2(messages, probabilityList);
            }

            System.out.println("-----------------------");
            messages.forEach(System.out::println);
            System.out.println("-----------------------");

            for (Message message : messageList) {
                Solution solution = repository.solveMessage(game.getGameId(), message.getAdId());
                System.out.println(message);

                if (solution != null) {
                    System.out.println(solution);
                    currentGold = solution.getGold().intValue();
                    Optional<AdSolution> adSolutionOptional = adSolutionRepository.findById(message.getAdId());
                    AdSolution adSolution = adSolutionOptional.orElse(null);

                    if (!solution.getSuccess()) {
                        System.out.println(">>>>>Fail: " + message);
                        if (adSolution != null) {
                            adSolution.setSuccess(adSolution.getSuccess() + 1);
                        } else {
                            adSolution = new AdSolution(message.getAdId(), 1, 0);
                        }
                    } else {
                        if (adSolution != null) {
                            adSolution.setSuccess(adSolution.getFail() + 1);
                        } else {
                            adSolution = new AdSolution(message.getAdId(), 0, 1);
                        }
                    }
                    adSolutionRepository.save(adSolution);
                    lives = solution.getLives().intValue();
//                        if (listItemsInShop.size() != 0 && lives > 0) {
//                            purchaseItem(listItemsInShop, solution.getScore());
//                        }
                    if (lives == 1) {
                        Purchase hpot = repository.purchaseItem(game.getGameId(), "hpot");
                        System.out.println("!!!!!!!!!" + hpot);
                    }
                } else {
                    System.out.println("solution = null");
                }

                if (lives == 0) {
                    System.out.println(solution);
                }
                System.out.println("-----------------------");
            }
            System.out.println("-----------------------");


        } while (lives > 0);

    }

    private void purchaseItem2(List<Shop> listItemsInShop, Number score) {
        Shop itemForPurchase = listItemsInShop.get(0);
        if (score.intValue() >= itemForPurchase.getCost().intValue()) {
            Purchase purchase = repository.purchaseItem(game.getGameId(), itemForPurchase.getId());
            if (purchase.getShoppingSuccess().equals("true")) {
                System.out.println(">>>>>>purchase: " + itemForPurchase);
                listItemsInShop.remove(itemForPurchase);
            }
        }
    }

    private boolean purchaseItem(String itemId, Integer itemCost, Number gold) {
        if (gold.intValue() >= itemCost) {
            Purchase purchase = repository.purchaseItem(game.getGameId(), itemId);
            if (purchase.getShoppingSuccess().equals("true")) {
                System.out.println(">>>>>>purchase: " + purchase + " " + itemId);
                purchaseItemId = itemId;
                currentGold -= itemCost;
                return true;
            }
        }
        return false;
    }

    private List<Message> getFilteredMessages(List<Message> messages, String probability) {
        return messages.stream()
                .filter(mes -> mes.getProbability().equals(probability))
                .sorted(Comparator.comparing(mes -> (int) mes.getExpiresIn()))
                .collect(Collectors.toList());
    }

    private List<Message> getFilteredMessages2(List<Message> messages, Set<String> probabilityList) {
        Comparator<Message> comparator
                = Comparator.comparing(Message::getExpiresIn)
                .thenComparing(Message::getReward).reversed();


        List<Message> collect = messages.stream()
                .filter(mes -> probabilityList.contains(mes.getProbability()))
                .sorted(comparator)
                .collect(Collectors.toList());
        List<Message> collect2 = collect.stream()
                .filter(mes -> !mes.getMessage().contains("awesome"))
                .collect(Collectors.toList());
        return collect2.size() == 0 ? collect : collect2;
    }

    private List<Message> getFilteredMessages3(List<Message> messages) {
        Comparator<Message> comparator
                = Comparator.comparing(Message::getExpiresIn)
                .thenComparing(Message::getReward).reversed();

        for (String probability : probabilities) {
            List<Message> messageList = messages.stream()
                    .filter(mes -> probability.equals(mes.getProbability()))
                    .sorted(comparator)
                    .collect(Collectors.toList());
            if (messageList.size() != 0) {
                return messageList;
            }
        }
        return Collections.emptyList();
    }

}
