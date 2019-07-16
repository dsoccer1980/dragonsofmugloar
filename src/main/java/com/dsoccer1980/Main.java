package com.dsoccer1980;


import com.dsoccer1980.repository.AdSolutionRepository;
import com.dsoccer1980.repository.MessageRepository;
import com.dsoccer1980.repository.Repository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class);
        new GameStart(context.getBean(Repository.class), context.getBean(MessageRepository.class), context.getBean(AdSolutionRepository.class)).start2();
        context.close();
    }
}
