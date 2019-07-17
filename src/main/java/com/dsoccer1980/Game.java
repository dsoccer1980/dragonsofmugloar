package com.dsoccer1980;

import com.dsoccer1980.domain.GameEntity;
import com.dsoccer1980.domain.Message;
import com.dsoccer1980.domain.Purchase;
import com.dsoccer1980.domain.Solution;
import com.dsoccer1980.service.GameDecision;
import com.dsoccer1980.service.RequestService;

import java.util.List;

public class Game {

    private final RequestService requestService;
    private final GameDecision gameDecision;
    private GameEntity gameEntity;
    private int currentGold = 0;
    private int lives = 0;
    private boolean isGameTerminated = false;


    public Game(RequestService requestService, GameDecision gameDecision) {
        this.requestService = requestService;
        this.gameDecision = gameDecision;
    }

    public Solution start() {
        gameEntity = requestService.getGameStartParameters();
        lives = gameEntity.getLives().intValue();
        currentGold = gameEntity.getGold().intValue();

        do {
            Solution solution = step();
            if (lives == 0) {
                return solution;
            }

        } while (lives > 0 && !isGameTerminated);

        return null;
    }

    private Solution step() {
        List<Message> messages = requestService.getMessages(gameEntity.getGameId());
        Message bestMessage = gameDecision.getBestMessage(messages);
        if (bestMessage != null) {
            Purchase purchase = gameDecision.purchaseOrNotItem(bestMessage, lives, gameEntity, currentGold);
            if (purchase != null) {
                currentGold = purchase.getGold().intValue();
            }
            Solution solution = requestService.solveTask(gameEntity.getGameId(), bestMessage.getAdId());
            if (solution != null) {
                currentGold = solution.getGold().intValue();
                lives = solution.getLives().intValue();
                return solution;
            }
        }
        return null;
    }

    public void setGameTerminated(boolean gameTerminated) {
        isGameTerminated = gameTerminated;
    }
}
