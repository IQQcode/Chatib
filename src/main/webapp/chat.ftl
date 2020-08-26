<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <title>Chatlib</title>


    <link rel="stylesheet" href="assets/css/bootstrap.css"/>
    <link rel="stylesheet" href="assets/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="assets/css/build.css"/>
    <link rel="stylesheet" type="text/css" href="assets/css/qq.css"/>

</head>
<body>

<div class="qqBox">
    <div class="context">
        <div class="conLeft">
            <ul id="userList">

            </ul>
        </div>
        <div class="conRight">
            <div class="Righthead">
                <div class="headName">${username}</div>
            </div>
            <div class="RightCont">
                <ul class="newsList" id="message">

                </ul>
            </div>
            <div class="RightMiddle">
                <div class="file">
                    <form id="form_photo" method="post" enctype="multipart/form-data"
                          style="width:auto;">
                        <input type="file" name="filename" id="filename" onchange="fileSelected();"
                               style="display:none;">
                    </form>
                </div>
            </div>
            <div class="RightFoot">
                    <textarea id="dope"
                              style="width: 100%;height: 100%; border: none;outline: none;padding:20px 0 0 3%;" name=""
                              rows="" cols=""></textarea>
                <button id="fasong" class="sendBtn" onclick="send()" style="border-radius: 5px">发送</button>
            </div>
        </div>
    </div>
</div>


<script src="assets/js/jquery_min.js"></script>
<script src="https://cdn.bootcss.com/jquery.form/4.2.2/jquery.form.min.js"></script>
<script type="text/javascript">
    var webSocket = null;

    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        webSocket = new WebSocket('ws://127.0.0.1:8080/websocket?username=' + '${username}');
    } else {
        alert("当前浏览器不支持WebSocket");
    }

    //连接发生错误的回调方法
    webSocket.onerror = function () {
        setMessageInnerHTML("WebSocket连接发生错误！");
    };

    webSocket.onopen = function () {
        setMessageInnerHTML("WebSocket连接成功！")
    };

    webSocket.onmessage = function (event) {

        $("#userList").html("");
        eval("var msg=" + event.data + ";");

        if (undefined != msg.content)
            setMessageInnerHTML(msg.content);

        if (undefined != msg.names) {
            $.each(msg.names, function (key, value) {
                var htmlstr = '<li>'
                        + '<div class="checkbox checkbox-success checkbox-inline">'
                        + '<input type="checkbox" class="styled" id="' + key + '" value="' + key + '" checked>'
                        + '<label for="' + key + '"></label>'
                        + '</div>'
                        + '<div class="liLeft"><img src="assets/img/robot2.jpg"/></div>'
                        + '<div class="liRight">'
                        + '<span class="intername">' + value + '</span>'
                        + '</div>'
                        + '</li>'

                $("#userList").append(htmlstr);
            })
        }
    };

    webSocket.onclose = function () {
        setMessageInnerHTML("WebSocket连接关闭");
    };

    window.onbeforeunload = function () {
        closeWebSocket();
    };

    function closeWebSocket() {
        webSocket.close();
    }

    function send() {
        var time = new Date().toLocaleString();
        var message = $("#dope").val();
        $("#dope").val("");

            //发送消息
            var htmlstr = '<li><div class="answerHead"><img src="assets/img/2.png"></div><div class="answers">'
                    + '[本人]' + '   ' + time + '<br/>' + message + '</div></li>';
            webSocketSend(htmlstr,message,"");
    };

    function webSocketSend(htmlstr,message,re){
        $("#message").append(htmlstr);
        var ss = $("#userList :checked");
        var to = "";
        $.each(ss, function (key, value) {
            to += value.getAttribute("value") + "-";
        });
        console.info(to);

        if (ss.size() == 0) {
            var obj = {
                msg: message,
                type: 1
            }
        } else {
            var obj = {
                to: to,
                msg: message,
                type: 2
            }
        }
        var msg = JSON.stringify(obj);
        webSocket.send(msg);

        if(re){
            $("#jsonImg").attr("src", string.data);
            // loadDiv(re);
        }

    }

    //回车键发送消息
    $(document).keypress(function (e) {

        if ((e.keyCode || e.which) == 13) {
            $("#fasong").click();
        }

    });

    //局部刷新div
    function loadDiv(sJ) {
        $("#delayImgPer").html('<img src="'+sJ+'" class="delayImg" >');
    }

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {
            var msg = '<li>'
                    + '<div class="nesHead">'
                    + '<img src="assets/img/robot.jpg">'
                    + ' </div>'
                    + '<div class="news">'
                    + innerHTML
                    + '</div>'
                    + '</li>';
        $("#message").append(msg);

    }
</script>

</body>
</html>