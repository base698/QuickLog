package quicklog

import quicklog.GPS
import quicklog.util.GPX
import grails.converters.*
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile

class GpsController {

    def index = {
       String xmlStr;
       if(params['gpx']) {
 			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request
  			CommonsMultipartFile file = (CommonsMultipartFile)multiRequest.getFile("gpx")
  			BufferedReader bin = new BufferedReader(new InputStreamReader(file.getInputStream()))
  			String line = bin.readLine()
  			StringBuffer sb = new StringBuffer();
		  // print each line
  			while (line != null) {
  			 sb.append(line);
  			 line = bin.readLine();
   			 
  			}


          xmlStr = sb.toString()
          println xmlStr
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
