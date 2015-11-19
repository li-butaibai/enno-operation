<div class="zone-items">
    <ul class="level-1">
        <li class="cate no-items home">
            <a class="" href="#" data-permalink="">
                <span class="fa fa-overview"></span>
                <span class="text">Console</span>
            </a>
        </li>
        <li class="cate no-items eventsources">
            <a  class=""  href="/eventsources/list?pageIndex=1" data-permalink="">
                <span class="fa fa-overview"></span>
                <span class="text">Event Source</span>
            </a>
        </li>
        <li class="cate no-items subscribers">
            <a class="" href="/subscribers/list?pageIndex=1" data-permalink="">
                <span class="fa fa-overview"></span>
                <span class="text">Subscriber</span>
            </a>
        </li>
        <li class="cate no-items eventlogs">
            <a class="" href="/eventlogs/list?pageIndex=1" data-permalink="">
                <span class="fa fa-overview"></span>
                <span class="text">Event Log</span>
            </a>
        </li>
    </ul>
</div>
<script>
    $(document).ready(function () {
        $(".level-1 > li > a").click(function () {
            $(".selected").removeClass("selected");
            $(this).parent().addClass("selected");
        });
        var contextPath ='<%=request.getContextPath()%>';
        var pathName=location.pathname;
        pathName=pathName.replace(contextPath,"");
        var urls = pathName.split('/');
        if (urls[1] == undefined) {
            $(".index").addClass("selected");
        }
        else {
            $("." + urls[1]).addClass("selected");
        }
    });
</script>