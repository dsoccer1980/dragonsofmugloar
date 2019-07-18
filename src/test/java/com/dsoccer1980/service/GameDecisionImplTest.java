package com.dsoccer1980.service;

import com.dsoccer1980.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {GameDecisionImpl.class})
public class GameDecisionImplTest {

    @MockBean
    RequestService requestService;

    @Autowired
    private GameDecision gameDecision;

    @Test
    @DisplayName("BestMessage of one message is this message")
    void test() {
        Message message = new Message("adId1", "step", "10", null, 1, Probability.PIECE_OF_CAKE.getProbability());
        Message bestMessage = gameDecision.getBestMessage(Arrays.asList(message));
        assertThat(bestMessage).isEqualTo(message);
    }

    @Test
    @DisplayName("BestMessage of messages is message with 'Piece of cake' probability")
    void test2() {
        Message message1 = new Message("adId1", "step", "10", null, 1, Probability.GAMBLE.getProbability());
        Message message2 = new Message("adId2", "step2", "10", null, 1, Probability.PIECE_OF_CAKE.getProbability());
        Message message3 = new Message("adId2", "step2", "10", null, 1, Probability.SURE_THING.getProbability());
        Message bestMessage = gameDecision.getBestMessage(Arrays.asList(message1, message2, message3));
        assertThat(bestMessage).isEqualTo(message2);
    }

    @Test
    @DisplayName("BestMessage of messages with equal probability is message with the most reward")
    void test3() {
        Message message1 = new Message("adId1", "step2", "10", null, 1, Probability.PIECE_OF_CAKE.getProbability());
        Message message2 = new Message("adId2", "step", "20", null, 1, Probability.PIECE_OF_CAKE.getProbability());
        Message message3 = new Message("adId3", "step", "10", null, 1, Probability.PIECE_OF_CAKE.getProbability());
        Message bestMessage = gameDecision.getBestMessage(Arrays.asList(message1, message2, message3));
        assertThat(bestMessage).isEqualTo(message2);
    }

    @Test
    @DisplayName("BestMessage of empty list is null")
    void test4() {
        Message bestMessage = gameDecision.getBestMessage(Collections.emptyList());
        assertThat(bestMessage).isEqualTo(null);
    }

    @Test
    @DisplayName("BestMessage of null is null")
    void test5() {
        Message bestMessage = gameDecision.getBestMessage(null);
        assertThat(bestMessage).isEqualTo(null);
    }

    @Test
    @DisplayName("If live==1, purchase hpot")
    void test6() {
        GameEntity gameEntity = new GameEntity("id", 1, 0, 0, 0, 0, 0);
        when(requestService.getListItemsInShop(gameEntity.getGameId())).thenReturn(Arrays.asList(new Shop("hpot", "live", 50)));
        Purchase excpectedPurchase = new Purchase("true", 50, 1, 1, 1);
        when(requestService.purchaseItem(gameEntity.getGameId(), "hpot")).thenReturn(excpectedPurchase);

        Message message = new Message("anyId", "any", "any", null, 1, "any");
        Purchase actualPurchase = gameDecision.purchaseItemIfNecessary(message, 1, gameEntity, 100);

        assertThat(actualPurchase).isEqualTo(excpectedPurchase);
    }

    @Test
    @DisplayName("If task is to excort, then purchase wingpot")
    void test7() {
        GameEntity gameEntity = new GameEntity("id", 1, 0, 0, 0, 0, 0);
        when(requestService.getListItemsInShop(gameEntity.getGameId())).thenReturn(Arrays.asList(new Shop("wingpot", "wings", 50)));
        Purchase excpectedPurchase = new Purchase("true", 50, 2, 1, 1);
        when(requestService.purchaseItem(gameEntity.getGameId(), "wingpot")).thenReturn(excpectedPurchase);

        Message message = new Message("anyId", "Escort someone to ...", "any", null, 2, "any");
        Purchase actualPurchase = gameDecision.purchaseItemIfNecessary(message, 2, gameEntity, 100);

        assertThat(actualPurchase).isEqualTo(excpectedPurchase);
    }

    @Test
    @DisplayName("If no enough money, then no purchase")
    void test8() {
        GameEntity gameEntity = new GameEntity("id", 1, 0, 0, 0, 0, 0);
        when(requestService.getListItemsInShop(gameEntity.getGameId())).thenReturn(Arrays.asList(new Shop("wingpot", "wings", 50)));
        Purchase excpectedPurchase = null;
        when(requestService.purchaseItem(gameEntity.getGameId(), "wingpot")).thenReturn(excpectedPurchase);

        Message message = new Message("anyId", "Escort someone to ...", "any", null, 2, "any");
        Purchase actualPurchase = gameDecision.purchaseItemIfNecessary(message, 2, gameEntity, 10);

        assertThat(actualPurchase).isEqualTo(excpectedPurchase);
    }


}
