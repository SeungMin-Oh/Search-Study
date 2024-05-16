package com.tmax.waplsearch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tmax.waplsearch.service.LuceneIndexer;
import com.tmax.waplsearch.service.LuceneSearcher;


import com.tmax.waplsearch.util.BM25PlusSimilarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final LuceneIndexer indexer;
    private final LuceneSearcher searcher;

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

    @GetMapping
    public List<Map<String, String>>  search(@RequestParam String query) {
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
}

