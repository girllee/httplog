/**
 * Created by boot on 12/16/16.
 */


/**
 * @author Asin Liu
 * @type {{}}
 */
var L = L || {};
L.event = {};

L.commandMethod = {
    regExForName: "",
    regExForPhone: "",
    validateName: function (name) {
        console.log(name + " is validated by" + this.regExForName);
    },

    validatePhone: function (phone) {
        console.log(this.regExForPhone);
    }
};

L.event = {
    addListener: function (el, type, fn) {

    },
    removeListener: function (el, type, fn) {

    },
    getEvent: function (e) {

    }
};


L.event.addListener("el", "type", callback);


L.commandMethod.validateName("Asin Liu");
