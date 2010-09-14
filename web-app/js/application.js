String.format = function() {
   if(arguments.length == 0) {
       throw new Error("Format requires at least a string.");
   }

   var formatStr = arguments[0];

   for(var i = 1; i<arguments.length;i++) {
      var regEx = new RegExp("\\{" + (i-1) + "\\}","g");
      formatStr = formatStr.replace(regEx, arguments[i]);
   }
   return formatStr;
}

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
  // Setup form if HTML5 Drag/Drop FileReader not available	
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
      getDataFunction.interval = setInterval(getDataFunction,1000);
   });

   $('#demo').click(function() {
     document.file_upload_form.reset();
     $('#file_upload_form').submit(); 
     getDataFunction.interval = setInterval(getDataFunction,1000);
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
var currentDisplay = 0;
getDataFunction.exCount = 0;
getDataFunction.interval = 0;
	    
function getDataFunction(text) {		
    if(!text) {
       text = getTextFromIFrame();
    }

   try {
       var data = eval(text);
       showCurrentChart(data[0]);
       var start = jumpsToShow.length;

       jumpsToShow = jumpsToShow.concat(data);

       for( var i = 0; i < data.length; i++) {
	  var index = i+start;
	  $("#chartsActive").append(String.format('<div id="{0}">&nbsp;</div>',index));
	  $("#"+index).attr("id",index).toggleSwitch();
	}

	clearInterval(getDataFunction.interval);
    } catch(ex) {
       if(console) console.log(ex);
       if(getDataFunction.exCount++ > 5) {
	  clearInterval(getDataFunction.interval);
	  getDataFunction.exCount = 0;
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
	        "";
    html += "Time: {8} seconds</p>";

    html = String.format(html,data.fMin,data.fMax,data.fAvg,data.cMin,data.cMax,data.cAvg,data.exitAltitude,data.openingAltitude);
    $('.loaded').html(html);
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
