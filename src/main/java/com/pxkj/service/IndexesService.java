package com.pxkj.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IndexesService {

    public void createIndex(String content, Integer indexId, String title) {
        try {
            Directory directory = FSDirectory.open(Paths.get("E:\\luceneIndex\\lucene7"));
            Analyzer analyzer = new SmartChineseAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
            Document document = new Document();
            document.add(new IntPoint("id", indexId)); // id
            document.add(new StringField("title", title, Field.Store.YES)); // 标题
            document.add(new TextField("content", content, Field.Store.YES)); // 内容
            document.add(new StoredField("id", indexId)); // id排序
            indexWriter.addDocument(document);
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, String>> search(String text) {
        List<Map<String, String>> result = new ArrayList<>();
        try {
            Directory directory = FSDirectory.open(Paths.get("E:\\luceneIndex\\lucene7"));
            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            Analyzer analyzer = new SmartChineseAnalyzer();
            QueryParser queryParser = new QueryParser("content", analyzer);
            Query query = queryParser.parse(text);
            TopDocs topDocs = indexSearcher.search(query, indexReader.maxDoc());//在所有的索引中搜索
            QueryScorer scorer = new QueryScorer(query);
            Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");//设置查询结果红色字体加粗(可自行修改默认为粗体)
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
            highlighter.setTextFragmenter(fragmenter);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Map<String, String> map = new HashMap<>();
                Document doc = indexSearcher.doc(scoreDoc.doc);
                String content = doc.get("content");
                if (content != null) {
                    TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
                    String searchContent = highlighter.getBestFragment(tokenStream, content);
                    System.out.println(scoreDoc.score);
                    map.put("html", searchContent == null ? "" : searchContent);
                    map.put("score", String.valueOf(scoreDoc.score));
                    result.add(map);
                }
            }
            indexReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
