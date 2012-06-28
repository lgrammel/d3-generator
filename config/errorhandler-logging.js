errorHandler = {
    onError: function(error, message) {
        if (message !== undefined) {
            console.log(message);
        }
        console.log(error);
    }
};