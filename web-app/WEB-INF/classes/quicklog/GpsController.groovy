package quicklog

import quicklog.GPS
import quicklog.util.GPX
import grails.converters.*

class GpsController {

    def index = {
       String xmlStr;
       if(params['gpx']) {
          xmlStr = params['gpx']
       } else {
          File xmlFile = new File("/home/base698/Downloads/truncated.gpk");
	      xmlStr = xmlFile.getText();       
       }
	   def gpx = new XmlParser().parseText(xmlStr)
	   def track = gpx.trk
	   ArrayList<GPS> gpsPoints = GPX.getTrack(track);
       def totalHorizontalDistance = GPX.calculateTotalDistanceMiles(gpsPoints);
       def listOfJumpPts = GPX.listGpsToJump(gpsPoints)
    
       render listOfJumpPts as JSON
    }
}
