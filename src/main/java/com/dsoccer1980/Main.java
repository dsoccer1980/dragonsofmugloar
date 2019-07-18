package com.dsoccer1980;


import com.dsoccer1980.domain.Solution;
import com.dsoccer1980.service.GameDecision;
import com.dsoccer1980.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class);

        RequestService requestService = context.getBean(RequestService.class);
        GameDecision gameDecision = context.getBean(GameDecision.class);
        Solution solution = new Game(requestService, gameDecision).start();
        log.info("Your last solution:" + solution.toString());

        context.close();
    }
}
