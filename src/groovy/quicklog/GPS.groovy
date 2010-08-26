package quicklog

import quicklog.util.GPX;

class GPS {
		Double lat;
		Double lon;
		Double elev;
		Date time;
		
		String toString() {
			return "${lat},${lon},${elev},${time}";
		}
		
		Double distanceTo(GPS g) {
		   return GPX.distance(this,g);
		}
		
		Double distanceToMiles(GPS g) {
		   return GPX.distance(this,g) * GPX.MILES_KM;
		}
		
		Integer elapsedTimeSince(GPS g) {
		   Math.abs((g.time.getTime() - this.time.getTime())/1000);
		}
		
		Double elapsedElevation(GPS g) {
		   Math.abs(this.elev - g.elev);
		}
		
}
