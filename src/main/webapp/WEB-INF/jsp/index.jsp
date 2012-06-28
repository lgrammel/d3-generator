<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html lang="en">
<head>
    <script type='text/javascript'><%= request.getAttribute("errorHandlerScript") %></script>
    <script type='text/javascript'><%= request.getAttribute("mixpanelScript") %></script>
    <title>Generate D3 Bar Chart Source Code - D3.js Visualization Creator</title>

    <meta charset="utf-8">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=9">

    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
    <link href="css/d3-generator.css" rel="stylesheet">

    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
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
            <ul class="nav">
                <li><a href="#">Top</a></li>
                <li><a href="#configuration">CSV / Configuration</a></li>
                <li><a href="#source">Source Code / Chart</a></li>
            </ul>
        </div>
    </div>
</div>

<div class="container-fluid">
    <div id="introduction">
    <h2>Get started with developing custom D3 bar charts in seconds!</h2>
    <p>
        <a href="http://d3js.org/">D3.js</a> is an awesome JavaScript library that allows you to create
        <a href="https://github.com/mbostock/d3/wiki/Gallery">custom data visualizations</a>.
        This code generator helps you get off the ground quickly by automatically producing source code templates
        for various bar chart configurations.
        It uses the <a href="http://d3js.org/">D3</a> and <a href="http://underscorejs.org/">Underscore</a> JavaScript
        libraries.
        The generated source code is loosely based on
        <a href="http://mbostock.github.com/d3/tutorial/bar-1.html">Mike Bostock's D3 bar chart tutorial</a>.
    </p>
    </div>

    <div style="position:relative; top: -40px;"><a name="configuration">&nbsp</a></div>
    <div class="row-fluid">
        <div class="span6" id="csv">
            <h2>1. Data Editor</h2>
            <p>Paste your CSV data here. <a href="https://github.com/mbostock/d3/wiki/CSV">Learn more about d3 CSV parsing...</a></p>
            <div id="csvEditor"></div>
        </div>
        <div class="span6">
            <h2>2. Bar Chart Configuration</h2>
            <p>Configure your chart and press <code>Generate Chart</code> to load the source code.</p>
            <div class="alert alert-error" id="warning-no-columns" style="display: none;">
                <strong>No data available!</strong> Enter some CSV data first.
            </div>
            <div class="alert alert-error" id="warning-no-numerical-columns" style="display: none;">
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
                            <select class="input-small" id="measureOperation">
                                <option value='all'>all values</option>
                                <option value='sum'>sum</option>
                            </select>
                            <select class="input-medium" id="inputMeasureColumn">
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
                    <div class="form-actions" id="generateButtonSection">
                        <a onclick="generateChartButtonClicked()" class="btn btn-primary btn-large" id="buttonGenerateCode">
                            <i class="icon-picture icon-white"></i>
                            Generate Chart
                        </a>
                    </div>
                </fieldset>
            </form>
            <div class="alert alert-block alert-error" style="display:none;" id="sourceCodeOverrideAlert">
                <h4 class="alert-heading">This will override your source code edits!</h4>
                <p>Are you sure?</p>
                <p>
                    <a class="btn btn-danger" onclick="doCallCodeGenerator()">Yes, override my changes</a>
                    <a class="btn" onclick="abortCodeGeneration()">Not for now - I like my changes</a>
                </p>
            </div>
        </div>
    </div>
    <div style="position:relative; top: -40px;"><a name="source">&nbsp</a></div>
    <div class="row-fluid">
        <div class="span6" id="source">
            <h2>3. Source Code Editor</h2>
            <a  class="btn btn-primary disabled" id="htmlExportButton" onclick="exportButtonClicked()"
                style="float:right; margin-left: 20px;"><i class="icon-file icon-white"></i>Export HTML...</a>
            <p>
                The chart is rendered in the DOM element with the id <code>chart</code>. It automatically updates when
                you modify the source code. You can use
                <code>d3</code> (<a href="http://d3js.org/">d3.js</a>),
                <code>_</code> (<a href="http://underscorejs.org/">underscore.js</a>) and
                <code>$</code> (<a href="http://jquery.com/">JQuery</a>).
            </p>
            <div id="sourceEditor"></div>
        </div>
        <div class="span6">
            <h2>4. Preview</h2>
            <div id="chart" style="display:none;">
            </div>
            <div class="alert alert-error" id="renderError" style="display:none;">
                <strong>Chart could not be rendered! Check your JavaScript source code.</strong>
            </div>
            <div class="alert" id="noChartYet">
                <strong>No chart generated yet!</strong>
            </div>
        </div>
    </div>
    <hr/>
    <footer id="footer">
        <p>D3-Generator.com &copy; <a href="http://www.larsgrammel.de">Lars Grammel</a> 2012</p>
    </footer>
</div>
<div class="modal hide fade" style="width: 80%; margin-left: -40%;" id="htmlExportDialog">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">×</button>
        <h3>HTML Export</h3>
    </div>
    <div class="modal-body">
        <a class="btn btn-primary">Copy to clipboard</a>
        <a class="btn btn-primary">Open HTML in separate window</a>
        <p>
            <pre id="exportHtml">
            </pre>
        </p>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal">Close</a>
    </div>
</div>

<script src="js/underscore/underscore.js"></script>
<script src="js/d3/d3.v2.js"></script>
<script src="js/browserdetect/browserdetect.js"></script>
<script src="js/ace/ace.js"></script>
<script src="js/ace/mode-javascript.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script src="js/bootstrap/bootstrap-modal.js"></script>
<script src="js/bootstrap/bootstrap-transition.js"></script>
<script src="js/d3-generator.js"></script>
</body></html>