/**
 * Created by EriclLee on 15/11/23.
 */
$().ready(function(){
    window.onhashchange = onHashChanged;
    onHashChanged();
});

function highlightMenuTab(menuId){
    $(".level-1 > li > a").click(function () {
        $(".selected").removeClass("selected");
        $(this).parent().addClass("selected");
    });
    if (menuId != undefined) {
        $(".selected").removeClass("selected");
        $("#" + menuId).addClass("selected");
    }
}

function onHashChanged(){
    var hash = location.hash;
    var url = "/eventsources/list?pageIndex=0";
    if(hash != "")
    {
        url = hash.replace('#','/');
    }
    var tabName = url.split("/")[1];
    highlightMenuTab(tabName)
    $.ajax({
        url:url,
        type:"get",
        async:true,
        dataType:"text",
        beforeSend: function (XMLHttpRequest) {
            $("#waiting").show();
        },
        success: function (data) {
            $("#waiting").hide();
            $('#content').html(data);
        },
        error: function (data) {
            $("#waiting").hide();
            rtn = false;
        }
    });
}

function closeDialog(){
    $('#dialogDiv').html("");
    $('#dialogDiv').dialog('close');
}

function submitForm(formId, url){
    $.ajax({
        url:url,
        type:"post",
        async:false,
        dataType:"json",
        data:$("#"+formId).serialize(),
        success: function (data) {
            if(data.success)
            {
                onHashChanged();
                //window.location.reload(true);
            }
            else
            {
                alert(data.emMessage);
            }
        },
        error: function(data) {
            alert(data);
        }
    });
}

function getCreateEventSourceForm(){
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

function estemplateChange(oid){
    var selectedId = $("#"+oid).children('option:selected').val();
    getConnectInfoForm(selectedId)
    var mt = $("#modal").outerHeight()/2;
    if(mt+100 > window.outerHeight/2)
    {
        mt=$(body).innerHeight()/2-100;
    }
    $("#modal").css({"margin-top":mt*(-1)+"px"});
}

function getConnectInfoForm(eventSourceTemplateId) {
    $.ajax({
        url:"/eventsources/getconnectinfo?templateId="+eventSourceTemplateId,
        type:"get",
        async:false,
        dataType:"text",
        success:function(data){
            $('#connectInfoDiv').html(data);
        }
    });
}

function getAddSubscriber2ESForm(eventSourceId){
    $.ajax({
        url:"/eventsources/getAddSubscriberForm?eventSourceId="+eventSourceId,
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

function removeSubscriberFromES(eventSourceId,subscriberId){
    $.ajax({
        url:"/eventsources/removeSubscriber",
        type:"post",
        async:false,
        dataType:"json",
        data:{eventSourceId:eventSourceId, subscriberId:subscriberId},
        success: function (data) {
            if(data.success)
            {
                window.location.reload(true);
            }
            else
            {
                alert(data.emMessage);
            }
        },
        error: function(data) {
            alert(data);
        }
    });
}

function getEditESForm(eventSourceId){
    $.ajax({
        url:"/eventsources/getUpdateEventSourceForm?eventSourceId="+eventSourceId,
        type:"get",
        async:true,
        dataType:"text",
        beforeSend: function (XMLHttpRequest) {
            $("#waiting").show();
        },
        success: function (data) {
            alert(data);
            $("#waiting").hide();
            $('#dialogDiv').html(data);
            $('#dialogDiv').dialog({ autoOpen: true, modal: true, width: (small ? 345 : 690), show: "drop", hide: "drop", position: [295, 40] });
        },
        error: function (data) {
            alert(data);
            $("#waiting").hide();
            rtn = false;
        }
    });

}

function offlineEventSource(eventSourceId){
    $.ajax({
        url:"/eventsources/offline?eventSourceId="+eventSourceId,
        type:"post",
        async:false,
        dataType:"json",
        success: function (data) {
            if(data.success)
            {
                onHashChanged();
                //window.location.reload(true);
            }
            else
            {
                alert(data.emMessage);
            }
        },
        error: function(data) {
            alert(data);
        }
    });
}

function deleteEventSource(eventSourceId){
    $.ajax({
        url:"/eventsources/delete?eventSourceId="+eventSourceId,
        type:"post",
        async:false,
        dataType:"json",
        success: function (data) {
            if(data.success)
            {
                onHashChanged();
                //window.location.reload(true);
            }
            else
            {
                alert(data.emMessage);
            }
        },
        error: function(data) {
            alert(data);
        }
    });
}

function getAddES2SubscriberForm(subscriberId){
    $.ajax({
        url:"/subscribers/getAddEventSourceForm?subscriberId="+subscriberId,
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

function getEditSubscriberForm(subscriberId){
    $.ajax({
        url:"/subscribers/getUpdateSubscriberForm?subscriberId="+subscriberId,
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
            alert(data);
            $("#waiting").hide();
            rtn = false;
        }
    });

}

function offlineSubscriber(subscriberId){
    $.ajax({
        url:"/subscribers/offline?subscriberId="+subscriberId,
        type:"post",
        async:false,
        dataType:"json",
        success: function (data) {
            if(data.success)
            {
                onHashChanged();
                //window.location.reload(true);
            }
            else
            {
                alert(data.emMessage);
            }
        },
        error: function(data) {
            alert(data);
        }
    });
}

function deleteSubscriber(subscriberId){
    $.ajax({
        url:"/subscribers/delete?subscriberId="+subscriberId,
        type:"post",
        async:false,
        dataType:"json",
        success: function (data) {
            if(data.success)
            {
                onHashChanged();
                //window.location.reload(true);
            }
            else
            {
                alert(data.emMessage);
            }
        },
        error: function(data) {
            alert(data);
        }
    });
}