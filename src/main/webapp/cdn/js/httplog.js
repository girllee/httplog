/**
 * Created on 12/12/16.
 * @author Asin Liu
 * Note: wss:// for https only and ws:// for http.
 *
 */

var control = control || {};

control.maxRow = 2 << 12;
control.contentLenth = 2 << 12;
control.threshold = 2 << 10;
control.autoScroll = true;
control.msgd = true;

// 行计数器变量
var i = 0;
function doEventAction(data) {
    if (msgd) {
        if (++i > control.maxRow) {
            location.reload();
            i = 0;
        }
        if (data == '01') {
            err.innerHTML = "未找到指定的文件！";
        } else {
            var msg = data + "<br>";
            cs.innerHTML += msg;
            var hc = cs.innerHTML;
            if (hc.length > control.contentLenth) {
                cs.innerHTML = hc.substring(control.threshold);
            }
            if (control.autoScroll)
                cs.scrollTop = cs.scrollHeight;
        }
    }
}

function reverseScrollFlag() {
    control.autoScroll = ! control.autoScroll;
    return;
}


function reverseMsgFlag() {
    control.msgd = !control.msgd;
    return;
}

function startWebSocket(wsURI) {
    try {
        var webSocket = new WebSocket(wsURI);
        webSocket.onmessage = function (event) {
            doEventAction(event.data);
        }
    } catch (err) {
        var txt = "There was an error on this page.\r\n";
        txt += "Error description: " + err.toString() + "\r\n";
        txt += "Click OK to continue.\r\n";
        console.error(txt);
    }
}


function startWS(host, filePath, fileName) {
    if (!filePath || !fileName) {
        cs.innerHTML = "url地址传入的参数错误,filePath, fileName参数不能为空！";
        return;
    }
    if (!host) {
        host = location.host;
    }
    //var file = (filePath == null || filePath=="") ? logFileName : logFilePath + "/" + logFileName;

    var file = logFilePath + "/" + logFileName;
    var wsURI;

    var protocol = window.location.protocol;
    if (protocol === 'http:')
        wsURI = 'ws://' + host + '/log/' + file;
    else if (protocol === 'https:')
        wsURI = 'wss://' + host + '/log/' + file;
    else
        console.error('protocol http and https are support, other protocol is not supported.');
    startWebSocket(wsURI);
}