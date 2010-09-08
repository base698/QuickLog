// This will be the google maps/graph button
(function($) {
    $.fn.toggleSwitch = function(options) {
        var showMap = false;
        var defaults = {};
        var options = $.extend(defaults, options);  
	$(this).addClass("chartButton");
	showCurrentChart(jumpsToShow[$(this).attr("id")]);
        var currentDisplay = $(this).attr("id"); 

	$(this).mouseover(function() { $(this).addClass("iconHover"); });
        $(this).mouseout(function() { $(this).removeClass("iconHover"); });

	$(this).click(function() {
	  showMap = !showMap;
          if(showMap) {
             doMap(jumpsToShow[currentDisplay].timePoints);
             $(this).addClass("mapIcon");
          } else {
	     showCurrentChart(jumpsToShow[currentDisplay]);
             $(this).removeClass("mapIcon");
          }
        });

    };

})(jQuery);