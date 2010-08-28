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
	     it.isOpen = true
             this.exitAltitude = Math.round(it.elev);
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
	     it.isOpen = true
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