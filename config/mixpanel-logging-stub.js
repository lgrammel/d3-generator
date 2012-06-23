var createLogger = function(name) {
    return function() {
        console.log("`" + name + "` called");
        console.log("this=" + JSON.stringify(this, null, 2));
        console.log("arguments=" + JSON.stringify(arguments, null, 2));
    };
};

var mixpanel = {
    track: createLogger('Mixpanel.track'),
    track_funnel: createLogger('Mixpanel.track_funnel'),
    register: createLogger('Mixpanel.register'),
    register_once: createLogger('Mixpanel.register_once'),
    register_funnel: createLogger('Mixpanel.register_funnel'),
    identify: createLogger('Mixpanel.identify')
};