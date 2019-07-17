package com.dsoccer1980.service;

import com.dsoccer1980.domain.GameEntity;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {RequestServiceImpl.class})
class RequestServiceImplTest {

    private final String BEGIN_URL = "https://dragonsofmugloar.com";
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
    }

    @Test
    void getMessages() {
    }

    @Test
    void solveTask() {
    }

    @Test
    void getListItemsInShop() {
    }

    @Test
    void purchaseItem() {
    }
}