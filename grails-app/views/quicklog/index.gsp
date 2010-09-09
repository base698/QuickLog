<!DOCTYPE html>
<html>
<head>
    <title>QuickLog</title>
    <style type="text/css" media="screen">
    h2 {
	font-size:1.2em;
	color: #333333;
    }
    .inline { display: inline; }
    .center { margin: 0 auto; }
    .line { height: 1px; }
    .chart { width: 750px; height: 450px; margin: 0 auto; }
    .chartButton { width: 80px; height: 70px; float:left;  
     border:1px solid #000; margin: 3px; color: white; cursor: pointer; cursor: hand; background: url(../images/chartIcon.png);}
    .mapIcon { background: url(../images/mapIcon.png); }
    .iconHover { opacity: 0.6; }
    .loaded { position: absolute; width:300px; height:100px; padding: 5px 5px 5px 5px; top: 10px; right: 10px; border: 1px solid; }
    .chartsActive { height: 100px; width: 100%;  margin: 0 auto; }
    .chartWrapper { width: 760px; height:460px; margin: 0 auto; border: 2px solid;}
    p { padding-left: 20px; font-family: arial; color: #333333; font-size: 14px; }
    /* add z-index for drawing over components */
    .dropOver { border: 3px dotted; background: #EDEDED;}
    </style>
    
    <script type="text/javascript" src="/QuickLog/js/jquery-1.4.2.js"></script>
    <script type="text/javascript" src="../js/h5utils.js"></script>
    <script type="text/javascript" src="../js/highcharts.js"></script>
    <script type="text/javascript" src="../js/charts.js"></script>
    <script type="text/javascript"
       src="http://maps.google.com/maps/api/js?sensor=true"></script>
    <script type="text/javascript" src="../js/application.js"></script>
    <script type="text/javascript" src="../js/toggle.js"></script>
</head>
<body>
<h2>QuickLog</h2>
<form id="file_upload_form" name="file_upload_form" method="post" enctype="multipart/form-data" target="upload_target" action="../gps">
<div id="formElements">
</div>
</form>

<div id="chartsActive" class="chartsActive"></div>

<div id="chart" class="chart" style="border: 1px solid;"></div>
<div id="loaded" class="loaded"></div>
<!-- XXX Use for drag -->
<input type="button" id="demo" value="Demo" class="inline"/>
<iframe id="upload_target" name="upload_target" style="width:0;height:0;border:0;"></iframe>  

</body>
</html>
