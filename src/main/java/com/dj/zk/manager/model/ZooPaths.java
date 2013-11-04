package com.dj.zk.manager.model;


/**
 * 
 * @description:
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:25:54
 */
public class ZooPaths {

	/**
	 * 主键ID
	 */
	private Long id;

	/**
	 * 全路径
	 */
	private String fullPath;

	/**
	 * 当前路径
	 */
	private String curPath;

	/**
	 * 创建时间
	 */
	private long createTime;

	/**
	 * 父节点ID
	 */
	private Long parentId;

	/**
	 * 更新时间
	 */
	private long updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getCurPath() {
		return curPath;
	}

	public void setCurPath(String curPath) {
		this.curPath = curPath;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

}
