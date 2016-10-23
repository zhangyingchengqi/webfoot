package com.yc.utils.redisutil.fn2.rank;

import java.util.Set;

import redis.clients.jedis.Jedis;

/**
 * 排行榜操作
 * @author yingzhang
 *
 */
public class RankUtil {
	private static final String RANKBOARD="rankBoard";
		
	/**
	 * 更新得分
	 * @param Jedis
	 * @param key
	 * @param score 权重值，用于排序
	 * @param user   用于存在的用户名或是id
	 */
	public static long updateScore( Jedis jedis, String key, double score, String user){
		if( key==null){
			key=RANKBOARD;
		}
		return jedis.zadd(key, score, user);
	}
	
	public static long updateScore( Jedis jedis,  double score, String user){
		return updateScore(jedis,null, score, user);
	}
	
	/**
	 * 获取top n个数据
	 * @param jedis
	 * @param key
	 * @param topn
	 * @return
	 */
	public static Set<String> getTopN(  Jedis jedis, String key, int topn){
		if( key==null){
			key=RANKBOARD;
		}
		return jedis.zrevrange(key, 0, topn-1);
	}
	
	public static Set<String> getTopN(  Jedis jedis, int topn){
		return getTopN( jedis,null,topn);
	}
	
	/**
	 * 获取某个用户的排名
	 * @param jedis
	 * @param key
	 * @param user
	 * @return
	 */
	public static long getRank( Jedis jedis, String key,String user){
		if( key==null){
			key=RANKBOARD;
		}
		return jedis.zrevrank(key, user)+1;
	}
	
	public static long getRank( Jedis jedis, String user){
		return getRank(jedis, null,user);
	}
}
