// This will be the google maps/graph button
(function($) {
    var switches = [];
    $.fn.toggleSwitch = function(options) {
        var showMap = false;
        var defaults = {};
        var options = $.extend(defaults, options);
        switches.push($(this));
        removeSelectedClass();
	$(this).addClass("chartButton");
        $(this).addClass("dataSelected");
	showChart(jumpsToShow[$(this).attr("id")]);
        var currentDisplay = $(this).attr("id"); 

	$(this).mouseover(function() { 
            drawStateOver($(this),showMap);  
	});

        $(this).mouseout(function() { 
            drawStateOut($(this),showMap);  
        });

	$(this).click(function() {
	  removeSelectedClass();
          $(this).addClass("dataSelected");
	  showMap = !showMap;
          if(showMap) {
             doMap(jumpsToShow[currentDisplay].timePoints);
             $(this).addClass("mapIcon");
          } else {
	     showChart(jumpsToShow[currentDisplay]);
             $(this).removeClass("mapIcon");
          }
          drawStateOut($(this),showMap);
        });

    };
    var removeSelectedClass = function() {
	  for(var i = 0; i<switches.length; i++) {
	      switches[i].removeClass("dataSelected");
	  }
    };    
    var drawStateOver = function(obj,showMap) {
       obj.addClass("iconHover");
       if(showMap) {
	  obj.removeClass("mapIcon");
       } else {
	  obj.addClass("mapIcon");
       }
    };

    var drawStateOut = function(obj, showMap) {
       obj.removeClass("iconHover");
       if(!showMap) {
	  obj.removeClass("mapIcon");
       } else {
	  obj.addClass("mapIcon");
       }
 
    };
})(jQuery);