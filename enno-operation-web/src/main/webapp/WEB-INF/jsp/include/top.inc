<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
<title>Enno-Operation/></title>
	<script src="${pageContext.request.contextPath}/scripts/jquery.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery.validity.js"  type="text/javascript"></script>
	<link href="${pageContext.request.contextPath}/css/console.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/css/enno.operation.css" rel="stylesheet" />
	<script type="text/javascript">
		$(document).ready()(function(){
			alert("ready");
//			var total = document.documentElement.clientHeight;
//			alert(total);
//			$('.enno-container').css("height",total+"px");
//			alert($('.enno-container').height);
		});
		function
	</script>
</head>

<body class="enno-body">

<div class="enno-container">
		<div class="enno-top-bar"></div>
		<div class="enno-content">
			<div class="enno-left-menu"></div>
			<!-- SiteWhere alert dialog -->
			<div id="alert-dialog" class="modal hide" style="z-index: 5000;">

			</div>
			<!-- SiteWhere confirm dialog -->
			<div id="confirm-dialog" class="modal hide" style="z-index: 5000;">

			</div>
			<div id="enno-main-body" class="enno-main-body">