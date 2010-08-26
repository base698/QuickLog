package quicklog
// To be used in a list of other jump time points.
class JumpTimePoint {
    Double lat;
    Double lon;
	Double horizontalSpeed = 0.0;
	Double verticalSpeed = 0.0;
	Integer elapsedTime = 0;
	Float glideRatio = 0.0;
	Double distChange = 0.0;
	Double elapsedVerticalDistance = 0.0;
	Double elevChange = 0.0;
    Double elapsedHorizontalDistance = 0.0;
	Boolean isOpen = false;
	Boolean isExit = false;
	String toString() {
	    """
	      elapsedTime=${this.elapsedTime},horizontalSpeed=${this.horizontalSpeed},verticalSpeed=${this.verticalSpeed},
	      glideRatio=${this.glideRatio}
	    """
	}
}