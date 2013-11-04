
<%@page import="com.dj.zk.manager.commons.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/pages/base/baseExtJs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>命令执行</title>

<link rel="stylesheet" type="text/css" href="<%=basePath%>script/lib/extjs3/ux/treegrid/treegrid.css" rel="stylesheet" />

 <script type="text/javascript" src="<%=basePath%>script/lib/extjs3/ux/treegrid/TreeGridSorter.js"></script>
 <script type="text/javascript" src="<%=basePath%>script/lib/extjs3/ux/treegrid/TreeGridColumnResizer.js"></script>
 <script type="text/javascript" src="<%=basePath%>script/lib/extjs3/ux/treegrid/TreeGridNodeUI.js"></script>
 <script type="text/javascript" src="<%=basePath%>script/lib/extjs3/ux/treegrid/TreeGridLoader.js"></script>
 <script type="text/javascript" src="<%=basePath%>script/lib/extjs3/ux/treegrid/TreeGridColumns.js"></script>
 <script type="text/javascript" src="<%=basePath%>script/lib/extjs3/ux/treegrid/TreeGrid.js"></script>
 
<script type="text/javascript" src="<%=basePathNoSplit+Constants.BASE_PATH%>watcher/clientInfo.do"></script>
 <script type="text/javascript" src="<%=basePath%>script/app/watcherclient/watcher_client_tree_grid.js"></script>

<script type="text/javascript" src="<%=basePath%>script/app/watcherclient/control.js"></script>
<script type="text/javascript" src="<%=basePath%>script/app/watcherclient/watcher_client_layout.js"></script>

</head>
 
</html>