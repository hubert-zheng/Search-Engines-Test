package com.hubert.solrTest;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apdplat.word.lucene.ChineseWordAnalyzer;

public class PaodingAnalysisTest {
	
	public static void main(String[] args) throws IOException, ParseException{
		dissectA("2012");
		
//		/** 创建分词 */  
//        // 词典目录  
//        //String path = "D:\\Java\\paoding_analyzer\\dic";  
//        // 庖丁解牛中文分词器  
//        Analyzer analyzer = new PaodingAnalyzer();  
//        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
//        Path path = FileSystems.getDefault().getPath("/Lucene/paoding-analysis/dic");
//        //词典  
//        Directory luceneDir = FSDirectory.open(path);
//        // 索引书写器  
//        IndexWriter indexWriter = new IndexWriter(luceneDir,indexWriterConfig);
//        // 文档  
//        Document doc = new Document();  
//        // 字段  
//        FieldType pathFieldType = new FieldType();
//        pathFieldType.setIndexOptions(IndexOptions.NONE); 
//        pathFieldType.setStored(true);
//        Field field = new Field("content","你好，世界!",pathFieldType);   // termVector  
//        doc.add(field);             // 文档添加字段  
//        indexWriter.addDocument(doc);    // 索引书写器添加文档  
//        indexWriter.close();  
//        System.out.println("Indexed success!");  
//           
//        /** 搜索关键词 */  
//        // 索引读取器  
//        IndexReader reader = DirectoryReader.open(luceneDir);  
//        // 查询解析器  
//        QueryParser parser = new QueryParser("content", analyzer);  
//        Query query = parser.parse("你好");   // 解析  
//        // 搜索器  
//        IndexSearcher searcher = new IndexSearcher(reader);  
//        // 命中  
//        TopDocs result = searcher.search(query,Integer.MAX_VALUE);  
//        ScoreDoc[] a = result.scoreDocs;
//			for(int i=0;i<a.length;i++){
//				Document documents = reader.document(a[i].doc);
//				System.out.println("doc "+ i + " : " + documents.toString());
//			}
//			
//			//System.out.println(result.);
//			if  (result.totalHits > 0 )  {   
//            System.out.println(" 找到: "  +  result.totalHits  +   "  个结果! " );   
//        }   
//        reader.close();  
		
	}
	
	public final static void dissectA(String word) throws IOException {
		StringBuilder analyzerStr = new StringBuilder();
		analyzerStr.append(word);
		long time = System.currentTimeMillis();
		System.out.println(time);
			//Analyzer analyzer =   new  StandardAnalyzer();   
			//构造一个word分析器ChineseWordAnalyzer
			//如果需要使用特定的分词算法，可通过构造函数来指定：如不指定，默认使用双向最大匹配算法：SegmentationAlgorithm.BidirectionalMaximumMatching
			@SuppressWarnings("resource")
			Analyzer analyzer = new ChineseWordAnalyzer();
			//Analyzer analyzer = new ChineseWordAnalyzer(SegmentationAlgorithm.FullSegmentation);
			//利用word分析器切分文本
			TokenStream tokenStream = analyzer.tokenStream("text", "test");
			//准备消费
			tokenStream.reset();
			//开始消费
			while(tokenStream.incrementToken()){
			    //词
			    CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
			    //词在文本中的起始位置
			    OffsetAttribute offsetAttribute = tokenStream.getAttribute(OffsetAttribute.class);
			    //第几个词
			    PositionIncrementAttribute positionIncrementAttribute = tokenStream.getAttribute(PositionIncrementAttribute.class);

			    System.out.println((charTermAttribute.toString()+" ("+offsetAttribute.startOffset()+" - "+offsetAttribute.endOffset()+") "+positionIncrementAttribute.getPositionIncrement()));
			}
//			//消费完毕
//			tokenStream.close();
//			//利用word分析器建立Lucene索引
//			Directory directory = new RAMDirectory();
//			IndexWriterConfig config = new IndexWriterConfig(analyzer);
//			IndexWriter indexWriter = new IndexWriter(directory, config);
//			Path path = FileSystems.getDefault().getPath("/Lucene/paoding-analysis/dic");
//			Directory luceneDir = FSDirectory.open(path);
//			Document doc = new Document();  
//			FieldType pathFieldType = new FieldType();
//	        pathFieldType.setIndexOptions(IndexOptions.NONE); 
//	        pathFieldType.setStored(true);
//	        Field field = new Field("content","你好，世界!",pathFieldType); 
//		
//			
//			//利用word分析器查询Lucene索引
//			QueryParser queryParser = new QueryParser("text", analyzer);
//			Query query = queryParser.parse("text:杨尚川");
//			TopDocs docs = indexSearcher.search(query, Integer.MAX_VALUE);
		}
}
