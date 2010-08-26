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
        .dropzone { width: 700px; height: 100px; border: solid 1px; margin: 0 auto;}
        .loaded { margin: 0 auto; }
        </style>
        
        <script type="text/javascript" src="/QuickLog/js/jquery-1.4.2.js"></script>
        <script type="text/javascript" src="../js/highcharts.js"></script>
        <script type="text/javascript" src="../js/charts.js"></script>
		<script type="text/javascript">
		$(document).ready(function(){
			
			$('.dropzone').click(function() {
  				$.post('../gps', function(data) {
  				     var glideSeries = getSeries(data,'glideRatio');
  				     
  				     var vSeries = getSeries(data,'verticalSpeed');
  				     var xAxis = getXAxis(data,'elapsedTime');
  				     glideSeries.yAxis = 0;
  				     doLineChart('chart',xAxis,[glideSeries,vSeries]);
 					 showTotals(data);
				});
			});
			
		});
		
		function showTotals(data) {
		   var html;
		   for(var i = 0; i<data.length;i++) {
		      
		   }
		   
		   $('.loaded').html("<font color=\"blue\">Here</font>");
		}
		</script>
    </head>
    <body>
    <h2>QuickLog</h2>
    <input type="file"/>
    <div id="dropzone" class="dropzone"></div>
    <div id="loaded" class="loaded"></div>
    <hr class="line"/>
    <div id="chart" class="chart"></div>
    </body>
</html>







<