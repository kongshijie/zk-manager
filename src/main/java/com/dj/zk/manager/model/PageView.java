package com.dj.zk.manager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @description:页面展示对象
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:24:34
 */
public class PageView<T> {

	private int totalCount;
	
	
	private List<T> items = new ArrayList<T>();
	
	public PageView() {
	}
	

	public PageView(int totalCount, List<T> items) {
		super();
		this.totalCount = totalCount;
		this.items = items;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

}
