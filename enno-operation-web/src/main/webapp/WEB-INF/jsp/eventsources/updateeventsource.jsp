<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<%--
  Created by IntelliJ IDEA.
  User: EriclLee
  Date: 15/11/18
  Time: 10:23
  To change this template use File | Settings | File Templates.
--%>

<div id="windows-overlay" class="window-overlay">
  <div id="modal" class="modal"
       style="width: 800px; height: auto; margin-left:-400px; margin-top: -168px; top:50%; left:50%; z-index: 1000; ">
    <div class="modal-header" style="cursor: move;"><h4>Update Event Source<a href="javascript:void(0);" onclick="closeDialog" class="close"><span
            class="icon-close icon-Large"></span></a></h4></div>
    <div class="modal-content" id="">
      <form id="form" class="form form-horizontal" method="post" action="/eventsources/add">
        <fieldset>
          <legend>Update new event source</legend>
          <div class="item">
            <div class="control-label">Event Source Name</div>
            <div class="controls">
              <input type="hidden" name="id" value="${EventSource.id}"/>
              <input type="text" name="sourceId" autofocus="" value="${EventSource.sourceId}">
            </div>
          </div>
          <div class="item">
            <div class="control-label">Event Decoder</div>
            <div class="controls">
              <input type="text" name="eventDecoder" value="${EventSource.eventDecoder}">
              <%--<span class="help">自管私有网络需要您自行配置和管理其网络。</span>--%>
            </div>
          </div>
          <input type="hidden" name="eventSourceTemplateId" value="${EventSource.eventSourceTemplateId}"/>
          <input type="hidden" name="protocol" value="${EventSource.protocol}"/>
          <c:forEach items="${EventSource.eventSourceActivities}" var="esa">
            <div class="item">
              <div class="control-label">${esa.displayName}</div>
              <div class="controls">
                <input type="hidden" name="templateActivityIds" value="${esa.templateActivityId}" />
                <input type="hidden" name="eaNames" value="${esa.name}" />
                <input type="text" name="eaValues" value="${esa.value}"/>
              </div>
            </div>
          </c:forEach>
          <div class="item">
            <div class="control-label">Comments</div>
            <div class="controls">
              <textarea name="comments" style="width:400px" rows="3" placeholder="Add your comments for the event source">${EventSource.comments}</textarea>
            </div>
          </div>
          <div class="form-actions">
            <input id="submit" class="btn btn-primary" type="button" value="Submit" onclick="submitForm('form','/eventsources/update')"/>
            <input class="btn btn-cancel" type="button" value="Cancel" onclick="closeDialog()"/>
          </div>
        </fieldset>
      </form>
    </div>
    <div class="modal-footer"></div>
  </div>
</div>