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
    <div class="modal-header" style="cursor: move;"><h4>Update Subscriber<a href="javascript:void(0);" class="close" onclick="closeDialog()"><span
            class="icon-close icon-Large"></span></a></h4></div>
    <div class="modal-content" id="">
      <form id="form" class="form form-horizontal" method="post">
        <fieldset>
          <legend>Update Subscriber</legend>
          <div class="item">
            <div class="control-label">Subsciber Name</div>
            <div class="controls">
              <div class="control-text">
                <input type="hidden" name="id" value="${Subscriber.id}">
                ${Subscriber.name}
              </div>
            </div>
          </div>
          <div class="item">
            <div class="control-label">Address</div>
            <div class="controls">
              <input type="text" name="address" value="${Subscriber.address}">
              <%--<span class="help">自管私有网络需要您自行配置和管理其网络。</span>--%>
            </div>
          </div>
          <div class="item">
            <div class="control-label">Comments</div>
            <div class="controls">
              <textarea name="comments" style="width:400px" rows="3" placeholder="Add your comments for the subscriber">${Subscriber.comments}</textarea>
            </div>
          </div>
          <div class="form-actions">
            <input id="submit" class="btn btn-primary" type="button" value="Submit" onclick="submitForm('form','/subscribers/update')"/>
            <input class="btn btn-cancel" type="button" value="Cancel" onclick="closeDialog()"/>
          </div>
        </fieldset>
      </form>
    </div>
    <div class="modal-footer"></div>
  </div>
</div>