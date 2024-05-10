// package com.tmax.waplsearch.util;

// import java.util.List;

// import org.apache.lucene.analysis.Analyzer;
// import org.apache.lucene.analysis.TokenStream;
// import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
// import org.openkoreantext.processor.OpenKoreanTextProcessor;
// import org.openkoreantext.processor.tokenizer.KoreanTokenizer;

// public class OKTAnalyzer extends Analyzer{
//     @Override
//     protected TokenStreamComponents createComponents(String fieldName) {
//         return new TokenStreamComponents(new OKTTokenizer(fieldName));
//     }

//     private static class OKTTokenizer extends TokenStream {
//         private final List<KoreanTokenizer.KoreanToken> tokens;
//         private int currentToken = 0;
//         private final CharTermAttribute charAttr = addAttribute(CharTermAttribute.class);

//         public OKTTokenizer(String text) {
//             tokens = OpenKoreanTextProcessor.tokenize(text);
//         }

//         @Override
//         public boolean incrementToken() {
//             if (currentToken < tokens.size()) {
//                 charAttr.setEmpty().append(tokens.get(currentToken).text());
//                 currentToken++;
//                 return true;
//             }
//             return false;
//         }
//     }
// }

