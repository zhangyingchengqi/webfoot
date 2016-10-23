package com.yc.bean;

import java.io.Serializable;

public class Resuser implements Serializable {

	private static final long serialVersionUID = 1676684284807690359L;

	private int userid;
	private String username;
	private String pwd;
	private String email;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "Resuser [userid=" + userid + ", username=" + username + ", pwd=" + pwd + ", email=" + email + "]";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
