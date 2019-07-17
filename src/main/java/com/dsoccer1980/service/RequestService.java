package com.dsoccer1980.service;

import com.dsoccer1980.domain.*;

import java.util.List;

public interface RequestService {
    GameEntity getGameStartParameters();

    Reputation getReputation(String gameId);

    List<Message> getMessages(String gameId);

    Solution solveTask(String gameId, String adId);

    List<Shop> getListItemsInShop(String gameId);

    Purchase purchaseItem(String gameId, String itemId);
}
