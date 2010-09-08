// This will be the google maps/graph button
(function($) {
    $.fn.toggleSwitch = function(options) {
        var showMap = false;
        var defaults = {};
        var options = $.extend(defaults, options);  
	$(this).addClass("chartButton");
	showCurrentChart(jumpsToShow[$(this).attr("id")]);
        var currentDisplay = $(this).attr("id"); 

	$(this).mouseover(function() { 
            drawStateOver($(this),showMap);  
	});

        $(this).mouseout(function() { 
            drawStateOut($(this),showMap);  
        });

	$(this).click(function() {
	  showMap = !showMap;
          if(showMap) {
             doMap(jumpsToShow[currentDisplay].timePoints);
             $(this).addClass("mapIcon");
          } else {
	     showCurrentChart(jumpsToShow[currentDisplay]);
             $(this).removeClass("mapIcon");
          }
          drawStateOut($(this),showMap);
        });

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