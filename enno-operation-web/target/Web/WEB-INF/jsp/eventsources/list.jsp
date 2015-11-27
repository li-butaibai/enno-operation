<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%--<%@ include file="../include/top.inc"%>--%>

<div class="wrapper page">
    <div class="topbar">
        <div>
            <div class="breadcrumbs">
                <a class="level level-zone level-zone-2" href="#eventsources/list?pageIndex=1" data-permalink="">Event Sources</a>
            </div>
        </div>
    </div>
    <div class="pane">
        <div class="toolbar">
            <a class="btn" href="javascript:void(0);" onclick="getCreateEventSourceForm()">
                <span class="icon icon-run" ></span>
                <span class="text">Add</span>
            </a>
            <a class="btn stbn Online-NoSubscriber offline-NoSubscriber" href="javascript:void(0);" style="display: none;" onclick="toolbarFunc(getEditESForm)">
                <span class="text">Update</span>
            </a>
            <a class="btn sbtn Online-NoSubscriber" href="javascript:void(0);" style="display: none;" onclick="toolbarFunc(offlineEventSource)">
                <span class="text">Offline</span>
            </a>
            <a class="btn sbtn Offline-NoSubscriber" href="javascript:void(0);" style="display: none;" onclick="toolbarFunc(deleteEventSource)">
                <span class="text">Delete</span>
            </a>
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
                    Status
                </th>
                <th>
                    Protcol
                </th>
                <th>
                    Subscriber Count
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
                    <input  type="radio" name="checkItem" id="${es.id}" lang="${es.state}-${es.subscriberList.size()==0?'NoSubscriber':'Subscriber'}"/>
                </td>
                <td >
                    <a href="#eventsources/detail?eventSourceId=${es.id}&Count=0">${es.sourceId}</a>
                </td>
                <td>
                    <span>${es.state}</span>
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
<script type="text/javascript">
    PageClick=function(pageclickednumber)
    {
        location.href="#eventsources/list?pageIndex="+pageclickednumber;
    }
    //    $(document).ready(function(){
    $("#pager").pager({pagenumber:${EventSourcePage.currentPageIndex}, pagecount:${EventSourcePage.pageCount}, buttonClickCallback:PageClick});
    //    });
    $("[name='checkItem']:radio").on('click',function(){
        $('.sbtn').hide();
        $('.' + $(this).attr('lang')).show();
    });

</script>
<%--<%@ include file="../include/bottom.inc"%>--%>