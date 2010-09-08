// This will be the google maps/graph button
(function($) {
    $.fn.toggleSwitch = function(options) {
        var defaults = {};
        var options = $.extend(defaults, options);  
	$(this).addClass("chartButton");
    };

})(jQuery);