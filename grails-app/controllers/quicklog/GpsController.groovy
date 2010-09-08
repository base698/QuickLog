package quicklog

import quicklog.GPS
import quicklog.util.GPX
import grails.converters.*
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartFile

class GpsController {

    def index = {
       String xmlStr;

       if(params['gpx'] && params['gpx'].getSize() > 0) {
       
 			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request
  			CommonsMultipartFile file = (CommonsMultipartFile)multiRequest.getFile("gpx")
  			BufferedReader bin = new BufferedReader(new InputStreamReader(file.getInputStream()))
  			String line = bin.readLine()
  			StringBuffer sb = new StringBuffer();
		 	
		 	while (line != null) {
  			  sb.append(line);
  			  line = bin.readLine();
   			 
  			}

          xmlStr = sb.toString()
       } else {
          File xmlFile = new File("web-app/truncated.gpk");
	      xmlStr = xmlFile.getText();       
       }
       
	   def gpx = new XmlParser().parseText(xmlStr)
	   def track = gpx.trk
	   def gpsTracks = GPX.getTracks(track);
	   
       //def totalHorizontalDistance = GPX.calculateTotalDistanceMiles(gpsPoints);
       def jumps = GPX.listTracksToJumps(gpsTracks)
       def converted = new JSON(jumps);
       render converted.toString()
    }
}
