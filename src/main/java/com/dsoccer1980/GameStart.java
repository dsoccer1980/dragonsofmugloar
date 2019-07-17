package com.dsoccer1980;

import com.dsoccer1980.domain.Game;
import com.dsoccer1980.domain.Message;
import com.dsoccer1980.domain.Purchase;
import com.dsoccer1980.domain.Solution;
import com.dsoccer1980.service.RequestService;
import com.dsoccer1980.service.GameDecision;

import java.util.List;

public class GameStart {

    private final RequestService requestService;
    private final GameDecision gameDecision;
    private Game game;
    private int currentGold = 0;
    private int lives = 0;


    public GameStart(RequestService requestService, GameDecision gameDecision) {
        this.requestService = requestService;
        this.gameDecision = gameDecision;
    }

    public Solution start() {
        game = requestService.getGameStartParameters();
        lives = game.getLives().intValue();
        currentGold = game.getGold().intValue();

        do {
            Solution solution = step();
            if (lives == 0) {
                return solution;
            }

        } while (lives > 0);

        return null;
    }

    private Solution step() {
        List<Message> messages = requestService.getMessages(game.getGameId());
        Message bestMessage = gameDecision.getBestMessage(messages);
        if (bestMessage != null) {
            Purchase purchase = gameDecision.purchaseOrNotItem(bestMessage, lives, game, currentGold);
            if (purchase != null) {
                currentGold = purchase.getGold().intValue();
            }
            Solution solution = requestService.solveTask(game.getGameId(), bestMessage.getAdId());
            if (solution != null) {
                currentGold = solution.getGold().intValue();
                lives = solution.getLives().intValue();
                return solution;
            }
        }
        return null;
    }

}
