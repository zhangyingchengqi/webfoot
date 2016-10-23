package com.yc.utils.redisutil.fn4.autocomplete;

import java.io.IOException;
import java.util.List;

import redis.clients.jedis.Jedis;

public class Test {

	public static void main(String[] args) throws IOException {
		Jedis jedis=new Jedis("192.168.20.197",6379);
		RecentInfoUtil.addUpdateInfo(jedis, "1", "中国");
		RecentInfoUtil.addUpdateInfo(jedis, "1", "中国人民政府");
		RecentInfoUtil.addUpdateInfo(jedis, "1", "中国政府");
		RecentInfoUtil.addUpdateInfo(jedis, "1", "中国税务");
		RecentInfoUtil.addUpdateInfo(jedis, "1", "中国工商");
		RecentInfoUtil.addUpdateInfo(jedis, "1", "中国地区");
		
		List<String> list=RecentInfoUtil.fetchAutoCompleteList(jedis, "1", "中国");
		for( String s:list){
			System.out.println( s );
		}
	}

}
