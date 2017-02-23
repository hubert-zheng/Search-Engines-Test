package com.hubert.LuceneTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * 建立索引
 * @author Hubrt
 *
 */
public class TextFileIndexer {
	public static void main( String[] args ) throws IOException{
		//创建索引
		//createIndex();
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("隔行");
		//查询
		queryTest("2012");
        
	}
	
	
	/**
	 * 创建索引
	 * @return
	 * @throws IOException
	 */
	public static long createIndex() throws IOException{
		/**/ /*  指明要索引文件夹的位置,这里是D盘的Lucene文件夹下 */   
        File fileDir =   new  File( "/Lucene" );
        if(!fileDir.exists()){
        	if(fileDir.mkdir()){
				System.out.println("创建文件夹exportFile成功！");
			}else{
				System.out.println("创建文件夹exportFile失败！");
			}
        }
        
        /**/ /*  这里放索引文件的位置  */   
        File indexDir =   new  File( "/Lucene/Index" );   
        if(!indexDir.exists()){
        	if(indexDir.mkdir()){
				System.out.println("创建文件夹exportFile成功！");
			}else{
				System.out.println("创建文件夹exportFile失败！");
			}
        }
        Path path = FileSystems.getDefault().getPath("/Lucene/Index");
        System.out.println("The Path :" + path);
        //词典  
        Directory luceneDir = FSDirectory.open(path);
        System.out.println("The luceneDir :" + luceneDir);
        
        Analyzer luceneAnalyzer = new StandardAnalyzer();
        //弃用
        //IndexWriter indexWriter = new IndexWriter(indexDir, luceneAnalyzer,true);
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(luceneAnalyzer);
        //写入笔 
        IndexWriter indexWriter = new IndexWriter(luceneDir,indexWriterConfig);
        
        File[] textFiles = fileDir.listFiles();
        
        System.out.println("File length:" + textFiles.length);
        
        long startTime = new Date().getTime(); 
        
        //增加document到索引去    
        for(int i= 0 ; i<textFiles.length; i++)  {
        	 if  (textFiles[i].isFile()   &&  textFiles[i].getName().endsWith( ".txt" ))  {   
                 System.out.println(" File  "   + textFiles[i].getCanonicalPath() + " 正在被索引. " );   
                 String temp =  FileReaderAll(textFiles[i].getCanonicalPath(), "UTF-8" );   
                 System.out.println(temp);   
                 Document document =   new  Document();
                 // “path”是构造的Field的名字，通过该名字可以找到该Field
                 // Field.Store.YES表示存储该Field；Field.Index.UN_TOKENIZED表示不对该Field进行分词，但是对其进行索引，以便检索
                 FieldType pathFieldType = new FieldType();
                 //NONE 未编入索引
                 //DOCS  只有文档被索引：术语频率和位置被省略。对字段的短语和其他位置查询将抛出异常，并且评分将表现为文档中的任何术语仅出现一次。
                 //DOCS_AND_FREQS  只有文档和术语频率被编入索引：位置被省略。这使得正常评分，除了短语和其他位置查询将抛出异常。
                 //DOCS_AND_FREQS_AND_POSITIONS  索引文档，频率和位置。这是全文搜索的典型默认值：启用全评分，并支持位置查询。
                 //DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS   索引文档，频率，位置和偏移量。字符偏移编码在位置旁边。
                 pathFieldType.setIndexOptions(IndexOptions.NONE); 
                 pathFieldType.setStored(true);
                 Field FieldPath =   new  Field( "path", textFiles[i].getPath(),pathFieldType);   
                 FieldType bodyFieldType = new FieldType();
                 bodyFieldType.setStored(true);
                 bodyFieldType.setIndexOptions(IndexOptions.DOCS);
                 //bodyFieldType.setStoreTermVectorOffsets(true);
                 Field FieldBody =   new  Field( "body", temp, bodyFieldType);   
                 document.add(FieldPath);   
                 document.add(FieldBody);   
                 indexWriter.addDocument(document);   
             }   
        }
        
        //optimize()方法是对索引进行优化    
        //indexWriter.;   
        indexWriter.close();   
        
        
        // 测试一下索引的时间    
        long  endTime  =   new  Date().getTime();   
        System.out.println("这花费了" + (endTime  -  startTime) + "毫秒来把文档增加到索引里面去! " + fileDir.getPath());
        
		return endTime - startTime;
	}
	
	public static String FileReaderAll(String FileName, String charset)   
            throws  IOException  {   
        BufferedReader reader =   new  BufferedReader(new InputStreamReader(new  FileInputStream(FileName), charset));   
        String line =   new  String();   
        String temp =   new  String();   
           
        while  ((line  =  reader.readLine())  !=   null)  {   
            temp +=  line;   
        }   
        reader.close();   
        return  temp;   
    } 
	
	/**
	 * 从索引里查询
	 * @param queryString   关键词
	 * @throws IOException	
	 */
	public static void queryTest(String queryString) throws IOException{
		//Hits hits =   null ;  //自带高亮
 		Query query =   null ;  
 		TopDocs result = null;
 		//索引源
 		Path path = FileSystems.getDefault().getPath("/Lucene/Index");
        System.out.println("The Path :" + path);
        //词典  
        Directory luceneDir = FSDirectory.open(path);
        //读出
 		IndexReader indexReader = DirectoryReader.open(luceneDir);
 		//搜索
 		IndexSearcher searcher = new IndexSearcher(indexReader);
 		//分析
 		Analyzer analyzer =   new  StandardAnalyzer();   
 		
 		QueryParser qp =   new  QueryParser("body", analyzer);  
 		
 		Document documents = null;
 		try {
			query =  qp.parse(queryString);
		} catch (ParseException e) {
			System.out.println("查询索引异常！");
			e.printStackTrace();
		}
 		
 		if  (searcher != null)  {   
 			result = searcher.search(query,Integer.MAX_VALUE);
 			ScoreDoc[] a = result.scoreDocs;
 			for(int i=0;i<a.length;i++){
 				documents = indexReader.document(a[i].doc);
 				System.out.println("doc "+ i + " : " + documents.toString());
 			}
 			
 			//System.out.println(result.);
 			if  (result.totalHits > 0 )  {   
                System.out.println(" 找到: "  +  result.totalHits  +   "  个结果! " );   
            }   
 		}
	} 
	
	
	
}
