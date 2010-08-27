<html>
    <head>
        <title>QuickLog</title>
        <style type="text/css" media="screen">
        h2 {
            font-size:1.2em;
            color: #333333;
        }
        .center { margin: 0 auto; }
        .line { height: 1px; }
        .chart { width: 750px; height: 450px; margin: 0 auto; border: 2px solid; }
   
        .loaded { position: absolute; padding: 5px 5px 5px 5px; top: 10px; right: 10px; border: 1px solid; }
        p { padding-left: 20px; font-family: arial; color: #333333; font-size: 14px; }
        .dropOver { border: 3px dotted; background: #EDEDED;}
        </style>
        
        <script type="text/javascript" src="/QuickLog/js/jquery-1.4.2.js"></script>
        <script type="text/javascript" src="../js/highcharts.js"></script>
        <script type="text/javascript" src="../js/charts.js"></script>
		<script type="text/javascript">

		$(document).ready(function(){		
			$('#chart').mouseover(function() { $(this).addClass('dropOver'); });
			$('#chart').mouseout(function() { $(this).removeClass('dropOver'); });
			$('#action').click(function() { 
			   $('#file_upload_form').submit(); 
			   	interval = setInterval(getDataFunction,1000);
			});		

		});
		

		var interval = 0;
		var exCount = 0;
		function getDataFunction() {
		 	console.log('interval');
			var frame = window.frames["upload_target"].document;
		    var text = frame.firstChild.innerText;
		    try {
  			   var data = eval(text);
  			   console.log(data);
  			   var glideSeries = getSeries(data[0].timePoints,'glideRatio');
  			   var vSeries = getSeries(data[0].timePoints,'verticalSpeed');
  			   var xAxis = getXAxis(data[0].timePoints,'elapsedTime');
  			   glideSeries.yAxis = 0;
  			   doLineChart('chart',xAxis,[glideSeries,vSeries]);
 			   showTotals(data[0].timePoints);
 			   clearInterval(interval);
 			} catch(ex) {
 			   console.log(ex);
 			   if(exCount++ > 5) {
 			      clearInterval(interval);
 			      exCount = 0;
 			   }
 			}
		}
		
		function showTotals(data) {
		   var fMin,fMax,fAvg,cMin,cMax,cAvg;
		   var html = "<p>Freefall: min: {0} max: {1} avg: {2}<br/>" +
		             "Canopy: min: {3} max: {4} avg: {5}</br>";
		   html += "Time: " + data[data.length-1].elapsedTime + " seconds</p>"
		   for(var i = 0; i<data.length;i++) {
		      
		   }
		   html = format(html,[fMin,fMax,fAvg,cMin,cMax,cAvg]);
		   $('.loaded').html(html);
		}
		
		function format(formatStr,args){
		   console.log(formatStr);
           for(var i = 0; i<args.length;i++) {
             var regEx = new RegExp("\\{" + i + "\\}","g");
             formatStr = formatStr.replace(regEx, args[i]);
           }
           return formatStr;
        }
		</script>
    </head>
    <body>
    <h2>QuickLog</h2>
    <form id="file_upload_form" method="post" enctype="multipart/form-data" target="upload_target" action="../gps">
        <input name="gpx" id="gpx" size="27" type="file" /><br />
        <input type="button" id="action" value="Go" /><br />
        <iframe id="upload_target" name="upload_target" src="" style="width:0;height:0;border:0px solid #fff;"></iframe>
    </form>
    <div id="chart" class="chart"></div>
    <div id="loaded" class="loaded"></div>
  
    </body>
</html>







<