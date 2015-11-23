<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<div class="wrapper page">
  <div class="topbar">
    <div>
      <div class="breadcrumbs">
        <a class="level level-zone level-zone-2" href="#eventlogs/list?pageIndex=1" data-permalink="">Event Sources</a>
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
        </th>
        <th>
          EventSource
        </th>
        <th>
          Subscriber
        </th>
        <th>
          Title
        </th>
        <th>
          Level
        </th>
        <th>
          Message
        </th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${EventlogPage.queryResult}" var="el">
        <tr>
          <td class="checkbox">
            <input  type="radio" name="checkItem" id="${el.id}"/>
          </td>

          <td>
            <a href="#eventsources/detail?eventSourceId=${el.eventSourceModel.id}&Count=0">${el.eventSourceModel.sourceId}</a>
          </td>
          <td>
            <a href="#subscribers/detail?subscriberId=${el.subscriberModel.id}&Count=0">${el.subscriberModel.name}</a>
          </td>
          <td >
            <span>${el.title}</span>
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
    <div id="pager"></div>
  </div>
</div>
<script type="text/javascript">
  PageClick=function(pageclickednumber)
  {
    location.href="#eventlogs/list?pageIndex="+pageclickednumber;
  }
  $("#pager").pager({pagenumber:${EventlogPage.currentPageIndex}, pagecount:${EventlogPage.pageCount}, buttonClickCallback:PageClick});
</script>