package com.dj.zk.manager.model;


/**
 * 
 * @description:角色与zoopath资源权限
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:25:37
 */
public class RoleZooPathMid {

	private Long id;

	private Long roleId;

	private Long zooPathId;

	private long createTime;
 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getZooPathId() {
		return zooPathId;
	}

	public void setZooPathId(Long zooPathId) {
		this.zooPathId = zooPathId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
