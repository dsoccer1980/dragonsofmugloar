package com.dsoccer1980.service;

import com.dsoccer1980.domain.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {RequestServiceImpl.class})
class RequestServiceImplTest {

    @MockBean
    RestTemplate restTemplate;
    @Autowired
    RequestService requestService;

    @Test
    void getGameStartParameters() {
        GameEntity gameEntity = new GameEntity("id", 1, 0, 0, 0, 0, 0);
        ResponseEntity<GameEntity> gameEntityResponseEntity = new ResponseEntity<>(gameEntity, HttpStatus.OK);

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<GameEntity>>any()))
                .thenReturn(gameEntityResponseEntity);

        assertThat(requestService.getGameStartParameters()).isEqualTo(gameEntity);

    }

    @Test
    void getReputation() {
        GameEntity gameEntity = new GameEntity("id", 1, 0, 0, 0, 0, 0);
        Reputation reputation = new Reputation(1, 1, 1);
        ResponseEntity<Reputation> responseEntity = new ResponseEntity<>(reputation, HttpStatus.OK);

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<Reputation>>any(),
                ArgumentMatchers.anyMap()))
                .thenReturn(responseEntity);

        assertThat(requestService.getReputation(gameEntity.getGameId())).isEqualTo(responseEntity.getBody());

    }

    @Test
    void getMessages() {
        GameEntity gameEntity = new GameEntity("id", 1, 0, 0, 0, 0, 0);
        Message message = new Message("adI", "any", "any", null, 1, "any");
        ResponseEntity<List<Message>> responseEntity = new ResponseEntity<>(Arrays.asList(message), HttpStatus.OK);

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<Message>>>any(),
                ArgumentMatchers.anyMap()))
                .thenReturn(responseEntity);

        assertThat(requestService.getMessages(gameEntity.getGameId())).isEqualTo(responseEntity.getBody());
    }

    @Test
    void solveTask() {
        GameEntity gameEntity = new GameEntity("id", 1, 0, 0, 0, 0, 0);
        Solution solution = new Solution(true, 1, 1, 1, 1, 1, "any");
        ResponseEntity<Solution> responseEntity = new ResponseEntity<>(solution, HttpStatus.OK);

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<Solution>>any(),
                ArgumentMatchers.anyMap()))
                .thenReturn(responseEntity);

        assertThat(requestService.solveTask(gameEntity.getGameId(), "anyaddId")).isEqualTo(responseEntity.getBody());
    }

    @Test
    void getListItemsInShop() {
        GameEntity gameEntity = new GameEntity("id", 1, 0, 0, 0, 0, 0);
        Shop shop = new Shop("anyId", "any", 1);
        ResponseEntity<List<Shop>> responseEntity = new ResponseEntity<>(Arrays.asList(shop), HttpStatus.OK);

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<Shop>>>any(),
                ArgumentMatchers.anyMap()))
                .thenReturn(responseEntity);

        assertThat(requestService.getListItemsInShop(gameEntity.getGameId())).isEqualTo(responseEntity.getBody());
    }

    @Test
    void purchaseItem() {
        GameEntity gameEntity = new GameEntity("id", 1, 0, 0, 0, 0, 0);
        Purchase purchase = new Purchase("success", 1, 1, 1, 1);
        ResponseEntity<Purchase> responseEntity = new ResponseEntity<>(purchase, HttpStatus.OK);

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<Purchase>>any(),
                ArgumentMatchers.anyMap()))
                .thenReturn(responseEntity);

        assertThat(requestService.purchaseItem(gameEntity.getGameId(), "anyItemId")).isEqualTo(responseEntity.getBody());
    }
}