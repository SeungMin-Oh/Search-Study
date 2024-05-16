package com.tmax.waplsearch.service;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.springframework.stereotype.Service;

import com.tmax.waplsearch.util.BM25PlusSimilarity;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.lucene.document.Field;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

@Service
public class LuceneIndexer {
    private IndexWriter indexWriter;
private static final Logger logger = LoggerFactory.getLogger(LuceneSearcher.class);

    @PostConstruct
    public void init() throws IOException {
        Directory directory = FSDirectory.open(Paths.get("./index/"));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setSimilarity(new BM25PlusSimilarity(1.2f, 0.75f, 1.0f)); // BM25Plus 설정
        this.indexWriter = new IndexWriter(directory, config);
    }

    @PreDestroy
    public void cleanup() throws IOException {
        indexWriter.close();
    }

    public void addDocument(String title, String content) throws IOException {
        Document doc = new Document();
        logger.info("title: " + title + ", content: " + content + "\n");
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new TextField("content", content, Field.Store.YES));
        indexWriter.addDocument(doc);
        indexWriter.commit(); 
        indexWriter.flush();
    }

    public void commitIndex() throws IOException {
        indexWriter.commit(); // 대량 처리 후 한번에 커밋
    }
}


