<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../include/top.inc"%>
<div class="wrapper page">
  <div class="topbar">
    <div>
      <div class="breadcrumbs">
        <a class="level level-zone level-zone-2" href="/subscribers/list?pageIndex=1" data-permalink="">Event Sources</a>
      </div>
    </div>
  </div>
  <div class="pane">
    <div class="toolbar">
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
          Subscriber Name
        </th>
        <th>
          Status
        </th>
        <th>
          Event Source Count
        </th>
        <th>
          Address
        </th>
        <th>
          Comments
        </th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${SubscriberPage.queryResult}" var="sb">
        <tr>
          <td class="checkbox">
            <input  type="checkbox" name="checkItem" id="${sb.id}"/>
          </td>
          <td >
            <a href="/subscribers/detail?subscriberId=${sb.id}">${sb.name}}</a>
          </td>
          <td>
            <span>${sb.protocol}</span>
          </td>
          <td>
            <span>${sb.eventsourceList.size()}</span>
          </td>
          <td>
            <span>${sb.address}</span>
          </td>
          <td>
            <span>${sb.comments}</span>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</div>
<%@ include file="../include/bottom.inc"%>