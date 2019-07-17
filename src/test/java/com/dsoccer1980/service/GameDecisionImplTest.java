package com.dsoccer1980.service;

import com.dsoccer1980.domain.Message;
import com.dsoccer1980.domain.Probability;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

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


}
