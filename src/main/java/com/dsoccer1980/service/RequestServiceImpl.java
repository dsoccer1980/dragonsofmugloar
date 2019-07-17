package com.dsoccer1980.service;

import com.dsoccer1980.domain.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RequestServiceImpl implements RequestService {

    private final RestTemplate restTemplate;
    private final String BEGIN_URL = "https://dragonsofmugloar.com";

    public RequestServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public GameEntity getGameStartParameters() {
        return restTemplate.exchange(
                BEGIN_URL + "/api/v2/game/start",
                HttpMethod.POST,
                null,
                GameEntity.class)
                .getBody();

    }

    @Override
    public Reputation getReputation(String gameId) {
        return restTemplate.exchange(
                BEGIN_URL + "/api/v2/{gameId}/investigate/reputation",
                HttpMethod.POST,
                null,
                Reputation.class,
                Collections.singletonMap("gameId", gameId))
                .getBody();
    }

    @Override
    public List<Message> getMessages(String gameId) {
        List<Message> messageList = restTemplate.exchange(
                BEGIN_URL + "/api/v2/{gameId}/messages",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Message>>() {
                },
                Collections.singletonMap("gameId", gameId))
                .getBody();
        if (messageList != null) {
            decodeEncryptedMessages(messageList);
        }
        return messageList;
    }

    @Override
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

    @Override
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

    @Override
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

    private void decodeEncryptedMessages(List<Message> messages) {
        messages.stream()
                .filter(message -> message.getEncrypted() != null && message.getEncrypted().equals("1"))
                .forEach(message -> {
                    message.setMessage(new String(Base64.getDecoder().decode(message.getMessage())));
                    message.setProbability(new String(Base64.getDecoder().decode(message.getProbability())));
                    message.setAdId(new String(Base64.getDecoder().decode(message.getAdId())));
                });
    }


}
