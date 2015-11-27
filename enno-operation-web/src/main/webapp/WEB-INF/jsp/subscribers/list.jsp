<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<div class="wrapper page">
  <div class="topbar">
    <div>
      <div class="breadcrumbs">
        <a class="level level-zone level-zone-2" href="#subscribers/list?pageIndex=1&pageSize=10" data-permalink="">Subscribers</a>
      </div>
    </div>
  </div>
  <div class="pane">
    <div class="toolbar">
      <a class="btn Online-NoSubscriber Offline-NoSubscriber Online-Subscriber" href="javascript:void(0);" style="display: none"
         onclick="toolbarFunc(getEditSubscriberForm)">
        <span class="text">Update</span>
      </a>
      <a class="btn Online-NoSubscriber Offline-NoSubscriber Online-Subscriber" href="javascript:void(0);" style="display: none"
              onclick="toolbarFunc(offlineSubscriber)">
        <span class="text">Offline</span>
      </a>
      <a class="btn Offline-NoSubscriber" href="javascript:void(0);" style="display: none" onclick="toolbarFunc(deleteSubscriber)">
        <span class="text">Delete</span>
      </a>
    </div>
    <table class="table table-bordered table-hover">
      <thead>
      <tr>
        <th class="checkbox">

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
            <input  type="radio" name="checkItem" id="${sb.id}" lang="${sb.state}-${sb.eventsourceList.size()==0?'NoSubscriber':"Subscriber"}"/>
          </td>
          <td >
            <a href="#subscribers/detail?subscriberId=${sb.id}&Count=0">${sb.name}</a>
          </td>
          <td>
            <span>${sb.state}</span>
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
    <div id="pager"></div>
  </div>
</div>
<script type="text/javascript">
  PageClick=function(pageclickednumber)
  {
    location.href="#subscribers/list?pageIndex="+pageclickednumber;
  }

  $("#pager").pager({pagenumber:${SubscriberPage.currentPageIndex}, pagecount:${SubscriberPage.pageCount}, buttonClickCallback:PageClick});

  $("[name='checkItem']:radio").on('click',function(){
    $('.sbtn').hide();
    $('.' + $(this).attr('lang')).show();
  });
</script>