<%--
  Created by IntelliJ IDEA.
  User: EriclLee
  Date: 15/11/18
  Time: 19:09
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>

<c:if test="${ESTemplate != null}">
  <div class="item">
    <div class="control-label">Protocol</div>
    <div class="controls">
      <input type="text" name="protocol" value="${ESTemplate.protocol}">
  </div>
</c:if>
<c:forEach items="${ESTemplate.eventSourceTemplateActivityModels}" var="esta">
<div class="item">
  <div class="control-label">${esta.displayName}</div>
  <div class="controls">
    <input type="hidden" name="templateActivityIds" value="${esta.id}" />
    <input type="hidden" name="eaNames" value="${esta.name}" />
    <input type="text" name="eaValues"/>
    <%--<span class="help">自管私有网络需要您自行配置和管理其网络。</span>--%>
  </div>
</div>
</c:forEach>