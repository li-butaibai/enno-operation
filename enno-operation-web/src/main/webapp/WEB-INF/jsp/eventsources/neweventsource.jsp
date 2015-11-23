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
        <div class="modal-content">
            <form id="form" class="form form-horizontal" method="post" action="/eventsources/add">
                <fieldset>
                    <legend>Create new event source</legend>
                    <div class="item">
                        <div class="control-label">Event Source Name</div>
                        <div class="controls">
                            <input type="text" name="sourceId" autofocus="">
                        </div>
                    </div>
                    <div class="item">
                        <div class="control-label">Event Decoder</div>
                        <div class="controls">
                            <input type="text" name="eventDecoder" value="mqtt-event-decoder">
                            <%--<span class="help">自管私有网络需要您自行配置和管理其网络。</span>--%>
                        </div>
                    </div>
                    <div class="item">
                        <div class="control-label">Template</div>
                        <div class="controls">
                            <div class="select-con">
                                <select id="estemplate" name="eventSourceTemplateId" class="dropdown-select">
                                    <option value="-1" selected="selected">----Please Selecte Template----</option>
                                    <c:forEach items="${ESTemplateList}" var="est">
                                        <option value="${est.id}">${est.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div id="connectInfoDiv">

                    </div>
                    <div class="item">
                        <div class="control-label">Comments</div>
                        <div class="controls">
                            <textarea name="comments" style="width:400px" rows="3" placeholder="Add your comments for the event source"></textarea>
                        </div>
                    </div>
                    <div class="form-actions">
                        <input id="submit" class="btn btn-primary" type="button" value="Submit" onclick="submitForm('form','/eventsources/add')"/>
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
    });

</script>