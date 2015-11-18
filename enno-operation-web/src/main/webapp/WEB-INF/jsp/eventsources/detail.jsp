<%--
  Created by IntelliJ IDEA.
  User: EriclLee
  Date: 15/11/17
  Time: 22:17
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../include/top.inc"%>
<div class="overview">
  <div class="topbar">
    <div>
      <div class="breadcrumbs">
        <a class="level level-zone level-zone-2" href="/eventsources/list?pageIndex=0&pageSize=10" data-permalink="">Event Sources</a>
        <a class="level" href="#" data-permalink="">${EventSource.sourceId}</a>
      </div></div>
  </div>
  <div class="overview-main">
    <div class="overview-quotas">
      <div class="overview-quotas-inner">
        <div class="quotas-title">

        </div>
      </div>
    </div>

  </div>
  <div class="overview-activities">
    <div class="activities-inner">
      <h4 class="activities-title">
        News
        <a href="#" >All</a>
      </h4>
      <ol class="activities-items">
        <li>
          <span class="job-status successful"></span>
          <div class="job-details">
            <a class="job-action" href="#">Create Subscriber</a>
            <span class="consumed"></span>
            <span class="job-time">2015-11-28 10:28:43</span>
            <ul class="resources">
              <li>Enno01 create subscriber to AMQ01!</li>
            </ul>
          </div>
        </li>
      </ol>
    </div>
  </div>
</div>
<%@ include file="../include/bottom.inc"%>
