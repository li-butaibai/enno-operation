<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<div class="overview">
  <div class="topbar">
    <div>
      <div class="breadcrumbs">
        <a class="level level-zone level-zone-2" href="#eventsources/list?pageIndex=1" data-permalink="">Event Sources</a>
        <a class="level" href="#" data-permalink="">${EventSource.sourceId}</a>
      </div></div>
  </div>
  <div class="overview-main" style="width: 35%">

          <div class="description" style="width: 100%">
            <div class="detail-item">
              <div class="title"><h3>Event Soure</h3>

                <div class="dropdown"><input class="dropdown-toggle" type="text">

                  <div class="dropdown-text"><span class="icon-menu"></span></div>
                  <div class="dropdown-content dropdown-wide">
                    <a class="btn" href="javascript:void(0);" onclick="getAddSubscriber2ESForm('${EventSource.id}')">
                      <span class="icon icon-run" ></span>
                      <span class="text">Add Subscriber</span>
                    </a>
                    <a class="btn" href="javascript:void(0);" onclick="getEditESForm('${EventSource.id}')">
                      <span class="icon icon-run" ></span>
                      <span class="text">Edit</span>
                    </a>
                    <a class="btn" href="javascript:void(0);" onclick="offlineEventSource('${EventSource.id}')"><span class="icon icon-resize"></span><span class="text">Offline</span></a>
                    <a class="btn" onclick="deleteEventSource('${EventSource.id}')"><span class="icon icon-adduser"></span><span class="text">Delete</span></a>
                  </div>
                </div>
              </div>
              <dl class="dl-horizontal">
                <dt>SourceId</dt>
                <dd>${EventSource.sourceId}</dd>
                <dt>Event Type</dt>
                <dd>${EventSource.eventSourceTemplateName}</dd>
                <dt>Status</dt>
                <dd>${EventSource.state}</dd>
                <dt>Subscribers</dt>
                <dd>
                  <c:forEach items="${EventSource.subscriberList}" var="ss">
                    ${ss.name}
                    <a href="javascript:void(0);" onclick="removeSubscriberFromES('${EventSource.id}','${ss.id}')"><span>[DELETE]</span></a>
                    <br/>
                  </c:forEach>
                </dd>
                <dt>Potocol</dt>
                <dd>${EventSource.protocol}</dd>
                <dt>Event Decoder</dt>
                <dd>${EventSource.eventDecoder}</dd>
                <dt>Test</dt>
                <dd data-tooltip="" data-original-title="just for test">1,1,1</dd>
                <c:forEach items="${EventSource.eventSourceActivities}" var="esa">
                  <dt>${esa.displayName}</dt>
                  <dd>${esa.value}</dd>
                </c:forEach>
                <dt>Create Time</dt>
                <dd>${EventSource.createTime}</dd>
                <dt>Update Time</dt>
                <dd>${EventSource.updateTime}</dd>
                <dt>Comments</dt>
                <dd>${EventSource.comments}</dd>
              </dl>
            </div>
          </div>
  </div>
  <div class="overview-activities" style="width: 63%">
    <div class="activities-inner">
      <h4 class="activities-title">
        Event Log
        <%--<a href="#" >All</a>--%>
      </h4>
      <ol class="activities-items">
        <%--<c:if test="${EventLogList.size==0}">--%>
          <%--no event log!--%>
        <%--</c:if>--%>
        <c:forEach items="${EventLogList}" var="el">
          <li>
            <c:if test="${el.level==0}">
              <span class="job-status failed"></span>
            </c:if>
            <c:if test="${el.level==1}">
              <span class="job-status working"></span>
            </c:if>
            <c:if test="${el.level==2}">
              <span class="job-status successful"></span>
            </c:if>
            <div class="job-details">
              <a class="job-action" href="#">${el.title}</a>
              <span class="consumed"></span>
              <span class="job-time">2015-11-28 10:28:43</span>
              <ul class="resources">
                <li>Event Source: ${el.eventSourceModel.sourceId}</li>
                <li>Subscriber: ${el.subscriberModel.name}</li>
                <li>${el.message}</li>
              </ul>
            </div>
          </li>
        </c:forEach>
      </ol>
    </div>
  </div>
</div>
</div>
