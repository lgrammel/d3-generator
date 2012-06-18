<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html lang="en">
<head>
<%= request.getAttribute("mixpanelScript") %>
    <title>d3.js chart generator</title>

    <meta charset="utf-8">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=9">

    <link href="css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
        body {
            padding-top: 60px;
            padding-bottom: 40px;
        }

        #chart {
            border: 1px solid gray;
            width: 100%;
        }

        #inputCSV {
            width: 100%;
        }

        #jsonEditor {
            position: relative;
            width: 100%;
            height: 300px;
        }

        #csvEditor {
            position: relative;
            width: 100%;
            height: 300px;
        }

        #sourceEditor {
            position: relative;
            width: 100%;
            height: 300px;
        }

    </style>
    <link href="css/bootstrap-responsive.css" rel="stylesheet">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- fav and touch icons
    <link rel="shortcut icon" href="../assets/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="../assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="../assets/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="../assets/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="../assets/ico/apple-touch-icon-57-precomposed.png">
    -->

</head>
<body>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a class="brand" href="#">d3.js bar chart generator</a>
        </div>
    </div>
</div>

<div class="container-fluid">
    <div class="row-fluid">
        <div class="span6">
            <h2>CSV</h2>
            <p>Paste your CSV data here. <a href="https://github.com/mbostock/d3/wiki/CSV">Learn more about d3 CSV parsing...</a></p>
            <div id="csvEditor"></div>
        </div>
        <div class="span6">
            <h2>Bar Chart Settings</h2>
            <p>Configure your chart.</p>
            <div class="alert" id="warning-no-columns" style="display: none;">
                <strong>No data available!</strong> Enter some CSV data first.
            </div>
            <div class="alert" id="warning-no-numerical-columns" style="display: none;">
                <strong>No numerical columns available!</strong> Add a numerical column to the CSV file to render a bar chart.
            </div>
            <form class="form-horizontal">
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="inputCategoryColumn">Bar label/grouping</label>
                        <div class="controls">
                            <select id="inputCategoryColumn">
                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="inputMeasureColumn">Bar length</label>
                        <div class="controls">
                            <select id="measureOperation">
                                <option value='all'>all values</option>
                                <option value='sum'>sum</option>
                            </select>
                            <select id="inputMeasureColumn">
                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">Order</label>
                        <div class="controls">
                            <label class="radio">
                                <input id="orderOriginal" type="radio" name="order" value="ORIGINAL" checked="true"/>
                                Original
                            </label>
                            <label class="radio">
                                <input id="orderValueAscending" type="radio" name="order" value="VALUE_ASCENDING" />
                                Bar length (ascending)
                            </label>
                            <label class="radio">
                                <input id="orderValueDescending" type="radio" name="order" value="VALUE_DESCENDING" />
                                Bar length (descending)
                            </label>
                            <label class="radio">
                                <input id="orderLabelAlphabetical" type="radio" name="order" value="LABEL_ALPHABETICAL" />
                                Bar label (alphabetical)
                            </label>
                        </div>
                    </div>
                    <div class="form-actions">
                        <a onclick="generateChartButtonClicked()" class="btn btn-primary" id="buttonGenerateCode">
                            <i class="icon-picture icon-white"></i>
                            Generate Chart
                        </a>
                    </div>
                </fieldset>
            </form>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span6">
            <h2>Parsed Data</h2>
            <p>Read-only, displayed as JSON. Available as <code>data</code> in the source code.</p>
            <div id="jsonEditor">[]</div>
        </div>
        <div class="span6">
            <h2>Source Code</h2>
            <p>The chart automatically updates when you change the source.</p>
            <div id="sourceEditor"></div>
        </div>
     </div>
    <div class="row-fluid">
        <div class="span12">
            <h2>Chart</h2>
            <div id="chart"></div>
        </div>
    </div>
    <footer>
        <p>&copy; <a href="http://www.larsgrammel.de">Lars Grammel</a> 2012</p>
    </footer>
</div>

<!-- Javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="js/underscore/underscore-min.js"></script>
<script src="js/d3/d3.v2.js"></script>
<script src="js/ace/ace.js" type="text/javascript" charset="utf-8"></script>
<script src="js/ace/mode-javascript.js" type="text/javascript" charset="utf-8"></script>
<script src="js/ace/mode-json.js" type="text/javascript" charset="utf-8"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script>

    window.data = []

    updateChartGeneratorState();

    function generateChartButtonClicked() {
        callCodeGenerator(
            $('#inputCategoryColumn').val(),
            $('#inputMeasureColumn').val(),
            $('input[name=order]:checked').val(),
            $('#measureOperation').val()
        );
    }

    function callCodeGenerator(categoryColumn, measureColumn, orderColumn, measureOperation) {
        hasOrder = !("" === orderColumn || undefined === orderColumn);

        mixpanel.track("generate_chart", {
            'order': hasOrder,
            'measureOperation': measureOperation
        });

        url = '/generator/barchart'
                + '?categoryColumn=' + categoryColumn
                + '&measureColumn=' + measureColumn
                + '&measureOperation=' + measureOperation;

        if (hasOrder) {
            url += '&orderColumn=' + orderColumn;
        }

        d3.text(url, function(generatedCode) {
            $('#chart').empty();
            window.sourceEditor.getSession().setValue(generatedCode);
            window.sourceEditor.resize();
            redrawChart();
        });
    }

    function parseCsv(csv) {
        window.data = d3.csv.parse(csv);
        window.jsonEditor.getSession().setValue(JSON.stringify(window.data, undefined, 2));
        updateChartGeneratorState();
    }

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
        $('#chart').empty();

        try {
            var code = window.sourceEditor.getSession().getValue();
            eval(code);
        }
        catch (error) {
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
    window.csvEditor.getSession().on('change', function() {
        parseCsv(window.csvEditor.getSession().getValue());
        window.csvEditor.resize();
    });

    window.onResize = function() {
        window.csvEditor.resize();
        window.jsonEditor.resize();
        window.sourceEditor.resize();
    };
</script>
</body>
</html>