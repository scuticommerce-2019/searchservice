package com.scuticommerce.search.controller;

import com.scuticommerce.search.model.SearchData;
import com.scuticommerce.search.service.RedisSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/search/")
public class SearchController {

    @Autowired
    RedisSearchService service;

    @GetMapping("/{key}")
    public ResponseEntity<?> search(@PathVariable String key){

        return new ResponseEntity<>(service.getSuggestions(key), HttpStatus.OK);
    }

    @PostMapping("/createindex")
    public ResponseEntity<?> createIndex(@RequestParam("name") String name){

        SearchData data  = new SearchData();
        data.setKeyword(name);
        data.setPayload(name);
        data.setScore(0.2);

        return new ResponseEntity<>(service.createSuggestions (data), HttpStatus.OK);
    }

}
