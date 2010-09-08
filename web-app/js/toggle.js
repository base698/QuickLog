// This will be the google maps/graph button
(function($) {
    $.fn.toggleSwitch = function(options) {
        var showMap = false;
        var defaults = {};
        var options = $.extend(defaults, options);  
	$(this).addClass("chartButton");
	showCurrentChart(jumpsToShow[$(this).attr("id")]);
        var currentDisplay = $(this).attr("id"); 
        
	$(this).click(function() {
	  showMap = !showMap;
          if(showMap) {
             doMap(jumpsToShow[currentDisplay].timePoints);
          } else {
	     showCurrentChart(jumpsToShow[currentDisplay]);
          }
        });

    };

})(jQuery);