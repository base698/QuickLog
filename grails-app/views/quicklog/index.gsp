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
    .chart { width: 750px; height: 450px; margin: 0 auto; border: 2px solid; }
    .chartButton { display: inline; width: 45px; height: 40px; background: blue; 
    margin: 3px; border:2px solid; color: white; padding: 3px; cursor: pointer; cursor: hand; }
    .loaded { position: absolute; width:300px; height:100px; padding: 5px 5px 5px 5px; top: 10px; right: 10px; border: 1px solid; }
    .chartsActive { height: 30px; margin: 0 auto; }
    p { padding-left: 20px; font-family: arial; color: #333333; font-size: 14px; }
    .dropOver { border: 3px dotted; background: #EDEDED;}
    </style>

    <script type="text/javascript" src="/QuickLog/js/jquery-1.4.2.js"></script>
    <script type="text/javascript" src="../js/highcharts.js"></script>
    <script type="text/javascript" src="../js/charts.js"></script>
    <script type="text/javascript"
       src="http://maps.google.com/maps/api/js?sensor=true"></script>
	    <script type="text/javascript">

	    $(document).ready(function(){		
		    $('#chart').mouseover(function() { $(this).addClass('dropOver'); });
		    $('#chart').mouseout(function() { $(this).removeClass('dropOver'); });
                    $('#map').click(function() {
                        chartClickCount++;
                        if((chartClickCount%2)==0) {
                           showCurrentChart(jumpsToShow[currentDisplay]);
                        } else {
                           doMap(jumpsToShow[currentDisplay].timePoints);
                        }
                    });

		    $('#action').click(function() { 
		       $('#file_upload_form').submit(); 
	               interval = setInterval(getDataFunction,1000);
		    });

	            $('#demo').click(function() {
	               document.file_upload_form.reset();
                       $('#file_upload_form').submit(); 
	               interval = setInterval(getDataFunction,1000);
                    });
	    });

	    var jumpsToShow = [];
	    var interval = 0;
	    var exCount = 0;
            var chartClickCount = 0;
            var currentDisplay = 0;
	    function getDataFunction() {
		var frame = window.frames["upload_target"].document;
		var text = frame.firstChild.innerText;
                chartClickCount = 0;
		try {
		       var data = eval(text);
		       showCurrentChart(data[0]);
		       var start = jumpsToShow.length;
		       for( var i = 0; i < data.length; i++) {
			      var index = i+start;
			  $("#chartsActive").append("<div id=\"" + index + "\">" + index + "</div>");
			  $("#"+index).addClass("chartButton").attr("id",index)
			  .click(function() {
				    showCurrentChart(jumpsToShow[$(this).attr("id")]);
                                    currentDisplay = $(this).attr("id"); 
				 });
		       }
		       jumpsToShow = jumpsToShow.concat(data);
		       // XXX Likely prone to a race condition.
		       clearInterval(interval);
		    } catch(ex) {
		       if(exCount++ > 5) {
			  clearInterval(interval);
			  exCount = 0;
		       }
		    }
	    }

	    function showCurrentChart(data) {
		var glideSeries = getSeries(data.timePoints,'glideRatio');
		var vSeries = getSeries(data.timePoints,'verticalSpeed');
		var xAxis = getXAxis(data.timePoints,'elapsedTime');
		glideSeries.yAxis = 0;
		doLineChart('chart',xAxis,[glideSeries,vSeries]);
		showTotals(data);
	    }

    function showTotals(data) {
	var fMin,fMax,fAvg,cMin,cMax,cAvg;
	var html = "<p>Freefall: min: {0} max: {1} avg: {2}<br/>" +
		 "Canopy: min: {3} max: {4} avg: {5}<br/>" + 
		  "Exit: <b>{6}</b> Opening: <b>{7}</b><br/>" + 
		  "</p>";
     //  html += "Time: " + data[data.length-1].elapsedTime + " seconds</p>";

       html = format(html,[data.fMin,data.fMax,data.fAvg,data.cMin,data.cMax,data.cAvg,data.exitAltitude,data.openingAltitude]);
       $('.loaded').html(html);
    }

    function format(formatStr,args) {
       for(var i = 0; i<args.length;i++) {
	 var regEx = new RegExp("\\{" + i + "\\}","g");
	 formatStr = formatStr.replace(regEx, args[i]);
       }
       return formatStr;
    }
    
    function doMap(mapData) {
      var centerIdx = Math.round(mapData.length/2);

      var centerPt = new google.maps.LatLng(mapData[centerIdx].lat,mapData[centerIdx].lon);
      var myOptions = {
        zoom: 14,
        center: centerPt,
        mapTypeId: google.maps.MapTypeId.ROADMAP
      };


      var map = new google.maps.Map(document.getElementById("chart"),
          myOptions);
      for(var i = 0; i<mapData.length; i++) {
         var myLatLng = new google.maps.LatLng(mapData[i].lat, mapData[i].lon);
         var marker = new google.maps.Marker({
           position: myLatLng,
           map: map,
//           icon: image
           });
      }
   
    }

</script>
</head>
<body>
<h2>QuickLog</h2>
<form id="file_upload_form" name="file_upload_form" method="post" enctype="multipart/form-data" target="upload_target" action="../gps.json">
<div>
    <input name="gpx" id="gpx" size="27" type="file" class="inline"/><br />
    <input type="button" id="action" value="Go" class="inline"/><br />
    <input type="button" id="demo" value="Demo" class="inline"/>
    <input type="button" id="map" value="Map" class="inline"/>
</div>
</form>


<div id="chartsActive" class="chartsActive"></div>
<div id="chart" class="chart"></div>
<div id="loaded" class="loaded"></div>
<iframe name="upload_target" style="width:1;height:1;border:0px solid #fff;"></iframe>  
</body>
</html>
