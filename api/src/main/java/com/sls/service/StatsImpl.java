package com.sls.service;

import com.sls.exception.InternalServerErrorException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class StatsImpl implements Stats {

    /*
        {
            "size": 0,
            "query": {
                "bool": {
                    "filter": {
                        "range": {
                            "@timestamp": {
                                "gte": "now-3000s",
                                "lt": "now"
                            }
                        }
                    }
                }
            },
            "aggs": {
                "info_logs": {
                    "filter": {
                        "term": {
                            "log_level_type": 0

                        }
                    }
                },
                "warning_logs": {
                    "filter": {
                        "term": {
                            "log_level_type": 1
                        }
                    }
                },
                "error_logs": {
                    "filter": {
                        "term": {
                            "log_level_type": 2
                        }
                    }
                }
            }
        }
    */
    @Override
    public Map<String, Integer> getLogCounts(int duration) throws InternalServerErrorException {

        String requestJson =
                "{\"size\":0,\"query\":{\"bool\":{\"filter\":{\"range\":{\"@timestamp\":{\"gte\":\"now-" +
                duration +
                "s\",\"lt\":\"now\"}}}}},\"aggs\":{\"info_logs\":{\"filter\":{\"term\":{\"log_level_type\":0}}},\"warning_logs\":{\"filter\":{\"term\":{\"log_level_type\":1}}},\"error_logs\":{\"filter\":{\"term\":{\"log_level_type\":2}}}}}";

        // request url
        // !!! we cannot access this service via localhost.
        // containers inside "docker compose" communicating via container names:
        String url = "http://elasticsearch:9200/lc_logs/_search";

        // create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // build the request
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        // send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // check response
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject json = new JSONObject(response.getBody());

            HashMap<String, Integer> results = new HashMap<>();

            results.put("total", json.getJSONObject("hits").getJSONObject("total").getInt("value"));

            JSONObject queryResults = json.getJSONObject("aggregations");

            results.put("info", queryResults.getJSONObject("info_logs").getInt("doc_count"));
            results.put("warning", queryResults.getJSONObject("warning_logs").getInt("doc_count"));
            results.put("error", queryResults.getJSONObject("error_logs").getInt("doc_count"));

            return results;
        }
        else {
            throw new InternalServerErrorException(response.getBody());
        }
    }
}