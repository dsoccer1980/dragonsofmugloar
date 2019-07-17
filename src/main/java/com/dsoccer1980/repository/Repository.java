package com.dsoccer1980.repository;

import com.dsoccer1980.domain.*;

import java.util.List;

public interface Repository {
    Game getGameStartParameters();

    Reputation getReputation(String gameId);

    List<Message> getMessages(String gameId);

    Solution solveTask(String gameId, String adId);

    List<Shop> getListItemsInShop(String gameId);

    Purchase purchaseItem(String gameId, String itemId);
}
