package com.scuticommerce.search.service;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ElasticService {

    /**
     * call client.close(); when done
     * @return
     */
    public RestHighLevelClient getClient(){

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));

        return client;

    }

    public IndexRequest createJsonRequest(String index, String type, String id, String payload) {

        IndexRequest request = new IndexRequest(index, type, id);

        return request.source(payload, XContentType.JSON);

    }

    public IndexRequest createMapRequest(String index, String type, String id, Map<String, Object> jsonMap){

        IndexRequest indexRequest = new IndexRequest(index, type, id).source(jsonMap);

        return indexRequest;
    }

    public String batchCreate(String index, String type, String id, List<Map> products) {

        BulkRequest request = new BulkRequest();

        for (Map product : products) {

            request.add(createMapRequest( index,  type,  product.get(id).toString(),  product));
        }

        try {

            BulkResponse bulkResponse = getClient().bulk(request, RequestOptions.DEFAULT);

            System.out.println(bulkResponse.toString());

            if (bulkResponse.hasFailures()) {

                System.out.println("Found failures ");
            }

            //close connection
            getClient().close();

            return bulkResponse.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "OK";
    }

    public String writeDocument(String index, String type, String id, String payload){

        IndexRequest request = createJsonRequest( index,  type,  id,  payload);

        try {

            IndexResponse indexResponse = getClient().index(request, RequestOptions.DEFAULT);

            System.out.println(indexResponse.toString());

            return indexResponse.getResult().toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "OK";

    }

    public GetResponse getRequest(String index, String type, String id) throws IOException {

        GetRequest getRequest = new GetRequest(index,type,id);

        GetResponse response = getClient().get(getRequest, RequestOptions.DEFAULT);

        return response;
    }

    public GetResponse getAysncRequest(String index, String type, String id) throws IOException {

        GetRequest request = new GetRequest(index,type,id);

        // TODO: 2019-03-09 check aysnc
        GetResponse response = null;//getClient().getAsync(request, RequestOptions.DEFAULT, listner);

        return response;
    }

    ActionListener<Boolean> listener = new ActionListener<Boolean>() {
        @Override
        public void onResponse(Boolean exists) {

        }

        @Override
        public void onFailure(Exception e) {

        }
    };


    public DeleteResponse deleteRequest(String index, String type, String id) throws IOException {

        DeleteRequest deleteRequest = new DeleteRequest(index,type,id);

        DeleteResponse response = getClient().delete(deleteRequest, RequestOptions.DEFAULT);

        return response;
    }

    public UpdateResponse updateRequest(String index, String type, String id) throws IOException {

        UpdateRequest request = new UpdateRequest(index,type,id);

        UpdateResponse response = getClient().update(request, RequestOptions.DEFAULT);

        return response;
    }

    public SearchResponse search(String type) throws IOException {

        //pass index in constructor or it will search all indexes
        SearchRequest searchRequest = new SearchRequest(type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = getClient().search(searchRequest, RequestOptions.DEFAULT);

        return response;

    }

    private void debugResponse(IndexResponse indexResponse){

        String index = indexResponse.getIndex();
        String type = indexResponse.getType();
        String id = indexResponse.getId();
        long version = indexResponse.getVersion();
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {

        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {

        }
        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {

        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure :
                    shardInfo.getFailures()) {
                String reason = failure.reason();
            }
        }
    }


}
