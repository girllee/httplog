/**
 * Created on 12/12/16.
 * @author Asin Liu
 *
 */



/**
 * Fetch the parameter of the given url.
 * @param name
 * @returns {*}
 */
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = location.search.substr(1).match(reg);
    if (r) {
        return decodeURI(r[2]);
    }
    return null;
}


/**
 * Add event to dom element.
 */
var addEvent = (function () {
    if (document.addEventListener) {
        return function (el, type, fn) {
            if (el.length) {
                for (var i = 0; i < el.length; i++) {
                    addEvent(el[i], type, fn);
                }
            } else {
                el.addEventListener(type, fn, false);
            }
        };
    } else {
        return function (el, type, fn) {
            if (el.length) {
                for (var i = 0; i < el.length; i++) {
                    addEvent(el[i], type, fn);
                }
            } else {
                el.attachEvent('on' + type, function () {
                        return fn.call(el, window.event);
                    }
                )
                ;
            }
        };
    }
})();


