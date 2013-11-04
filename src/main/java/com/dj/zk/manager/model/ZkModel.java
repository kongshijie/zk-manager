package com.dj.zk.manager.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "zkModel")
public class ZkModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1464340772711209136L;

	/**
	 * 节点名称
	 */
	private String text;
	
	/**
	 * 路径
	 */
	private String path;

	/**
	 * 是否子节点
	 */
	private boolean leaf;
	
	/**
	 * 数据
	 */
	private String data;
	
//	private boolean checked = true;
	
	/**
	 * 子节点集合
	 */
	private List<ZkModel> childLists = new ArrayList<ZkModel>();

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public List<ZkModel> getChildLists() {
		return childLists;
	}

	public void setChildLists(List<ZkModel> childLists) {
		this.childLists = childLists;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

//	public boolean isChecked() {
//		return checked;
//	}
//
//	public void setChecked(boolean checked) {
//		this.checked = checked;
//	}
	
	
	
}
