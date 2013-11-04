<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String basePathNoSplit = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<link rel="stylesheet" type="text/css" href="<%=basePath%>script/lib/extjs3/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>script/lib/extjs3/resources/css/icon.css" />
<script type="text/javascript">
 var rootPath = '<%=basePath%>';

</script>
<script type="text/javascript" src="<%=basePath%>script/app/commons.js"></script>
<script type="text/javascript" src="<%=basePath%>script/lib/extjs3/ext-base.js"></script>
<script type="text/javascript" src="<%=basePath%>script/lib/extjs3/ext-all.js"></script>
<script type="text/javascript" src="<%=basePath%>script/lib/extjs3/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=basePath%>script/lib/extjs3/framework-ext/Util.js"></script>
<script type="text/javascript" src="<%=basePath%>script/lib/extjs3/framework-ext/DelayMessage.js"></script>
<script type="text/javascript" src="<%=basePath%>script/lib/extjs3/framework-ext/StaticTextField.js"></script>
<script type="text/javascript" src="<%=basePath%>script/lib/extjs3/miframe.js"></script>


<%@include file="/WEB-INF/pages/base/baseExtJsException.jsp" %>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
 
