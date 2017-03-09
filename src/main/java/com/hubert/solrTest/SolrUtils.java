package com.hubert.solrTest;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

/**
 * Solr工具类
 * 
 * @author Hubrt
 *
 */
public class SolrUtils {

	private static String serverUrl = "http://127.0.0.1:8080/solr/Core1";

	/**
	 * 增加与修改<br>
	 * 增加与修改其实是一回事，只要id不存在，则增加，如果id存在，则是修改
	 * 
	 * @param fieldMap
	 *            Field键值对
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public static void upadteIndex(Map<String, Object> fieldMap) throws SolrServerException, IOException {
		// 创建
		HttpSolrClient client = new HttpSolrClient(serverUrl);
		SolrInputDocument doc = new SolrInputDocument();
		// 获取键集合
		Set<String> set = fieldMap.keySet();
		// 遍历
		Iterator<String> iter = set.iterator();
		while (iter.hasNext()) {
			doc.addField((String) iter.next(), fieldMap.get(iter.next()));
		}
		// doc.addField("id", "zxj1");
		// doc.addField("EMP_NAME", "SMITH");
		// doc.addField("EMP_JOB", "CLERK");
		// doc.addField("EMP_MGR", "书籍");
		// doc.addField("EMP_HIERDATE", "11");
		// doc.addField("EMP_SAL", "这是一本好书");
		// doc.addField("EMP_DEPTNO", "图片地址");

		client.add(doc);
		client.commit();

		client.close();
	}

	/**
	 * 删除索引
	 * 
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public static void deleteIndex(List<String> ids) throws SolrServerException, IOException {
		HttpSolrClient client = new HttpSolrClient(serverUrl);
		// //1.删除一个
		// client.deleteById("zxj1");

		// //2.删除多个
		// ids.add("1");
		// ids.add("2");
		client.deleteById(ids);

		// //3.根据查询条件删除数据,这里的条件只能有一个，不能以逗号相隔
		// client.deleteByQuery("id:zxj1");
		//
		// //4.删除全部，删除不可恢复
		// client.deleteByQuery("*:*");

		// 一定要记得提交，否则不起作用
		client.commit();
		client.close();
	}

	/**
	 * 删除指定条件
	 * 
	 * @param key
	 * @param value
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public static void deleteIndex(String key, String value) throws SolrServerException, IOException {
		HttpSolrClient client = new HttpSolrClient(serverUrl);
		String queryMap = key + ":" + value;
		client.deleteByQuery(queryMap);
		client.commit();
		client.close();
	}

	/**
	 * 删除所有索引
	 * 
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public static void deleteIndexAll() throws SolrServerException, IOException {
		HttpSolrClient client = new HttpSolrClient(serverUrl);
		client.deleteByQuery("*:*");
		client.commit();
		client.close();
	}

	/**
	 * 搜索
	 * 
	 * @param searchMap
	 *            Map<query,<key,value>> query-查询条件，key,value键值对
	 * @param start
	 * @param rows
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public static Map<String,Object> search(Map<String,List<Map<String,Object>>> searchMap, int start, int rows)
			throws IOException, SolrServerException {
		HttpSolrClient client = new HttpSolrClient(serverUrl);
		// 创建查询对象
		SolrQuery query = new SolrQuery();
		Set<String> queryKeySets = searchMap.keySet();
		// 遍历查询条件
		for (String queryKey : queryKeySets) {
			// 获取键值对集合
			List<Map<String, Object>> queryMapsList = searchMap.get(queryKey);
			for(Map<String,Object> queryMaps:queryMapsList){
				Set<String> queryMapsKeySets = queryMaps.keySet();
				Iterator<String> iter = queryMapsKeySets.iterator();
				String queryMapString = "";
				while (iter.hasNext()) {
					String iterNext = iter.next();
					queryMapString = iterNext + ":" + queryMaps.get(iterNext);
				}
				// 设置查询条件 和 键值对
				query.set(queryKey, queryMapString);
			}
		}
		// q 查询字符串，如果查询所有*:*     
//		query.set("q", "EMP_JOB:SMITH");
//		query.set("q", "EMP_NAME:SMITH");
//		query.set("q", "EMP_NAME:CLERK");  //多关键词    不能重复EMP_NAME
		
		// fq 过滤条件，过滤是基于查询结果中的过滤
		// query.set("fq", "EMP_JOB:CLERK");
		// sort 排序，请注意，如果一个字段没有被索引，那么它是无法排序的
		// query.set("sort", "product_price desc");
		// start row 分页信息，与mysql的limit的两个参数一致效果
		query.setStart(start);
		query.setRows(rows);
		// fl 查询哪些结果出来，不写的话，就查询全部，所以我这里就不写了
		// query.set("fl", "");
		// df 默认搜索的域
		query.set("df", "product_keywords");
		
		// ======高亮设置===
		// 开启高亮
		query.setHighlight(true);
		// 高亮域
		query.addHighlightField("EMP_NAME");
		// 前缀
		query.setHighlightSimplePre("<span style='color:red'>");
		// 后缀
		query.setHighlightSimplePost("</span>");

		// 执行搜索
		QueryResponse queryResponse = client.query(query);
		// 搜索结果
		SolrDocumentList results = queryResponse.getResults();
		// 查询出来的数量
		long numFound = results.getNumFound();
		System.out.println("总查询出:" + numFound + "条记录");

		// 遍历搜索记录
		// 获取高亮信息
		//Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		Map<String,Object> searchResult = new HashMap<String,Object>();
		for (SolrDocument solrDocument : results) {
			Collection<String> solrNamesCol = solrDocument.getFieldNames();
			for (String solrName : solrNamesCol) {
				//Map -- key : value
				searchResult.put(solrName, solrDocument.get(solrName));
				//System.out.println(solrName + ":" + solrDocument.get(solrName));
			}
			// System.out.println("id" + solrDocument.get("id"));
			// System.out.println("名称 :" + solrDocument.get("EMP_NAME"));
			// System.out.println("分类:" + solrDocument.get("EMP_JOB"));
			// System.out.println("分类名称:" + solrDocument.get("EMP_MGR"));
			// System.out.println("价格:" + solrDocument.get("EMP_HIERDATE"));
			// System.out.println("描述:" + solrDocument.get("EMP_SAL"));
			// System.out.println("图片:" + solrDocument.get("EMP_COMM"));

//			// 输出高亮
//			Map<String, List<String>> map = highlighting.get(solrDocument.get("id"));
//			List<String> list = map.get("EMP_NAME");
//			if (list != null && list.size() > 0) {
//				System.out.println(list.get(0));
//			}
		}

		client.close();
		return searchResult;
	}

}
