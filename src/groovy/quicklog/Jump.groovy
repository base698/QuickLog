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
      adjustElapsedTime();      
      calculate();
   }

   def calculate() {
       def canopySpeeds = [];
       def freefallSpeeds = [];
       def freefall = false;
       def canopy = false;
       for(it in this.timePoints) {
          if(it.isExit) freefall = true;
          if(it.isOpening) canopy = true;
          
          def canopyPoints = 0
          if(canopy) {
        //     if(canopyPoints>4) {
	        canopySpeeds.add(it.verticalSpeed);
        //     }
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

   def adjustElapsedTime() {
      def count = 0;
      def last = null;
      for(it in this.timePoints) {
          if(count == 0) {
	     it.elapsedTime = 0;
 	     last = it;
	     count++;
             continue;
	  }
          it.elapsedTime = last.elapsedTime += it.secondsSince;
	  count++;
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