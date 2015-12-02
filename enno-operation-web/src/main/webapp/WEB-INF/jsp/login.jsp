<%--
  Created by IntelliJ IDEA.
  User: EriclLee
  Date: 15/12/2
  Time: 13:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>
<html>
<head>
  <title>Enno-Operation Login Page</title>
  <script src="${pageContext.request.contextPath}/scripts/jquery.js" type="text/javascript"></script>
  <script src="${pageContext.request.contextPath}/scripts/jquery.validity.js"  type="text/javascript"></script>
  <script src="${pageContext.request.contextPath}/scripts/jquery.pager.js"  type="text/javascript"></script>
  <script src="${pageContext.request.contextPath}/scripts/bootstrap.min.js" type="text/javascript"></script>
  <script src="${pageContext.request.contextPath}/scripts/enno.operation.js" type="text/javascript"></script>
  <link href="${pageContext.request.contextPath}/css/Console.css" rel="stylesheet" />
  <link href="${pageContext.request.contextPath}/css/Pager.css" rel="stylesheet" />
</head>
<body class="modal-ready">
<div class="container">
  <div class="page-home">
    <div class="wrapper">
      <div class="home-logo en"></div>
      <div class="form-wrapper" style="width:246px">
        <form class="form" action="/account/login" method="POST">
          <%--<c:if test="${loginFaild==true}">--%>
            <%--<span>${loginMessage}</span>--%>
          <%--</c:if>--%>
          <div style="">
            <fieldset><legend>Login</legend>
              <div class="item user">
                <input type="text" name="userName" placeholder="User Name">
              </div>
              <div class="item password">
                <input type="password" name="password" placeholder="Password">
              </div>
              <div class="item">
                <input class="btn btn-primary" type="submit" value="Login" style="width:246px">
              </div>
            </fieldset>
          </div>
        </form>
      </div>
      <div class="home-footer"></div>
    </div>
  </div>
</div>
</body>
</html>
