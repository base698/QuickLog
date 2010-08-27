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
        .chart { width: 700px; height: 500px; margin: 0 auto; }
        .loaded { position: absolute; padding: 5px 5px 5px 5px; top: 10px; right: 10px; border: 1px solid; }
        p { padding-left: 20px; font-family: arial; color: #333333; font-size: 14px; }
        .dropOver { border:  1px solid; background: #33AA33;}
        </style>
        
        <script type="text/javascript" src="/QuickLog/js/jquery-1.4.2.js"></script>
        <script type="text/javascript" src="../js/highcharts.js"></script>
        <script type="text/javascript" src="../js/charts.js"></script>
		<script type="text/javascript">
		$(document).ready(function(){
			
			$('.chart').click(function() {
  				$.post('../gps', function(data) {
  				     var glideSeries = getSeries(data,'glideRatio');
  				     
  				     var vSeries = getSeries(data,'verticalSpeed');
  				     var xAxis = getXAxis(data,'elapsedTime');
  				     glideSeries.yAxis = 0;
  				     doLineChart('chart',xAxis,[glideSeries,vSeries]);
 					 showTotals(data);
				});
			});
			
			$('.chart').mouseOver(function() { $(this).addClass('dropOver'); });
			$('.chart').mouseOut(function() { $(this).removeClass('dropOver'); });
			
		});
		
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
             console.log(args[i]);
             var regEx = new RegExp("\\{" + i + "\\}","g");
             formatStr = formatStr.replace(regEx, args[i]);
           }
           return formatStr;
        }
		</script>
    </head>
    <body>
    <h2>QuickLog</h2>
    <form id="file_upload_form" method="post" enctype="multipart/form-data" action="../gps">
        <input name="gpx" id="gpx" size="27" type="file" /><br />
        <input type="submit" name="action" value="Go" /><br />
        <iframe id="upload_target" name="upload_target" src="" style="width:0;height:0;border:0px solid #fff;"></iframe>
    </form>
    <div id="chart" class="chart"></div>
    <div id="loaded" class="loaded"></div>
  
    </body>
</html>







<