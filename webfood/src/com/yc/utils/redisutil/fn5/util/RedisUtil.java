package com.yc.utils.redisutil.fn5.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.yc.bean.Resfood;
import com.yc.utils.YcConstant;

import redis.clients.jedis.Jedis;

public class RedisUtil<T> {

	public void saveToHash(Jedis jedis, String keyPrefix, String id, List<T> list, Class<T> c)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(   jedis==null||jedis.isConnected()==false){
			return;
		}
		// TODO:     结果：   allfood:1    allfood:2  ->  1,2  编号   ->得到编号 
		//         ->编号从   List<T>   中的t中去取   ->   要调用这个   t 的   getFid的方法
		//          所以要根据    id  在前面加上  get  ->   形成　　　　getFid这个方法
		String getIdMethodName = "get" + id.substring(0, 1).toUpperCase() + id.substring(1);   // getFid
		// 取出所有get方法
		Set<Method> methodGet = getMethod(c);    //从反射实例中取出所有   get方法.
		for (T rf : list) {
			// 取出id
			String itemid = keyPrefix + ":";   //    allfood:1    allfood:2    allfood:3
			//取   getFid()方法值  ->  为取编号做准备
			for (Method m : methodGet) {
				if (m.getName().equals(getIdMethodName)) {
					itemid = itemid + m.invoke(rf).toString();    // t.getFid()  ->  得到了   id      ->   itemid=> allfood:   +id   =>   allfood:1    allfood:2
					break;
				}
			}
			for (Method m : methodGet) {
				//  m    name   ->   getFid()      getFname   ->    fid     fname
				//   methodGet中存的是　　　　getＦｎａｍｅ（）　　　　ｇｅｔＰｒｉｃｅ（）  ->去掉get,将首字母小写
				//而存的时候   redis中的hash的键
				//      fname    
				//      price  
				String fieldName = m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4);
				//   jedis.hset(   ,   "fid","fname"
				//   jedis.haset(   "allfood:1",    fname, "xx");
				//   jedis.hset( "allfood:1", "price", "22");           getNum()  ->    null
				Object value=m.invoke(rf);
				if(   value!=null ){
					jedis.hset(itemid, fieldName, value.toString());    //  (  itmeid, num, null.toString()  );
				}
			}
		}
	}
	
	public List<T> getFromHash(Jedis jedis, String keyPrefix, String id,  Class<T> c)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		List<T> list=new ArrayList<T>();
		//先取出所有的键
		String keysPattern=keyPrefix;   //   "allfood:*"
		Set<String> keyset=jedis.keys(keysPattern);   //查出所有的"allfood:*"键
		Iterator<String> its=keyset.iterator();
		T t=null;
		while(  its.hasNext()   ){
			String key=its.next();    //取出每个键     -> "allfood:1"   "allfood:2"
			Map<String,String> map=jedis.hgetAll(key);  //根据键取出  hash   ->   Map      
																//        fname    xxx
																//        price    22
			t=parseMapToT(  map,  c);
			list.add(t);
		}
		
		return list;
		
	}
	
	protected T parseMapToT(Map<String,String> map, Class<T> c) {
		// 1. 获取所有的set方法名
		Set<Method> setMethod = getSetMethod(c);         // setPname,
																// setPrice
																// setIns
		T t = null;
		try {
			t = c.newInstance();
			// 循环所有的set方法，查到 与ｍｅｔｈｏｄＮａｍｅｓ中相同的方法
			for (Method method : setMethod) {
				//循环map中所有的键，拼接上  set后与  method.getName相等比较 
				Set<String> keys=map.keySet();   //  fname ,  price,   
				for (String key : keys) { // setResadmin
					//拼装键名
					String methodName= "set"+ key.substring(0,1).toUpperCase()+ key.substring(1);   //    setFname   setPrice
					if (method.getName().equals(methodName)) {
						//  setPrice( double类型 的参数）,在这里要考虑方法的参数类型
						//javabean规范   ->  setXXX( 有且只有一个参数)       getXXX(不能有参数)   无参构造方法. 
						String typeName = method.getParameterTypes()[0].getName();
						String value = map.get(key);
						if (value != null  && !"".equals(value)) {
							if ("java.lang.Integer".equals(typeName) || "int".equals(typeName)) {
								method.invoke(t, Integer.parseInt(value));
							} else if ("java.lang.Double".equals(typeName) || "double".equals(typeName)) {
								method.invoke(t, Double.parseDouble(value));
							} else if ("java.lang.Float".equals(typeName) || "float".equals(typeName)) {
								method.invoke(t, Float.parseFloat(value));
							} else if ("java.lang.Long".equals(typeName) || "long".equals(typeName)) {
								method.invoke(t, Long.parseLong(value));
							} else {
								method.invoke(t, value);
							}
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	private Set<Method> getSetMethod(Class c) {
		Method[] ms = c.getMethods();
		Set<Method> result = new HashSet<Method>();
		for (Method m : ms) {
			if (m.getName().startsWith("set")) {
				result.add(m);
			}
		}
		return result;
	}
	
	private Set<Method> getMethod(Class c) {
		Method[] ms = c.getMethods();
		Set<Method> result = new HashSet<Method>();
		for (Method m : ms) {
			if (m.getName().startsWith("get")) {
				result.add(m);
			}
		}
		return result;
	}
}
