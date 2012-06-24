var createLogger = function(name) {
    return function() {
        console.log(name + " (" + JSON.stringify(arguments, null, 2) + ")");
    };
};

var mixpanel = {
    track: createLogger('Mixpanel.track'),
    track_links: createLogger('Mixpanel.track_links'),
    track_funnel: createLogger('Mixpanel.track_funnel'),
    register: createLogger('Mixpanel.register'),
    register_once: createLogger('Mixpanel.register_once'),
    register_funnel: createLogger('Mixpanel.register_funnel'),
    identify: createLogger('Mixpanel.identify')
};