package com.dsoccer1980;

import com.dsoccer1980.domain.Game;
import com.dsoccer1980.domain.Message;
import com.dsoccer1980.domain.Purchase;
import com.dsoccer1980.domain.Solution;
import com.dsoccer1980.repository.Repository;
import com.dsoccer1980.service.GameDecision;

import java.util.List;

public class GameStart {

    private final Repository repository;
    private final GameDecision gameDecision;
    private Game game;
    private int currentGold = 0;
    private int lives = 0;


    public GameStart(Repository repository, GameDecision gameDecision) {
        this.repository = repository;
        this.gameDecision = gameDecision;
    }

    public Solution start() {
        game = repository.getGameStartParameters();
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
        List<Message> messages = repository.getMessages(game.getGameId());
        Message bestMessage = gameDecision.getBestMessage(messages);
        if (bestMessage != null) {
            Purchase purchase = gameDecision.purchaseOrNotItem(bestMessage, lives, game, currentGold);
            if (purchase != null) {
                currentGold = purchase.getGold().intValue();
            }
            Solution solution = repository.solveTask(game.getGameId(), bestMessage.getAdId());
            if (solution != null) {
                currentGold = solution.getGold().intValue();
                lives = solution.getLives().intValue();
                return solution;
            }
        }
        return null;
    }

}
