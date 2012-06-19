<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html lang="en">
<head>
<%= request.getAttribute("mixpanelScript") %>
    <title>d3.js chart generator</title>

    <meta charset="utf-8">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=9">

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
    <link href="css/d3-generator.css" rel="stylesheet">

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
<script src="js/d3/d3.v2.min.js"></script>
<script src="js/browserdetect/browserdetect.min.js"></script>
<script src="js/ace/ace.js"></script>
<script src="js/ace/mode-javascript.js"></script>
<script src="js/ace/mode-json.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script src="js/generator.js"></script>
</body></html>