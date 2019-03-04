package com.scuticommerce.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scuticommerce.search.model.SearchData;
import io.redisearch.Suggestion;
import io.redisearch.client.Client;
import io.redisearch.client.SuggestionOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisSearchService {

    static Client client;

    @Autowired
    public RedisSearchService(){

        client = new Client("redisSearchIndex", "localhost", 6379);
    }

    public List<Suggestion> getSuggestions(String keyword) {

        List<Suggestion> payloads = client.getSuggestion(keyword,
                SuggestionOptions.builder().with(SuggestionOptions.With.PAYLOAD).build());

        return payloads;
    }

    public String createSuggestions(SearchData data) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            String payload = mapper.writeValueAsString(data.getPayload());
            Suggestion suggestion = Suggestion.builder().str(data.getKeyword()).payload(payload).score(
                    data.getScore()).build();

            client.addSuggestion(suggestion.toBuilder().str(data.getKeyword()).payload(payload).build(),
                    false) ;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "OK";
    }

}
