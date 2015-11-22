<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
<title>Enno-Operation/></title>
	<script src="${pageContext.request.contextPath}/scripts/jquery.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/jquery.validity.js"  type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/jquery.pager.js"  type="text/javascript"></script>
	<link href="${pageContext.request.contextPath}/css/Console.css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/Pager.css" rel="stylesheet" />
</head>
<script type="text/javascript">
	$().ready(function(){
		var hash = location.hash;
		if(hash == "")
		{
			$.ajax({
				url:"/eventsources/list?pageIndex=0",
				type:"get",
				async:true,
				dataType:"text",
				beforeSend: function (XMLHttpRequest) {
//					$("#waiting").show();
				},
				success: function (data) {
//					$("#waiting").hide();
					$('#content').html(data);
//					$('#dialogDiv').dialog({ autoOpen: true, modal: true, width: (small ? 345 : 690), show: "drop", hide: "drop", position: [295, 40] });
				},
				error: function (data) {
//					$("#waiting").hide();
					rtn = false;
				}
			});
		}
	});
</script>
<body class="enno-body">

<div id="dialogDiv"></div>
<div id="waiting" class="waiting" style="display: none; z-index: 99999;">
	<img src="/Images/waiting.gif" />
</div>
<div class="container">
	<div class="viewport" id="console">
		<div class="nav-global">
			<div>
				<div class="global-logo">
					<p class="site-title">Enno-Operation Platform</p>
				</div>
				<div class="items-inner">

				</div>
			</div>
		</div>
		<div class="nav-zone">
			<div class="nav-zone-inner">
				<%@ include file="leftmenu.inc"%>
			</div>
		</div>
		<div class="content" id="content">
