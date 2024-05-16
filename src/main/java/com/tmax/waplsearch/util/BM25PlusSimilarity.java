package com.tmax.waplsearch.util;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

// BM25의 단점이었던 매우 낮은 빈도의 Term이 무시되는 문제를 개선한 버전
public class BM25PlusSimilarity extends SimilarityBase {
    private final float k1;
    private final float b;
    private final float delta;

    public BM25PlusSimilarity(float k1, float b, float delta) {
        this.k1 = k1;
        this.b = b;
        this.delta = delta;
    }

    @Override
    protected double score(BasicStats stats, double freq, double docLen) {
        double idf = idf(stats);
        double tf = freq + delta; // delta 값이 있으므로 인해 매우 낮은 빈도의 용어에도 가중치 부여
        double normLength = 1 - b + b * (docLen / stats.getAvgFieldLength());
        return idf * ((tf * (k1 + 1)) / (tf + k1 * normLength));
    }
    private float idf(BasicStats stats) {
        long docFreq = stats.getDocFreq();
        long docCount = stats.getNumberOfDocuments();
        return (float) (Math.log(1 + (docCount - docFreq + 0.5D) / (docFreq + 0.5D)));
    }

    @Override
    public String toString() {
        return "BM25Plus(k1=" + k1 + ", b=" + b + ", delta=" + delta + ")";
    }
}