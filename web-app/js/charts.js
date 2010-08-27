function doLineChart(container,catagories,seriesData) {

var chart = new Highcharts.Chart({
	chart: {
	renderTo: container,
	margin: [60, 100, 60, 100],
	zoomType: 'xy'
},
title: {
	text: 'Glide and Vertical Speed (MPH) vs Time (s)',
	style: {
		margin: '10px 0 0 0' // center it
	}
},
xAxis: [{
	text: 'Elapsed Time (s)',
	categories: catagories
}],
yAxis: [{ // Primary yAxis
	labels: {
		formatter: function() {
			return this.value  +' MPH';
		},
		style: {
			color: '#A52222'
		}
	},
	title: {
		text: 'Vertical Speed',
		style: {
			color: '#A52222'
		},
		margin: 60
	}
}, { // Secondary yAxis
	title: {
		text: 'Glide Ratio',
		margin: 70,
		style: {
			color: '#4572A7'
		}
	},
	labels: {
		formatter: function() {
			return this.value;
		},
		style: {
			color: '#4572A7'
		}
	},
	opposite: true
}],
tooltip: {
	formatter: function() {
		return ''+
			this.x +': '+ this.y +
			(this.series.name == 'Glide Ratio' ? '' : ' MPH');
	}
},
legend: {
	layout: 'vertical',
	style: {
		left: '120px',
		bottom: 'auto',
		right: 'auto',
		top: '100px'
	},
	backgroundColor: '#FFFFFF'
},
series: [{
	name: 'Glide Ratio',
	color: '#4572A7',
	type: 'spline',
	yAxis: 1,
	data: seriesData[0]		

}, {
	name: 'Vertical Speed',
	color: '#A52222',
	type: 'spline',
	data: seriesData[1]
}]
});
}

function getSeries(data,prop) {
	var ySeries = [];
	
   for(var i=0;i<data.length;i++) {
	  ySeries.push(data[i][prop]);   
   }
   
   return ySeries;
}

function getXAxis(data,prop) {
   var list = [];
   
	for(var i = 0; i<data.length;i++) {
	  list.push(data[i][prop]);   
   }
   
   return list;
}
