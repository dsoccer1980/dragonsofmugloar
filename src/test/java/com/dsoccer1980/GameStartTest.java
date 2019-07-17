package com.dsoccer1980;

import com.dsoccer1980.domain.Game;
import com.dsoccer1980.domain.Message;
import com.dsoccer1980.domain.Probability;
import com.dsoccer1980.domain.Solution;
import com.dsoccer1980.service.RequestService;
import com.dsoccer1980.service.GameDecision;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class GameStartTest {

    @MockBean
    private RequestService requestService;

    @MockBean
    private GameDecision gameDecision;

    @Test
    void testOneLifeAndFailFirstTask() {
        Game game = new Game("1", 1, 0, 0, 0, 0, 0);
        when(requestService.getGameStartParameters()).thenReturn(game);
        Message message = new Message("adId1", "step", "10", null, 1, Probability.PIECE_OF_CAKE.getProbability());
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        when(requestService.getMessages(game.getGameId())).thenReturn(messages);

        when(gameDecision.getBestMessage(messages)).thenReturn(message);

        Solution solution = new Solution(false, 0, 0, 0, 0, 1, "Fail");
        when(requestService.solveTask(game.getGameId(), message.getAdId())).thenReturn(solution);


        GameStart gameStart = new GameStart(requestService, gameDecision);
        assertThat(gameStart.start()).isEqualTo(solution);
    }

    @Test
    void testOneLifeAndSuccessFirstTaskAndFailSecondTask() {
        Game game = new Game("1", 1, 0, 0, 0, 0, 0);
        when(requestService.getGameStartParameters()).thenReturn(game);

        Message message1 = new Message("adId1", "step1", "10", null, 1, Probability.PIECE_OF_CAKE.getProbability());
        Message message2 = new Message("adId2", "step2", "10", null, 2, Probability.PIECE_OF_CAKE.getProbability());

        List<Message> messages = new ArrayList<>();
        messages.add(message1);
        messages.add(message2);
        List<Message> messages2 = new ArrayList<>();
        messages.add(message2);
        when(requestService.getMessages(game.getGameId())).thenReturn(messages, messages2);
        Solution solution1 = new Solution(true, 1, 10, 10, 0, 1, "Success");
        when(requestService.solveTask(game.getGameId(), message1.getAdId())).thenReturn(solution1);

        when(gameDecision.getBestMessage(messages)).thenReturn(message1);

        when(gameDecision.getBestMessage(messages2)).thenReturn(message2);


        Solution solution2 = new Solution(false, 0, 10, 10, 0, 2, "Fail");
        when(requestService.solveTask(game.getGameId(), message2.getAdId())).thenReturn(solution2);


        GameStart gameStart = new GameStart(requestService, gameDecision);
        assertThat(gameStart.start()).isEqualTo(solution2);
    }

}
