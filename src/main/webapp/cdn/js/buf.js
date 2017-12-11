/**
 * @author Asin Liu
 * Created on 12/15/16.
 *
 * A simple buffer for WebSocket which minus operations of DOM
 */

var msg = "";
var oldMsg = "";
onmessage = function (event) {
    if (event.data.msg){
        if(event.data.msg == oldMsg){
            console.error("The same data is sent");
        }

        msg += event.data.msg + "<br/>";
        // console.log("onmessage from buf. msg length is:" + msg);
        oldMsg = event.data.msg;
    }
};
setInterval(function () {
    console.log("postMessage from buf");
    postMessage(msg);
}, 500);