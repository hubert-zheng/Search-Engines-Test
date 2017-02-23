package com.hubert.solrTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServerException;

public class SolrTest {

	public static void main(String[] args) throws IOException, SolrServerException {
		//查询
		Map<String,Object> keyValue = new HashMap<String, Object>();
		keyValue.put("EMP_NAME", "SMITH");
		Map<String,Map<String,Object>> searchMap = new HashMap<String,Map<String,Object>>();
		searchMap.put("q", keyValue);
		Map<String,Object> result = SolrUtils.search(searchMap,0,10);
		Set<String> queryMapsKeySets = result.keySet();
		Iterator<String> iter = queryMapsKeySets.iterator();
		while(iter.hasNext()){
			String iterNext = iter.next();
			System.out.println(iterNext + ":" + result.get(iterNext));
		}
	}
	
	 
}
