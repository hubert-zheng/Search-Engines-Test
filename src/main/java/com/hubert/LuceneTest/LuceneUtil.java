//package com.hubert.LuceneTest;
//
//import java.io.File;
//
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.index.LogByteSizeMergePolicy;
//import org.apache.lucene.index.LogMergePolicy;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.util.Version;
//
///**
// * Lucene工具类
// * @author Hubrt
// *
// */
//public class LuceneUtil {
//	
//	public static Integer totalNum=0;
//	
//	class LuceneConfig{
//		static final String INDEX_PATH = "/Lucene/Index";
//	}
//	
//	/**
//	 * 创建索引
//	 * @param data 要放入索引的一条记录
//	 * @return
//	 */
//	public static synchronized boolean createIndex(LuceneData data) {
//		IndexWriter indexWriter = null;
//		Directory d = null;
//		try {
//			d = FSDirectory.open(new File(LuceneConfig.INDEX_PATH));
//			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_6_4_1,
//					AnalyzerUtil.getIkAnalyzer());
//			// 3.6以后不推荐用optimize,使用LogMergePolicy优化策略
//			conf.setMergePolicy(optimizeIndex());
//			// 创建索引模式：CREATE，覆盖模式； APPEND，追加模式
//			File file = new File(LuceneConfig.INDEX_PATH);
//			File[] f = file .listFiles();
//			if(f.length==0)    
//				conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
//			else
//				conf.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
//
//			indexWriter = new IndexWriter(d, conf);
//			//因为id是唯一的，如果之前存在就先删除原来的，在创建新的
//			Term term = new Term("id", data.getId());
//			indexWriter.deleteDocuments(term);
//			
//			Document doc = getDocument(data);
//			indexWriter.addDocument(doc);
//
//			logger.debug("索引结束,共有索引{}个" + indexWriter.numDocs());
//			//System.out.println("索引结束,共有索引{}个" + indexWriter.numDocs()+":"+doc.get("id")+":"+doc.get("author"));
//			// 自动优化合并索引文件,3.6以后不推荐用optimize,使用LogMergePolicy优化策略
//			// indexWriter.optimize();
//			indexWriter.commit();
//			return true;
//		} catch (CorruptIndexException e) {
//			e.printStackTrace();
//			logger.error("索引添加异常", e);
//		} catch (LockObtainFailedException e) {
//			e.printStackTrace();
//			logger.error("索引添加异常", e);
//		} catch (IOException e) {
//			e.printStackTrace();
//			logger.error("索引不存在", e);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("索引添加异常", e);
//		} finally {
//			if (indexWriter != null) {
//				try {
//					indexWriter.close();
//				} catch (CorruptIndexException e) {
//					e.printStackTrace();
//					logger.error("索引关闭异常", e);
//				} catch (IOException e) {
//					e.printStackTrace();
//					logger.error("索引关闭异常", e);
//				} finally {
//					try {
//						if (d != null && IndexWriter.isLocked(d)) {
//							IndexWriter.unlock(d);
//						}
//					} catch (IOException e) {
//						e.printStackTrace();
//						logger.error("解锁异常", e);
//					}
//				}
//			}
//		}
//		return false;
//	}
//	
//	
//	/**
//	 * 优化索引，返回优化策略
//	 * 
//	 * @return
//	 */
//	private static LogMergePolicy optimizeIndex() {
//		LogMergePolicy mergePolicy = new LogByteSizeMergePolicy();
//
//		// 设置segment添加文档(Document)时的合并频率
//		// 值较小,建立索引的速度就较慢
//		// 值较大,建立索引的速度就较快,>10适合批量建立索引
//		// 达到50个文件时就和合并
//		mergePolicy.setMergeFactor(50);
//
//		// 设置segment最大合并文档(Document)数
//		// 值较小有利于追加索引的速度
//		// 值较大,适合批量建立索引和更快的搜索
//		mergePolicy.setMaxMergeDocs(5000);
//
//		// 启用复合式索引文件格式,合并多个segment
//		//mergePolicy.setUseCompoundFile(true);
//		return mergePolicy;
//	}
//}
//
//
//
///**
// * 分词器工具，设定分词器
// * @author Administrator
// *
// */
//class AnalyzerUtil {
//	private static Analyzer analyzer;
//
//	public static Analyzer getIkAnalyzer() {
//		if (analyzer == null) {
//			// 当为true时，分词器迚行最大词长切分 ；当为false时，分词器迚行最细粒度切
//			analyzer = new IKAnalyzer(true);
//		}
//		return analyzer;
//	}
//}
