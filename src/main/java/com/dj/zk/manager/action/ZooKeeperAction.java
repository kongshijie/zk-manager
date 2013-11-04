package com.dj.zk.manager.action;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dj.zk.manager.commons.Constants;
import com.dj.zk.manager.exceptions.GeneralException;
import com.dj.zk.manager.model.ZkModel;
import com.dj.zk.manager.model.dto.TreeDto;
import com.dj.zk.manager.service.ZookeeperService;
import com.dj.zk.manager.utils.json.JsonUtils;
import com.dj.zk.manager.utils.response.ResponseUtils;


/**
 * 
 * @description:zookeeper菜单树
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:21:32
 */
@Controller
@RequestMapping(value = Constants.BASE_PATH + "zoo")
public class ZooKeeperAction {

	@Autowired
	private ZookeeperService zookeeperService;

	@RequestMapping(value = { "config" }, method = { RequestMethod.POST,RequestMethod.GET })
	public ModelAndView config(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		String path = Constants.getDefaultRoot();
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		boolean flag = zookeeperService.checkNodeExist(path);
		if (!flag) {
			zookeeperService.createNode(path, null);
		}
		String config = "var zookeeper = {root:'" + path + "'}";
		ResponseUtils.responseScript(response, config);
		return null;
	}

	@RequestMapping(value = { "list" }, method = { RequestMethod.POST,RequestMethod.GET })
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
 		String path = request.getParameter("path");
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		List<ZkModel> lists = zookeeperService.listChildNode(path);
		modelMap.addAttribute("data", lists);
		String res = JsonUtils.toJson(lists, null, null, null);
		ResponseUtils.responseJson(response, res);
		return null;
	}

	@RequestMapping(value = { "create" }, method = { RequestMethod.POST })
	public ModelAndView create(HttpServletRequest request,
			HttpServletResponse response, TreeDto treeDto, ModelMap modelMap) {

		if (StringUtils.isEmpty(treeDto.getNode())) {
			throw new GeneralException("新建节点不能为空！");
		}
		if (StringUtils.isEmpty(treeDto.getPath())) {
			throw new GeneralException("父节不能为空！");
		}

		if (!treeDto.getPath().startsWith("/")) {
			treeDto.setPath("/" + treeDto.getPath());
		}
		treeDto.setPath(treeDto.getPath() + "/" + treeDto.getNode());
		try {
			zookeeperService.createNode(treeDto.getPath(), treeDto.getData()
					.getBytes(Constants.charset));
		} catch (UnsupportedEncodingException e) {
		}
		modelMap.addAttribute("msg", "ok");
		ResponseUtils.responseJson(response, "{'msg':'ok'}");
		return null;
	}

	@RequestMapping(value = { "delete" }, method = { RequestMethod.POST })
	public ModelAndView delete(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		String path = request.getParameter("path");
		if (StringUtils.isEmpty(path)) {
			throw new GeneralException("节点path不能为空！");
		}
		zookeeperService.deleteNode(path);
		String msg = "{'msg':'ok'}";
		modelMap.addAttribute("msg", msg);
		ResponseUtils.responseJson(response, msg);
		return null;
	}

	@RequestMapping(value = { "save" }, method = { RequestMethod.POST })
	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		String path = request.getParameter("path");
		String data = request.getParameter("data");
		if (StringUtils.isEmpty(path)) {
			throw new GeneralException("节点path不能为空！");
		}
		try {
			zookeeperService.setData(path, data.getBytes(Constants.charset));
		} catch (UnsupportedEncodingException e) {
		}
		String msg = "{'msg':'ok'}";
		modelMap.addAttribute("msg", msg);
		ResponseUtils.responseJson(response, msg);
		return null;
	}

	@RequestMapping(value = { "finddata" }, method = { RequestMethod.POST })
	public ModelAndView queryDataByPath(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) {
		String path = request.getParameter("path");
		if (StringUtils.isEmpty(path)) {
			throw new GeneralException("节点path不能为空！");
		}
		byte[] data = zookeeperService.getData(path);
		TreeDto td = new TreeDto();
		td.setPath(path);
		String res = "";
		if (data != null) {
			try {
				res = new String(data, Constants.charset);
			} catch (UnsupportedEncodingException e) {
			}

			td.setData(res);
		}
		String jsonRes = JsonUtils.toJson(td, null, new String[] { "data",
				"path" }, null);
		ResponseUtils.responseJson(response, jsonRes);
		return null;
	}

	/**
	 * @description 节点复制
	 * @return
	 */
	@RequestMapping(value = { "nodecopy" }, method = { RequestMethod.POST})
	public ModelAndView nodeCopy(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap) {
	     String sourceNodePath = request.getParameter("sourceNodePath");
	     String targetNodePath = request.getParameter("targetNodePath");
	     String sourceNode = request.getParameter("sourceNode");
	     
	     if(StringUtils.isEmpty(sourceNode)) {
		 		throw new GeneralException("源节点名称不能为空！");
		 }
	     
	 	if(StringUtils.isEmpty(sourceNodePath)) {
			throw new GeneralException("源节点不能为空！");
		}
	 	if(StringUtils.isEmpty(targetNodePath)) {
	 		throw new GeneralException("目标节点不能为空！");
	 	}
	 	
	 	if(!zookeeperService.checkNodeExist(sourceNodePath)) {
			throw new GeneralException("源节点不存在！"); 
		}
		if(!zookeeperService.checkNodeExist(targetNodePath)) {
			throw new GeneralException("目标节点不存在！"); 
		}
		
	 	zookeeperService.copyNodes(sourceNode,sourceNodePath, targetNodePath);
		String msg = "{'msg':'ok'}";
		modelMap.addAttribute("msg", msg);
		ResponseUtils.responseJson(response, msg);
		return null;
	}
}
