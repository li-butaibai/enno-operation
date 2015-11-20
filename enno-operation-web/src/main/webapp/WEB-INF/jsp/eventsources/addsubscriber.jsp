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
    <div class="modal-header" style="cursor: move;"><h4>New Event Source<a href="#" class="close"><span
            class="icon-close icon-Large"></span></a></h4></div>
    <div class="modal-content" id="">
      <form id="form" class="form form-horizontal" method="post" action="/eventsources/add">
        <fieldset>
          <legend>Create new event source</legend>
          <div class="item">
            <div class="control-label">Event Source Name</div>
            <div class="controls">
              <input type="text" name="eventSourceId" value="${EventSource.id}" />
              <input type="text" name="sourceId" disabled="disabled" value="${EventSource.sourceId}">
            </div>
          </div>
          <div class="item">
            <div class="control-label">Template</div>
            <div class="controls">
              <div class="select-con">
                <select id="subscriberSelect" name="subscriberId" class="dropdown-select">
                  <option value="-1" selected="selected">----Please Selecte Template----</option>
                  <c:forEach items="${SubscriberList}" var="est">
                    <option value="${est.id}">${est.name}</option>
                  </c:forEach>
                </select>
              </div>
            </div>
          </div>
          <div class="form-actions">
            <input id="submit" class="btn btn-primary" type="button" value="Submit"/>
            <input class="btn btn-cancel" type="button" value="Cancel"/>
          </div>
        </fieldset>
      </form>
    </div>
    <div class="modal-footer"></div>
  </div>
</div>
<script type="text/javascript">
  $().ready(function(){
    $('.close').click(function(){
      $('#dialogDiv').html("");
      $('#dialogDiv').dialog('close');
    });
    $("#estemplate").change(function(){
      var selectedId = $(this).children('option:selected').val();
      getConnectInfo(selectedId)
      var mt = $("#modal").outerHeight()/2;
      if(mt+100 > window.outerHeight/2)
      {
        mt=$(body).innerHeight()/2-100;
      }
      $("#modal").css({"margin-top":mt*(-1)+"px"});
    });
    $('#submit').click(function(){
      $.ajax({
        url:"/eventsources/addSubscriber",
        type:"post",
        async:false,
        dataType:"json",
        data:$("#form").serialize(),
        success: function (data) {
          if(data.success)
          {
            $('#dialogDiv').dialog('close');
            window.location.reload();
          }
          else
          {
            alert(data.emMessage);
          }
        },
        error: function(data) {
          alert(data);
        }
      });
    });
  });
  function getConnectInfo(esId)
  {
    $.ajax({
      url:"/eventsources/getconnectinfo?templateId="+esId,
      type:"get",
      async:false,
      dataType:"text",
      success:function(data){
        $('#connectInfoDiv').html(data);
      },
    });
  }
</script>