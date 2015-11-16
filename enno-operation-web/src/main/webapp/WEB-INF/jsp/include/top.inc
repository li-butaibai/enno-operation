<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html class="sw-body">
<head>
<title>Enno-Operation/></title>
<script src="${pageContext.request.contextPath}/scripts/jquery-1.10.2.min.js"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery.validity.js"></script>
<script src="${pageContext.request.contextPath}/scripts/kendo.web.min.js"></script>
<script src="${pageContext.request.contextPath}/scripts/modernizr.js"></script>
<script src="${pageContext.request.contextPath}/scripts/sitewhere.js"></script>
<script src="${pageContext.request.contextPath}/scripts/timeago.js"></script>
<script src="${pageContext.request.contextPath}/scripts/sitewhere.js"></script>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" />
<link href="${pageContext.request.contextPath}/css/kendo.common.min.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/css/kendo.bootstrap.min.css" rel="stylesheet" />
</head>
<body class="enno-body">
	<div class="enno-container">
		<div class="enno-top-bar" style="background-color: #000088;height: 10px; position: fixed;"></div>
		<div class="enno-content">
			<div class="enno-leftmenu"></div>
			<!-- SiteWhere alert dialog -->
			<div id="alert-dialog" class="modal hide" style="z-index: 5000;">

			</div>

			<!-- SiteWhere confirm dialog -->
			<div id="confirm-dialog" class="modal hide" style="z-index: 5000;">

			</div>
			<div id="enno-maincontent">