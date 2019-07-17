package com.dsoccer1980.repository;

import com.dsoccer1980.domain.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Repository {

    private final RestTemplate restTemplate;
    private final String BEGIN_URL = "https://dragonsofmugloar.com";

    public Repository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Game getGameStartParameters() {
        return restTemplate.exchange(
                BEGIN_URL + "/api/v2/game/start",
                HttpMethod.POST,
                null,
                Game.class)
                .getBody();

    }

    public Reputation getReputation(String gameId) {
        return restTemplate.exchange(
                BEGIN_URL + "/api/v2/{gameId}/investigate/reputation",
                HttpMethod.POST,
                null,
                Reputation.class,
                Collections.singletonMap("gameId", gameId))
                .getBody();
    }

    public List<Message> getMessages(String gameId) {
        return restTemplate.exchange(
                BEGIN_URL + "/api/v2/{gameId}/messages",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Message>>() {
                },
                Collections.singletonMap("gameId", gameId))
                .getBody();
    }

    public Solution solveTask(String gameId, String adId) {
        Map<String, String> map = new HashMap<>();
        map.put("gameId", gameId);
        map.put("adId", adId);

        try {
            return restTemplate.exchange(
                    BEGIN_URL + "/api/v2/{gameId}/solve/{adId}",
                    HttpMethod.POST,
                    null,
                    Solution.class,
                    map)
                    .getBody();
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    public List<Shop> getListItemsInShop(String gameId) {
        return restTemplate.exchange(
                BEGIN_URL + "/api/v2/{gameId}/shop",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Shop>>() {
                },
                Collections.singletonMap("gameId", gameId))
                .getBody();
    }

    public Purchase purchaseItem(String gameId, String itemId) {
        Map<String, String> map = new HashMap<>();
        map.put("gameId", gameId);
        map.put("itemId", itemId);

        return restTemplate.exchange(
                BEGIN_URL + "/api/v2/{gameId}/shop/buy/{itemId}",
                HttpMethod.POST,
                null,
                Purchase.class,
                map)
                .getBody();
    }


}
