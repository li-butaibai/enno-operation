<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../include/top.inc"%>
<div class="wrapper page">
    <div class="topbar">
        <div class="breadcrumbs">
            <a class="level level-zone gd1 pjaxA" href="#" data-permalink="">Event Source</a>
        </div>
    </div>
    <div class="pane">
        <div class="toolbar">
            <a class="btn" href="#" onclick="createTenant()">
                <span class="icon icon-run" ></span>
                <span class="text">Add</span>
            </a>
            <a class="btn" href="#">
                <span class="text">Update</span>
            </a>
        </div>
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th class="checkbox">
                    <input type="checkbox" name="checkAll"/>
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
                    <input  type="checkbox" name="checkItem" id="${es.id}"/>
                </td>
                <td >
                    <span>${es.sourceId}</span>
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
    </div>
</div>
<%@ include file="../include/bottom.inc"%>