var createErrorHandlingWrapper = function(context, delegate) {
    return function() {
        try {
            delegate.apply(context, arguments);
        } catch (ex) {
            errorHandler.onError(ex);
        }
    };
};

mixpanel.track = createErrorHandlingWrapper(mixpanel, mixpanel.track);
mixpanel.track_funnel = createErrorHandlingWrapper(mixpanel, mixpanel.track_funnel);
mixpanel.register = createErrorHandlingWrapper(mixpanel, mixpanel.register);
mixpanel.register_once = createErrorHandlingWrapper(mixpanel, mixpanel.register_once);
mixpanel.register_funnel = createErrorHandlingWrapper(mixpanel, mixpanel.register_funnel);
mixpanel.identify = createErrorHandlingWrapper(mixpanel, mixpanel.identify);

var createThrottledTracker = function(milliseconds, trackingFunction, enabled) {

    var hasBeenTracked = false;

    var resetTrackingStatus = function() {
        hasBeenTracked = false;
    }

    return {
        'track': function() {
            if (hasBeenTracked || !enabled) {
                return;
            }

            hasBeenTracked = true;
            trackingFunction();
            setInterval(resetTrackingStatus, milliseconds);
        },

        'setEnabled': function(newState) {
            enabled = newState;
        }
    };
}

var csvChangeTracker = createThrottledTracker(60 * 1000, function() {
    mixpanel.track("csv_change");
}, false);

mixpanel.track("pageload", {
    'URL': window.location.href,
    'Browser + Version' : BrowserDetect.browser + " " + BrowserDetect.version,
    'Screen Resolution' : screen.width + " x " + screen.height
});

window.data = [];
window.editorContent = "";

updateChartGeneratorState();

function generateChartButtonClicked() {
    $('#generateButtonSection').hide();
    if (window.editorContent != window.sourceEditor.getSession().getValue()) {
        $('#sourceCodeOverrideAlert').show();
    } else {
        doCallCodeGenerator();
    }
}

function abortCodeGeneration() {
    $('#sourceCodeOverrideAlert').hide();
    $('#generateButtonSection').show();
}

function doCallCodeGenerator() {
    $('#sourceCodeOverrideAlert').hide();
    callCodeGenerator(
        $('#inputCategoryColumn').val(),
        $('#inputMeasureColumn').val(),
        $('input[name=order]:checked').val(),
        $('#measureOperation').val()
    );
}

function callCodeGenerator(categoryColumn, measureColumn, orderColumn, measureOperation) {
    mixpanel.track("generate_chart", {
        'order': orderColumn,
        'measureOperation': measureOperation
    });

    var url = '/generator/barchart'
        + '?categoryColumn=' + categoryColumn
        + '&measureColumn=' + measureColumn
        + '&measureOperation=' + measureOperation
        + '&orderColumn=' + orderColumn;

    d3.text(url, function(generatedCode) {
        window.editorContent = generatedCode;
        $('#chart').empty();
        window.sourceEditor.getSession().setValue(generatedCode);
        window.sourceEditor.resize();
        redrawChart();
        $('#generateButtonSection').show();
    });
}

function parseCsv(csv) {
    window.data = d3.csv.parse(csv);
    window.jsonEditor.getSession().setValue(JSON.stringify(window.data, undefined, 2));
    updateChartGeneratorState();
}

function onCsvChange() {
    csvChangeTracker.track();
    parseCsv(window.csvEditor.getSession().getValue());
    window.csvEditor.resize();
};

function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}

function updateChartGeneratorState() {
    if (window.data !== undefined && window.data.length > 0) {
        $('#warning-no-columns').hide();

        var columns = _.map(window.data[0], function(value, key) {
            return [value, key];
        });

        var optionAssembler = function(memo, column) {
            return memo + '<option value="' + column[1] + '">' + column[1] + '</option>';
        };

        var numericColumns = _.filter(columns, function(d) {
            return isNumber(d[0]);
        });

        var allColumnOptions = _.reduce(columns, optionAssembler, '');
        var numericColumnOptions = _.reduce(numericColumns, optionAssembler, '');

        $('#inputCategoryColumn').html(allColumnOptions);
        $('#inputMeasureColumn').html(numericColumnOptions);
        $('#orderColumn').html('<option value="">no order</option>' + numericColumnOptions);

        $('#inputCategoryColumn').attr('disabled', false);

        if (numericColumns.length > 0) {
            $('#inputMeasureColumn').attr('disabled', false);
            $('#measureOperation').attr('disabled', false);

            $('#orderOriginal').attr('disabled', false);
            $('#orderValueAscending').attr('disabled', false);
            $('#orderValueDescending').attr('disabled', false);
            $('#orderLabelAlphabetical').attr('disabled', false);

            $('#buttonGenerateCode').removeClass('disabled');
            $('#warning-no-numerical-columns').hide();
        } else {
            $('#inputMeasureColumn').attr('disabled', true);
            $('#measureOperation').attr('disabled', true);

            $('#orderOriginal').attr('disabled', true);
            $('#orderValueAscending').attr('disabled', true);
            $('#orderValueDescending').attr('disabled', true);
            $('#orderLabelAlphabetical').attr('disabled', true);

            $('#buttonGenerateCode').addClass('disabled');
            $('#warning-no-numerical-columns').show();
        }
    } else {
        $('#inputCategoryColumn').html('');
        $('#inputMeasureColumn').html('');

        $('#inputCategoryColumn').attr('disabled', true);
        $('#inputMeasureColumn').attr('disabled', true);
        $('#measureOperation').attr('disabled', true);

        $('#orderOriginal').attr('disabled', true);
        $('#orderValueAscending').attr('disabled', true);
        $('#orderValueDescending').attr('disabled', true);
        $('#orderLabelAlphabetical').attr('disabled', true);

        $('#buttonGenerateCode').addClass('disabled');

        $('#warning-no-numerical-columns').hide();
        $('#warning-no-columns').show();
    }
}

function redrawChart() {
    var code = window.sourceEditor.getSession().getValue();

    if (code === "") {
        return;
    }

    $('#chart').empty();
    $('#chart').show();
    $('#emptyChart').hide();
    $('#noChartYet').hide();
    $('#renderError').hide();

    try {
        eval('(function() {\n' + code + '\n}());');
    } catch (error) {
        $('#chart').hide();
        $('#renderError').show();
        console.log("error drawing chart");
        console.log(error);
    }
    finally {}
}

var JsonMode = require("ace/mode/json").Mode;
window.jsonEditor = ace.edit("jsonEditor");
window.jsonEditor.getSession().setMode(new JsonMode());
window.jsonEditor.setReadOnly(true);
window.jsonEditor.setShowPrintMargin(false);
window.jsonEditor.getSession().on('change', redrawChart);

var JavascriptMode = require("ace/mode/javascript").Mode;
window.sourceEditor = ace.edit("sourceEditor");
window.sourceEditor.getSession().setMode(new JavascriptMode());
window.sourceEditor.setShowPrintMargin(false);
window.sourceEditor.getSession().on('change', redrawChart);

window.csvEditor = ace.edit("csvEditor");
window.csvEditor.setShowPrintMargin(false);
window.csvEditor.getSession().on('change', onCsvChange);

window.onResize = function() {
    window.csvEditor.resize();
    window.jsonEditor.resize();
    window.sourceEditor.resize();
};

d3.text('data/countries.csv', function(csv) {
    window.csvEditor.getSession().setValue(csv);
    csvChangeTracker.setEnabled(true);
});