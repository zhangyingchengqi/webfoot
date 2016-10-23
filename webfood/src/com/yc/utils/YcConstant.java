package com.yc.utils;

/**
 * 这个类专门用于定义系统中所使用的字符串常 量
 * @author Administrator
 *
 */
public class YcConstant {
	/** redis中存所有的菜的键      值的类型:   string, list, set sorted set, hash, 为了实现排序的效果，使用sorted set来存*/
	public final static String ALLFOOD="allfood";
	
	/**  redis的联接地址  */
	public final static String REDIS_URL="localhost";
	/** redis联接端口  */
	public final static int REDIS_PORT=6379;
	
	/**购物车在session中的键名  */
	public final static String CART_NAME="cart";
}
