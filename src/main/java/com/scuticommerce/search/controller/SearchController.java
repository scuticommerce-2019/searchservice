package com.scuticommerce.search.controller;

import com.scuticommerce.search.model.SearchData;
import com.scuticommerce.search.service.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/search/")
public class SearchController {

    private static final String PRODUCT = "product";
    private static final String ORDER = "order";


    @Autowired
    ElasticService elasticService;


    @PostMapping("/writeindex")
    public ResponseEntity<?> writeIndex(@RequestParam("indexName") String name, @RequestParam("type") String type,
                                        @RequestParam("id") String id, @RequestBody String payload){

        return new ResponseEntity<>(elasticService.writeDocument(name, type,id,payload), HttpStatus.OK);
    }

    @PostMapping("/batchindex")
    public ResponseEntity<?> batchIndex(@RequestParam("indexName") String name, @RequestParam("type") String type,
                                        @RequestParam("id") String id, @RequestBody List<Map> products){


            elasticService.batchCreate(name, type,id.toString(),products);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> writeIndex(@RequestParam("indexName") String name, @RequestParam("doc") String type,
                                        @RequestParam("id") String id) throws IOException {

        return new ResponseEntity<>(elasticService.getRequest(name, type,id), HttpStatus.OK);
    }

    @GetMapping("/product")
    public ResponseEntity<?> searchProduct() throws IOException {

        return new ResponseEntity<>(elasticService.search(PRODUCT), HttpStatus.OK);
    }

    @GetMapping("/order")
    public ResponseEntity<?> searchOrder() throws IOException {

        return new ResponseEntity<>(elasticService.search(ORDER), HttpStatus.OK);
    }

}
