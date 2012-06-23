errorHandler = {
    onError: function() {
        console.log("onError");
        console.log("this=" + JSON.stringify(this, null, 2));
        console.log("arguments=" + JSON.stringify(arguments, null, 2));
    }
};