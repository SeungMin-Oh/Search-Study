package com.tmax.waplsearch.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Service;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LuceneSearcher {
    private final IndexReader indexReader;
    private final IndexSearcher indexSearcher;
    private static final Logger logger = LoggerFactory.getLogger(LuceneSearcher.class);


    public LuceneSearcher(Directory directory) throws IOException {
        this.indexReader = DirectoryReader.open(directory);
        this.indexSearcher = new IndexSearcher(indexReader);
    }

    public List<Document> search(String queryStr) throws Exception {
        logger.info("query: " + queryStr+"\n");
        Query query = new QueryParser("content", new StandardAnalyzer()).parse(queryStr);
        TopDocs results = indexSearcher.search(query, 10);
        List<Document> docs = new ArrayList<>();
        for (ScoreDoc sd : results.scoreDocs) {
            docs.add(indexSearcher.doc(sd.doc));
            Document doc = indexSearcher.doc(sd.doc);
            
            String title = doc.get("title"); // 문서에서 'title' 필드 가져오기
            String content = doc.get("content"); // 문서에서 'content' 필드 가져오기
            logger.info("Document ID: " + sd.doc + ", Title: " + title + ", Content: " + content);
        }
        return docs;
    }
}

