package com.dsoccer1980;


import com.dsoccer1980.domain.Solution;
import com.dsoccer1980.repository.Repository;
import com.dsoccer1980.service.GameDecision;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class);

        Repository repository = context.getBean(Repository.class);
        GameDecision gameDecision = context.getBean(GameDecision.class);
        Solution solution = new GameStart(repository, gameDecision).start();
        System.out.println(solution);

        context.close();
    }
}
