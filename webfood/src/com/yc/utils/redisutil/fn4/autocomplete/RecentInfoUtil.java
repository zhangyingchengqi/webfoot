package com.yc.utils.redisutil.fn4.autocomplete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * 使用自动填充的方式加载信息
 * @author yingzhang
 *
 */
public class RecentInfoUtil {
	public static final String RECENTINFO="recentInfo:";
	
	/** 更新列表信息 
	 * @throws IOException */
	public static void addUpdateInfo( Jedis jedis, String id, String info) throws IOException{
		String key=RECENTINFO+id;
		Pipeline pipeLine=jedis.pipelined();
		pipeLine.multi();
		pipeLine.lrem(key, 1, info);   //如果原来已经存在这个信息，则移除
		pipeLine.lpush(   key,info);   //将新信息添加到列表的头部
		pipeLine.ltrim(key, 0, 99);    //保留列表的头100个
		pipeLine.exec();
		pipeLine.close();
	}
	
	/**
	 * 完成查找自动填充的值
	 * @param jedis
	 * @param id
	 * @param prefix
	 * @return
	 */
	public static List<String> fetchAutoCompleteList( Jedis jedis, String id, String prefix){
		String key=RECENTINFO+id;
		List<String> list=jedis.lrange(key, 0, -1);
		List<String> matches=new ArrayList<String>();
		for( String s:list){
			if( s.toLowerCase().startsWith(prefix)){
				matches.add(s);
			}
		}
		return matches;
	}
	
	
}
