<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../include/top.inc"%>
<div class="wrapper page">
  <div class="topbar">
    <div>
      <div class="breadcrumbs">
        <a class="level level-zone level-zone-2" href="/eventlogs/list?pageIndex=1" data-permalink="">Event Sources</a>
      </div>
    </div>
  </div>
  <div class="pane">
    <div class="toolbar">
      <a class="btn" href="#" onclick="">
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
      <c:forEach items="${EventlogPage.queryResult}" var="el">
        <tr>
          <td class="checkbox">
            <input  type="checkbox" name="checkItem" id="${el.id}"/>
          </td>
          <td >
            <a href="/eventlogs/detail?eventSourceId=${el.id}">${el.title}</a>
          </td>
          <td>
            <a href="/eventsources/detail?eventSourceId=${el.eventSourceModel.id}">${el.eventSourceModel.sourceId}</a>
          </td>
          <td>
            <a href="/subscribers/detail?subscirberId=${el.subscriberModel.id}">${el.subscriberModel.name}</a>
          </td>
          <td>
            <span>${el.level}</span>
          </td>
          <td>
            <span>${el.message}</span>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</div>
<%@ include file="../include/bottom.inc"%>