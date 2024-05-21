package com.tmax.waplsearch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.tmax.waplsearch.service.LuceneIndexer;
import com.tmax.waplsearch.service.LuceneSearcher;

import com.tmax.waplsearch.util.BM25PlusSimilarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final LuceneIndexer indexer;
    private final LuceneSearcher searcher;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(LuceneSearcher.class);

    public SearchController(LuceneIndexer indexer, LuceneSearcher searcher) {
        this.indexer = indexer;
        this.searcher = searcher;
    }

    @PostMapping("/index")
    public ResponseEntity<String> index(@RequestParam String title, @RequestParam String content) {
        try {
            indexer.addDocument(title, content);
            return ResponseEntity.ok("Indexed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/bm25")
    public List<Map<String, String>>  searchBM25(@RequestParam String query) {
        try {
            List<Document> documents = searcher.search(query);
            List<Map<String, String>> results = new ArrayList<>();
            for (Document doc : documents) {
                Map<String, String> docMap = new HashMap<>();
                docMap.put("title", doc.get("title"));
                docMap.put("content", doc.get("content"));
                results.add(docMap);
            }
            return results;
        
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/vector")
    public List<Map<String, String>> searchVector(@RequestParam String query) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:5000/embed";
            Map<String, Object> request = new HashMap<>();
            request.put("texts", List.of(query));
            logger.info("Request Map: {}", request);
        
            // Call the Python server to get the search results
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            
            logger.info("Response OK \n");
            // Type safety handling
            List<Map<String, String>> results = new ArrayList<>();
            if (response != null && response.containsKey("results")) {
                // Use ObjectMapper to convert the results to the desired type
                List<List<String>> rawResults = objectMapper.convertValue(response.get("results"), new TypeReference<List<List<String>>>() {});
                for (List<String> resultList : rawResults) {
                    for (String doc : resultList) {
                        Map<String, String> docMap = new HashMap<>();
                        docMap.put("content", doc);
                        results.add(docMap);
                    }
                }
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid response from Python server");
            }
            return results;

        } catch (Exception e) {
            e.printStackTrace(); // 전체 스택 트레이스를 출력하여 디버깅
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

