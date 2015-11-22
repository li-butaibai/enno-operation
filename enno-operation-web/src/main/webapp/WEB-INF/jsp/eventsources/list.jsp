<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%--<%@ include file="../include/top.inc"%>--%>
<script type="text/javascript">
    $(document).ready(function(){
        $("#pager").pager({pagenumber:${EventSourcePage.currentPageIndex}, pagecount:${EventSourcePage.pageCount}, buttonClickCallback:PageClick});
    });
    PageClick=function(pageclickednumber)
    {
        location.href="/eventsources/list?pageIndex="+pageclickednumber;
        //window.load("/eventsources/list?pageIndex="+pageclickednumber);
    };
    function createEventSource(){
        $.ajax({
            url:"/eventsources/newEventSourceForm",
            type:"get",
            async:true,
            dataType:"text",
            beforeSend: function (XMLHttpRequest) {
                $("#waiting").show();
            },
            success: function (data) {
                $("#waiting").hide();
                $('#dialogDiv').html(data);
                $('#dialogDiv').dialog({ autoOpen: true, modal: true, width: (small ? 345 : 690), show: "drop", hide: "drop", position: [295, 40] });
            },
            error: function (data) {
                $("#waiting").hide();
                rtn = false;
            }
        });
    }
</script>
<div class="wrapper page">
    <div class="topbar">
        <div>
            <div class="breadcrumbs">
                <a class="level level-zone level-zone-2" href="/eventsources/list?pageIndex=1" data-permalink="">Event Sources</a>
            </div>
        </div>
    </div>
    <div class="pane">
        <div class="toolbar">
            <a class="btn" href="#" onclick="createEventSource()">
                <span class="icon icon-run" ></span>
                <span class="text">Add</span>
            </a>
            <%--<a class="btn" href="#">--%>
                <%--<span class="text">Update</span>--%>
            <%--</a>--%>
        </div>
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th class="checkbox">

                </th>
                <th>
                   EventSource
                </th>
                <th>
                    Protcol
                </th>
                <th>
                    Subsciber Count
                </th>
                <th>
                    Event Decoder
                </th>
                <th>
                    Comments
                </th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${EventSourcePage.queryResult}" var="es">
            <tr>
                <td class="checkbox">
                    <input  type="radio" name="checkItem" id="${es.id}"/>
                </td>
                <td >
                    <a href="/eventsources/detail?eventSourceId=${es.id}&Count=0">${es.sourceId}</a>
                </td>
                <td>
                    <span>${es.protocol}</span>
                </td>
                <td>
                    <span>${es.subscriberList.size()}</span>
                </td>
                <td>
                    <span>${es.eventDecoder}</span>
                </td>
                <td>
                    <span>${es.comments}</span>
                </td>
            </tr>
            </c:forEach>
            </tbody>
        </table>
        <div id="pager"></div>
    </div>
</div>
<%--<%@ include file="../include/bottom.inc"%>--%>