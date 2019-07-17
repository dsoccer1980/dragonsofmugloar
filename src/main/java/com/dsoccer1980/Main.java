package com.dsoccer1980;


import com.dsoccer1980.repository.Repository;
import com.dsoccer1980.service.GameDecisionImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class);

        Repository repository = context.getBean(Repository.class);
        new GameStart(repository, new GameDecisionImpl(repository)).start();
        context.close();
    }
}
