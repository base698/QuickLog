package quicklog

import groovy.util.GroovyCollections

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
   def fMin,fMax,fAvg,cMin,cMax,cAvg;
   
   def Jump(timePoints) {
      this.timePoints = timePoints
      units = "English"
      calculateExitAltitude();
      calculateOpeningAltitude();
      truncateBegin();
      truncateEnd();
      //adjustElapsedTime();      
      calculate();
   }

   def calculate() {
       def canopySpeeds = [];
       def freefallSpeeds = [];
       def freefall = false;
       def canopy = false;
       int canopyPoints = 0;
       for(it in this.timePoints) {
          if(it.isExit) freefall = true;
          if(it.isOpening) canopy = true;
          

          if(canopy) {
             // correct for snivel
             if(canopyPoints > 1) {
                canopySpeeds.add(it.verticalSpeed);
             }

  	     canopyPoints++;
             continue;
          }

          if(freefall) {
	     freefallSpeeds.add(it.verticalSpeed);
          }
       }
       
       if(this.exitAltitude > 0.0 && freefallSpeeds.size() > 0) {
       	  fMin = Math.round( GroovyCollections.min(freefallSpeeds) )
          fMax = Math.round( GroovyCollections.max(freefallSpeeds) )
          fAvg = Math.round( GroovyCollections.sum(freefallSpeeds)/freefallSpeeds.size() )
       }

       if(this.openingAltitude > 0.0 && canopySpeeds.size() > 0) {
          cMin = Math.round( GroovyCollections.min(canopySpeeds) )
          cMax = Math.round( GroovyCollections.max(canopySpeeds) )
          cAvg = Math.round( GroovyCollections.sum(canopySpeeds)/canopySpeeds.size() )
       }
       
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
         if(it.isOpening) {
            okToTruncate = true;
	 }

         if(it.elevChange < 5.0) {
	    elevChanges++;
         }

         if(elevChanges > 10 && okToTruncate) {
	    this.timePoints.removeRange(count-5,this.timePoints.size());
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
          currentTime = it.elapsedTime;

          if(it.isExit) {
	     this.timePoints.removeRange(0,count)
             break;
	  }

          count++;

       }

       for(it in this.timePoints) {
       	  it.elapsedTime -= currentTime
       }
   }

   def calculateExitAltitude() {
      def last = null;
      def secondCount = 0;
      def count = 0;

      for(it in this.timePoints) {
      	     
         if(it.verticalSpeed > 60.0) {
	     secondCount++
  	 }
         if(secondCount == 4) {
	     def exitJumpPoint;
	     
	     if(count > 3) {
	     	exitJumpPoint = this.timePoints.get(count-3);
	     } else {
	        exitJumpPoint = it;
             }
	     exitJumpPoint.isExit = true
             this.exitAltitude = Math.round(exitJumpPoint.elev);
             return;
	 }	 
     	 last = it;
         count++;
      }
   }
   
   def calculateOpeningAltitude() {
      def last = null;
      def secondCount = 0;
      def count = 0;
      def okToCalculate

      for(it in this.timePoints) {
         if(it.verticalSpeed > 60) {
	    okToCalculate = true		
 	 }

         if(it.verticalSpeed < 25.0 && okToCalculate) {
	     secondCount++
  	 }

	 if(it.verticalSpeed > 50 && !okToCalculate) {
	     secondCount = 0;
	 }

	 // Shift the opening point back a little to compensate for
         // when the calculation started
         if(secondCount == 4) {
	     def openingPoint;
             if(count > 5) {
	        openingPoint = this.timePoints.get(count-5);
	     } else {
	        openingPoint = it;
	     }

	     openingPoint.isOpening = true
             this.openingAltitude = Math.round(openingPoint.elev)
	     return;
	 }	 
	 count++
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