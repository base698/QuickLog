var Ajax;
if (Ajax && (Ajax != null)) {
	Ajax.Responders.register({
	  onCreate: function() {
        if($('spinner') && Ajax.activeRequestCount>0)
          Effect.Appear('spinner',{duration:0.5,queue:'end'});
	  },
	  onComplete: function() {
        if($('spinner') && Ajax.activeRequestCount==0)
          Effect.Fade('spinner',{duration:0.5,queue:'end'});
	  }
	});
}

$(document).ready(function(){	
  // Setup HTML5 Drag/Drop File	
  if (typeof window.FileReader === 'undefined') {
     $('#formElements').append('<input name="gpx" id="gpx" size="27" type="file" class="inline"/><br />');
  } else {
      // Setup drag drop.
      var holder = document.getElementById('chart');
      
      holder.addEventListener('dragover', 
            function (e) { 
              e.stopPropagation(); 
              e.preventDefault();  
              return false; },
            false);
      holder.addEventListener('dragend', 
         function (e) { 
            e.stopPropagation(); 
            e.preventDefault(); 
             return false; },false);
      holder.addEventListener('drop', function (e) {
      e.stopPropagation();
      e.preventDefault();

      var file = e.dataTransfer.files[0],
      reader = new FileReader();
      reader.onload = function(e) {
          var frame = window.frames[0].document;
          $.ajax({
            type: 'POST',
            url: '../gps',
	    data: {'gpx':e.target.result},
            success: getDataFunction
          });

      };
      reader.readAsText(file);
      return false;
     },false);
  }

   $('#gpx').change(function() {
      $('#file_upload_form').submit(); 
      interval = setInterval(getDataFunction,1000);
   });

   $('#demo').click(function() {
     document.file_upload_form.reset();
     $('#file_upload_form').submit(); 
     interval = setInterval(getDataFunction,1000);
   });
});

function getTextFromIFrame() {
   var frame = window.frames[0].document;
   var text = frame.firstChild.innerText;
   if(!text) {
       text = frame.firstChild.textContent;
   }
   return text;
}
var jumpsToShow = [];
var interval = 0;
var exCount = 0;
var chartClickCount = 0;
var currentDisplay = 0;
	    
function getDataFunction(text) {		
    if(!text) {
       text = getTextFromIFrame();
    }

   chartClickCount = 0;
   try {
       var data = eval(text);
       showCurrentChart(data[0]);
       var start = jumpsToShow.length;

       jumpsToShow = jumpsToShow.concat(data);

       for( var i = 0; i < data.length; i++) {
	  var index = i+start;
	  $("#chartsActive").append(format('<div id="{0}">&nbsp;</div>',[index]));
	  $("#"+index).attr("id",index).toggleSwitch();
	}

	clearInterval(interval);
    } catch(ex) {
       if(console) console.log(ex);
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
           map: map
//         icon: image
      });
   }
   
}
