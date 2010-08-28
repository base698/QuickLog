package quicklog

class Jump {
   List<JumpTimePoint> timePoints;
   Double openingAltitude = 0.0;
   Double exitAltitude = 0.0;
   Double horizontalSpeed = 0.0;
   Double horizontalDistance = 0.0;
   Double verticalSpeed = 0.0;
   Double verticalDistance = 0.0;
   Integer elapsedTime = 0.0;
   Integer freefallTime = 0.0;
   Integer canopyTime = 0.0;
   String units;
   
   def Jump(timePoints) {
      this.timePoints = timePoints
      units = "English"
      calculateExitAltitude();
      calculateOpeningAltitude();
      truncateBegin();
      truncateEnd();

   }

   def truncate() {
      return (this.openingAltitude > 0.0 && this.exitAltitude > 0.0) 
   }

   def truncateEnd() {
      if(!this.truncate()) {
         return;
      }
      
      def count = 0;
      def okToTruncate = false;
      def elevChanges = 0;
      for(it in this.timePoints) {
         if(it.isExit) {
            okToTruncate = true;
	 }

         if(it.elevChange < 100.0) {
	    elevChanges++;
         }

         if(elevChanges > 5 && okToTruncate) {
            println count
            println this.timePoints.size()
	    this.timePoints.removeRange(count,this.timePoints.size()-1);
            return;
         }

         count++;
      }
   }

   def truncateBegin() {
       def count = 0;
       if(!this.truncate()) {
          return;
       }
       
       def currentTime;
       for(it in this.timePoints) {
          currentTime = this.elapsedTime;

          if(it.isExit) {
	     this.timePoints.removeRange(0,count)

             break;
	  }

          count++;

       }

       for(it in this.timePoints) {
       	  this.elapsedTime -= currentTime
       }
   }

   def calculateExitAltitude() {
      def last = null;
      def secondCount = 0;
      def secondToPoint = [:]
     
      for(it in this.timePoints) {
         secondToPoint[it.elapsedTime] = it

         if(it.verticalSpeed > 60.0) {
	     secondCount++
  	 }
         if(secondCount == 4) {
	     it.isExit = true
             this.exitAltitude = Math.round(it.elev);
             return;
	 }	 
     	 last = it;
      }
   }
   
   def calculateOpeningAltitude() {
      def last = null;
      def secondCount = 0;
      def secondToPoint = [:]
      def okToCalculate
      for(it in this.timePoints) {
         secondToPoint[it.elapsedTime] = it
         if(it.verticalSpeed > 60) {
	    okToCalculate = true		
 	 }

         if(it.verticalSpeed < 25.0 && okToCalculate) {
	     secondCount++
  	 }

	 if(it.verticalSpeed > 50 && !okToCalculate) {
	     secondCount = 0;
	 }

         if(secondCount == 4) {
	     it.isOpening = true
             // Subtract exit altitude?
             this.openingAltitude = Math.round(it.elev)
	     return;
	 }	 
     	 last = it;
      }
   }
   
   def calculateHorizontalSpeed() {
   
   }
   
   def calculateHorizontalDistance() {
   
   }
   
   def calculateVerticalSpeed() {
   
   }
   

   def calculateVerticalDistance() {
   
   }

      
}