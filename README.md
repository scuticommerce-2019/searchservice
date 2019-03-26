# searchservice

1) Search Service use redis search module to perform search.
2) Use auto suggestions to give auto complete search.

use docker on local env.
//docker pull redislabs/redisearch

 docker run -p 6379:6379 redislabs/redisearch:latest


To have launchd start kibana now and restart at login:
  brew services start kibana
Or, if you don't want/need a background service you can just run:
  kibana
  
  To have launchd start elasticsearch now and restart at login:
    brew services start elasticsearch
  Or, if you don't want/need a background service you can just run:
    elasticsearch
    
 API doc
 
 https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-index.html   