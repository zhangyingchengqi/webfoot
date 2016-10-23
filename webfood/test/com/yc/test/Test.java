package com.yc.test;

import java.sql.Connection;

import com.yc.dao.DBHelper;

public class Test {

	public static void main(String[] args) {
		DBHelper db=new DBHelper();
		Connection con=db.getConn();
		
		System.out.println(  con );
	}

}
