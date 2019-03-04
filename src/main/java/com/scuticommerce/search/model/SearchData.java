package com.scuticommerce.search.model;

import lombok.Data;

@Data
public class SearchData<T> {

    String id;
    String keyword;
    String category;
    T payload; //generic payload will be transformed into Json and stored in redis
    Double score;
}
