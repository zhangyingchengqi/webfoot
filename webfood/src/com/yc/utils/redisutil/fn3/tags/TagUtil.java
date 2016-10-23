package com.yc.utils.redisutil.fn3.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

/**
 * 标签系统设计
 * @author yingzhang
 */
public class TagUtil {
	/** 条目的键  */
	public final static String ITEM="item:";
	
	/** 标签的键名 */
	public final static String TAG="tag:";
	
	/** 条目保存有效期 一年  */
	public final static int EXPIRETIME=1*365*24*60;
	
	/**
	 * item 条目的保存
	 */
	public static void saveItem( Jedis jedis, String id, String title , List<String> tags){
		jedis.set(   ITEM+id+":title", title);
		jedis.expire(ITEM+id+":title", EXPIRETIME);
		for(String tag: tags){
			jedis.sadd(  TAG+tag, id);
		}
	}
	
	/**
	 * 取出这组标签 下所有的条目信息
	 * @param jedis
	 * @param tags: 这些tags是逻辑与的关系
	 * @return
	 */
	public static List<Map<String,String>> getMultiAnd(Jedis jedis, List<String> tags){
		String[] ss=new String[tags.size()];
		int i=0;
		for( String s: tags){
			ss[i]=  TAG+s;
			i++;
		}
		Set<String> setids=jedis.sinter(  ss );    //使用  sinter 交集
		return getItemsByIds(jedis, setids);
		
	}

	
	
	
	/**
	 * 取出这组标签 下所有的条目信息
	 * @param jedis
	 * @param tags: 这些tags是逻辑或的关系
	 * @return
	 */
	public static List<Map<String,String>> getMultiOr(Jedis jedis, List<String> tags){
		String[] ss=new String[tags.size()];
		int i=0;
		for( String s: tags){
			ss[i]=  TAG+s;
			i++;
		}
		Set<String> setids=jedis.sunion(  ss );  //  使用  sunion完成即可
		return getItemsByIds(jedis, setids);
	}
	
	/**
	 * 取出这组标签 下所有的条目信息
	 * @param jedis
	 * @param tags: 是tag1但不是后面tag的条目信息
	 * @return
	 */
	public static List<Map<String,String>> getMultiDiff(Jedis jedis, List<String> tags){
		String[] ss=new String[tags.size()];
		int i=0;
		for( String s: tags){
			ss[i]=  TAG+s;
			i++;
		}
		Set<String> setids=jedis.sdiff(  ss );  //  使用  sunion完成即可
		return getItemsByIds(jedis, setids);
	}
	
	private static List<Map<String, String>> getItemsByIds(Jedis jedis, Set<String> setids) {
		int i;
		List<Map<String,String>> listmap=new ArrayList<Map<String,String>>();
		Iterator<String> ite=setids.iterator();
		String[] ids=new String[setids.size()];
		String[] indexes=new String[setids.size()];
		 i=0;
		while(  ite.hasNext() ){
			String id=ite.next();
			ids[i]=ITEM+id+":title";
			indexes[i]=id;
			i++;
		}
		List<String> titles=jedis.mget(   ids    );
		for(  i=0;i<ids.length;i++){
			Map<String ,String> map=new HashMap<String,String>();
			map.put(indexes[i], titles.get(i));
			listmap.add(map);
		}
		return listmap;
	}
	
	
}
