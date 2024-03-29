package com.dsoccer1980;

import com.dsoccer1980.domain.GameEntity;
import com.dsoccer1980.domain.Message;
import com.dsoccer1980.domain.Purchase;
import com.dsoccer1980.domain.Solution;
import com.dsoccer1980.service.GameDecision;
import com.dsoccer1980.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class Game {

    private static final Logger log = LoggerFactory.getLogger(Game.class);
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
        log.info("Game start parameters:" + gameEntity);
        lives = gameEntity.getLives().intValue();
        currentGold = gameEntity.getGold().intValue();

        Solution lastNotNullSolution = null;
        Solution solution;

        do {
            solution = step().orElse(lastNotNullSolution);
            lastNotNullSolution = solution;

        } while (lives > 0 && !isGameTerminated);

        return solution;
    }

    private Optional<Solution> step() {
        List<Message> messages = requestService.getMessages(gameEntity.getGameId());
        if (messages.size() == 0) {
            setGameTerminated(true);
        } else {
            Message bestMessage = gameDecision.getBestMessage(messages);
            if (bestMessage != null) {
                log.info("Task to solve:" + bestMessage);
                Purchase purchase = gameDecision.purchaseItemIfNecessary(bestMessage, lives, gameEntity, currentGold);
                if (purchase != null) {
                    log.info("Purchase:" + purchase);
                    currentGold = purchase.getGold().intValue();
                }

                Solution solution = requestService.solveTask(gameEntity.getGameId(), bestMessage.getAdId());
                if (solution != null) {
                    log.info("Solution:" + solution);
                    currentGold = solution.getGold().intValue();
                    lives = solution.getLives().intValue();
                    return Optional.of(solution);
                }
            }
        }
        return Optional.empty();
    }

    public void setGameTerminated(boolean gameTerminated) {
        isGameTerminated = gameTerminated;
    }
}
