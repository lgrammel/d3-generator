var error_fn = function() {
    throw "mixpanel fake error";
};
var mixpanel = {
    track: error_fn,
    track_links: error_fn,
    track_funnel: error_fn,
    register: error_fn,
    register_once: error_fn,
    register_funnel: error_fn,
    identify: error_fn
};