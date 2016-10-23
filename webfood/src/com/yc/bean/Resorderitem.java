package com.yc.bean;

import java.io.Serializable;

public class Resorderitem implements Serializable {
	private static final long serialVersionUID = 4415732811555252222L;
	private Integer roiid ;
	private Integer roid ;
	private Integer fid   ;
	private Double dealprice ;
	private Integer num ;
	public Integer getRoiid() {
		return roiid;
	}
	public void setRoiid(Integer roiid) {
		this.roiid = roiid;
	}
	public Integer getRoid() {
		return roid;
	}
	public void setRoid(Integer roid) {
		this.roid = roid;
	}
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public Double getDealprice() {
		return dealprice;
	}
	public void setDealprice(Double dealprice) {
		this.dealprice = dealprice;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "Resorderitem [roiid=" + roiid + ", roid=" + roid + ", fid=" + fid + ", dealprice=" + dealprice
				+ ", num=" + num + "]";
	}
	
	
	
}
