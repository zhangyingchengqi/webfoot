package com.yc.test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yc.bean.Resadmin;
import com.yc.bean.Resfood;
import com.yc.dao.DBHelper;

public class Test2 {

	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		DBHelper db=new DBHelper();
		List<Object> params=new ArrayList<Object>();
		//动态数组： null,  什么都没有,   Object[]　　　－》　　　ｊｄｋ１.７以后．
		//List:     null,   List对象
		//List<Map<String,Object>> list=db.finds("select * from resfood ");
	//	for(Map<String,Object> map: list){
		//	System.out.println(    map);
		//}
		
		List< Resfood  > listt=db.find("select * from resorder",  Resfood.class);
		
		for(  Resfood r: listt){
			System.out.println ( r );
		}
	}

}
