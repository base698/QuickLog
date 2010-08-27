package quicklog.util

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import quicklog.GPS
import quicklog.JumpTimePoint

class GPX {
    static final double MILES_KM = 0.621371192;
	static final double FEET_METER = 3.2808;
	static final double FEET_MILES = 5280.0;
	static final double G = 32.6;
	static final double DESCENT_FOR_EXIT = 65;
	static final double SUSTAINED_TIME_AFTER_EXIT = 5.0;
	
	//2010-02-21T19:27:12Z
	public static SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
		
	static def testMain() {
		File xmlFile = new File("/home/base698/Downloads/truncated.gpk");
		String xmlStr = xmlFile.getText();
		def gpx = new XmlParser().parseText(xmlStr)
		def track = gpx.trk
		ArrayList<GPS> gpsPoints = getTrack(track);
		
		println(calculateTotalDistance(gpsPoints)*MILES_KM);
	}
	
	static Double calculateTotalDistanceMiles(List<GPS> points) {
		double totalDistance = 0.0;
		GPS last = null;
		for(GPS g:points) {
			if(last!=null) {
				totalDistance += distance(g,last);
			}
			
			last = g;
		}
		
		return totalDistance*MILES_KM;
	}
	
	// For all the tracks in the gpx file get all the track points.
	static List<GPS> getTrack(tracks) {
		List<GPS> points = new ArrayList<GPS>();
		
		tracks.each { trk ->
		   trk.trkseg.each { ts ->
		      ts.each { tp ->
		   	     points.add(getGps(tp));
		   	  }
		   }
		}

		return points;
	}
	
	// 
	static GPS getGps(trkpt) {
		GPS gps = new GPS();	
		gps.lat = Double.parseDouble(trkpt.'@lat');
		gps.lon = Double.parseDouble(trkpt.'@lon');
		gps.elev = Double.parseDouble(trkpt.ele.text()) * FEET_METER;
		gps.time = sdf.parse(trkpt.time.text().substring(11,19));
		return gps;
	}

    // Haversine formula for calculating distance (km) of GPS
	static double distance(GPS pt1,GPS pt2) {
		double R = 6371.0; // km
		double dLat = Math.toRadians(pt2.lat-pt1.lat);
		double dLon = Math.toRadians(pt2.lon-pt1.lon); 
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(Math.toRadians(pt1.lat)) * Math.cos(Math.toRadians(pt2.lat)) * 
        Math.sin(dLon/2) * Math.sin(dLon/2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		double d = R * c;
		return d;
		
	}
	
	static def listGpsToJump(gps) {
		List<JumpTimePoint> list = new ArrayList<JumpTimePoint>();
		
		def startTime = gps[0].time;
		GPS last = null;
		JumpTimePoint lastPt = null;

		gps.each { g ->
		   JumpTimePoint p = new JumpTimePoint();
		   p.lat = g.lat;
		   p.lon = g.lon;
		   if(last==null) {
		      last = g;
		      lastPt = p;
		      return;
		   }
		   
		   // Seconds
		   def secondsSince = g.elapsedTimeSince(last)
		   p.elapsedTime = g.elapsedTimeSince(last) + lastPt.elapsedTime;

		   // FEET
		   def elevChange = g.elapsedElevation(last);
		   p.elapsedVerticalDistance += elevChange + lastPt.elapsedVerticalDistance
		   p.elevChange = elevChange;
		   // MPH
		   p.verticalSpeed = (elevChange/FEET_MILES)/(secondsSince/60/60)

		   // Miles
		   p.distChange = g.distanceToMiles(last);
		   p.elapsedHorizontalDistance += p.distChange + lastPt.elapsedHorizontalDistance

		   // MPH
		   p.horizontalSpeed = p.distChange / (secondsSince/60/60)
		   p.glideRatio = -1
		   if(p.verticalSpeed != 0.0 || p.verticalSpeed != 0) {
		      p.glideRatio = p.horizontalSpeed/p.verticalSpeed;
		   }
		   list.add(p);
		   
		   last = g;
		   lastPt = p;
		}
		
		def endTime = gps.last().time
		
		return list;
	}
}
