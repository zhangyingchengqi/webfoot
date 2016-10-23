package com.yc.utils.redisutil.fn3.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class Test {

	public static void main(String[] args) {
		Jedis jedis=new Jedis("192.168.20.197",6379);

		List<String> tags = new ArrayList<String>();
		tags.add("php");
		tags.add("programming");

		TagUtil.saveItem(jedis, "1", "a", tags);

		TagUtil.saveItem(jedis, "2", "b", tags);

		tags.add("java");
		TagUtil.saveItem(jedis, "3", "c", tags);

		// 获取php书
		List<String> tags1 = new ArrayList<String>();
		tags1.add("php");
		List<Map<String, String>> list = TagUtil.getMultiAnd(jedis, tags1);
		System.out.println("输出php书");
		for (Map<String, String> map : list) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " " + entry.getValue());
			}
		}
		// 获取即是php, 又是java的书
		tags1.add("java");
		list = TagUtil.getMultiAnd(jedis, tags1);
		System.out.println("输出即是php, 又是java的书");
		for (Map<String, String> map : list) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " " + entry.getValue());
			}
		}

		// 获取是php或是java的书
		tags1.add("java");
		list = TagUtil.getMultiOr(jedis, tags1);
		System.out.println("输出是php或是java的书");
		for (Map<String, String> map : list) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " " + entry.getValue());
			}
		}

		// 获取是php但不能是java的书
		tags1.add("java");
		list = TagUtil.getMultiDiff(jedis, tags1);
		System.out.println("输出是php但不能是java的书");
		for (Map<String, String> map : list) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " " + entry.getValue());
			}
		}

	}

}
