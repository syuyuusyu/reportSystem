<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>报表系统</title>
	<link rel="stylesheet" type="text/css" href="./../resources/ext/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="./../resources/ext/resources/css/myicon.css" />
	<script type="text/javascript" src="./../resources/ext/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="./../resources/ext/ext-all-debug.js"></script>
	
	<script type="text/javascript" src="./../resources/WdatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="./../resources/WdatePicker/lang/zh-cn.js"></script>
	<script type="text/javascript" src="./../resources/echart/echarts.js"></script>
	<script type="text/javascript" src="./../scripts/report.js"></script>
	<script type="text/javascript" src="./../scripts/chart.js"></script>
</head>
  
  <body>
	<input id="selectedId" type="hidden" value="<%=request.getAttribute("selectedId") %>"/>
  </body>
</html>
