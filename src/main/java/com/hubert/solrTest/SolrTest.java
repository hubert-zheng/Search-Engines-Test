package com.hubert.solrTest;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;

public class SolrTest {
	
	private final static Log log = LogFactory.getLog(SolrTest.class);
	
	public static void main(String[] args) throws IOException, SolrServerException {
//		//word 分词
//		List<Word> words = WordSegmenter.segWithStopWords("hubert test");
//		System.out.println(words);
//		System.out.println("分词结果：");
//		//mmseg
//		File file = new File("/Lucene/mmseg4j");
//		Dictionary dic = Dictionary.getInstance(file);
//		String txt = "哦， 那个是我本机上测的， 没打包到jar里。 ";
//		Seg seg = new ComplexSeg(dic); 
//		MMSeg mmSeg = new MMSeg(new StringReader(txt), seg); 
//		com.chenlb.mmseg4j.Word word = null;
//		while((word = mmSeg.next())!=null) {
//				if(word != null) { 
//					System.out.print(word + "|"); 
//			} 
//		}
		//查询
		List<Map<String,Object>> queryResultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> keyValue = new HashMap<String, Object>();
		//分词
		File file = new File("/Lucene/mmseg4j");
		Dictionary dic = Dictionary.getInstance(file);
		String txt = "SMITH WHERE IS HERE?";
		Seg seg = new ComplexSeg(dic); 
		MMSeg mmSeg = new MMSeg(new StringReader(txt), seg); 
		com.chenlb.mmseg4j.Word word = null;
		while((word = mmSeg.next())!=null) {
			String keyWord = word.getString().toLowerCase();
			//字母小写
			if(isLowCase(keyWord)){
					log.debug("分词：" + keyWord);
					keyValue.put("EMP_NAME",keyWord);
					//keyValue.put("EMP_NAME", "SMITH");
					//keyValue.put("EMP_JOB", "CLERK");
					Map<String,List<Map<String,Object>>> searchMap = new HashMap<String,List<Map<String,Object>>>();
					List<Map<String,Object>> searchMapList = new ArrayList<Map<String,Object>>();
					searchMapList.add(keyValue);
					searchMap.put("q", searchMapList);   // q,EMP_NAME:SMITH
					Map<String,Object> result = SolrUtils.search(searchMap,0,10);
					queryResultList.add(result);
			}
			//字母大写
			keyWord = word.getString().toUpperCase();
			if(isUpperCase(keyWord)){
				log.debug("分词：" + keyWord);
				keyValue.put("EMP_NAME",keyWord);
				//keyValue.put("EMP_NAME", "SMITH");
				//keyValue.put("EMP_JOB", "CLERK");
				Map<String,List<Map<String,Object>>> searchMap = new HashMap<String,List<Map<String,Object>>>();
				List<Map<String,Object>> searchMapList = new ArrayList<Map<String,Object>>();
				searchMapList.add(keyValue);
				searchMap.put("q", searchMapList);   // q,EMP_NAME:SMITH
				Map<String,Object> result = SolrUtils.search(searchMap,0,10);
				queryResultList.add(result);
			}
		}
		//输出结果集
		for(Map<String,Object> queryResult : queryResultList){
			Set<String> queryMapsKeySets = queryResult.keySet();
			Iterator<String> iter = queryMapsKeySets.iterator();
			while(iter.hasNext()){
				String iterNext = iter.next();
				System.out.println(iterNext + ":" + queryResult.get(iterNext));
			}
		}
	}
	
	/**
	 * 判断是否小写
	 * @param str
	 * @return
	 */
	private static boolean isLowCase(String str){
		return str.equals(str.toLowerCase());
	}
	/**
	 * 判断是否小写
	 * @param str
	 * @return
	 */
	private static boolean isUpperCase(String str){
		return str.equals(str.toUpperCase());
	}
	 
}
