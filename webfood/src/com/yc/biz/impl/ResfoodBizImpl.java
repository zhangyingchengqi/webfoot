package com.yc.biz.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yc.bean.Resfood;
import com.yc.biz.ResfoodBiz;
import com.yc.dao.DBHelper;
import com.yc.utils.YcConstant;
import com.yc.utils.redisutil.fn5.util.RedisUtil;

import redis.clients.jedis.Jedis;

public class ResfoodBizImpl implements ResfoodBiz {
	private DBHelper db = new DBHelper();
	private Jedis jedis = new Jedis(YcConstant.REDIS_URL, YcConstant.REDIS_PORT);

	RedisUtil<Resfood> ru = new RedisUtil<Resfood>();

	@Override
	public Resfood getResfoodByFid(Integer fid)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Resfood resfood = null;
		List<Resfood> list = null;
		try {
			jedis.connect();
			if (jedis.isConnected() == true && jedis.keys(YcConstant.ALLFOOD + ":" + fid).size() > 0) {
				list = ru.getFromHash(jedis, YcConstant.ALLFOOD + ":" + fid, "fid", Resfood.class);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			
		}
		if( list==null){
			List<Object> params = new ArrayList<Object>();
			params.add(fid);
			list = db.find("select * from resfood where fid=?", Resfood.class, params);
		}
		if (list != null && list.size() > 0) {
			resfood = list.get(0);
		}
		return resfood;
	}

	@Override
	public List<Resfood> findAll() throws Exception {
		// 1. 判断jedis中是否有数据,如果有，则用redis中的数据.
		// 2. 没有则从数据查一次.
		List<Resfood> list = null;
		try {
			jedis.connect();
			if (jedis.isConnected() == true && jedis.keys(YcConstant.ALLFOOD + ":*").size() > 0) {
				RedisUtil<Resfood> ru = new RedisUtil<Resfood>();
				list = ru.getFromHash(jedis, YcConstant.ALLFOOD + ":*", "fid", Resfood.class);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if(   list==null){
			list = db.find("select * from resfood", Resfood.class);
			RedisUtil<Resfood> ru = new RedisUtil<Resfood>();
			ru.saveToHash(jedis, YcConstant.ALLFOOD, "fid", list, Resfood.class);
		}
		return list;
	}

	public static void main(String[] args) throws Exception {
		ResfoodBizImpl rbi = new ResfoodBizImpl();
		//List<Resfood> list = rbi.findAll();
		//for (Resfood f : list) {
		//	System.out.println(f);
		//}
		 Resfood rf=rbi.getResfoodByFid(1);
		 System.out.println( rf);
	}

}
