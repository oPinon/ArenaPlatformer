package physics;

public class Box {

	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;

	public Box(double xMin, double xMax, double yMin, double yMax) {
		this.xMin=xMin;
		this.xMax=xMax;
		this.yMin=yMin;
		this.yMax=yMax;
	}
	
	public boolean collides(Box other) {
		if(this.xMax>other.xMin && this.xMin<other.xMax) {
			return (this.yMax>other.yMin && this.yMin<other.yMax);
		}
		else { return false; }
	}
	
	/*
	 * returns :
	 * -1 if collides from left
	 * 0 if doesn't collides
	 * 1 if collides from right
	 */
	public int collidesX(Box other) {
		
		if( this.yMax>other.yMin && this.yMin<other.yMax) {
			if( this.xMin < other.xMax) {
				if(this.xMax>other.xMin) { return -1; }
				else { return 0; }
			}
			else if(this.xMax > other.xMin) {
				if(this.xMin<other.xMax) { return 1; }
				else { return 0; }
			}
			else { return 0; }
		}
		else return 0;
	}
	
	/*
	 * returns :
	 * -1 if collides from below
	 * 0 if doesn't collides
	 * 1 if collides from above
	 */
	public int collidesY(Box other) {
		
		if( this.xMax>other.xMin && this.xMin<other.xMax) {
			if( this.yMin < other.yMax) {
				if(this.yMax>other.yMin) { return -1; }
				else { return 0; }
			}
			else if(this.yMax > other.yMin) {
				if(this.yMin<other.yMax) { return 1; }
				else { return 0; }
			}
			else { return 0; }
		}
		else return 0;
	}
	
	public double getxMin() {
		return xMin;
	}

	public double getxMax() {
		return xMax;
	}

	public double getyMin() {
		return yMin;
	}

	public double getyMax() {
		return yMax;
	}
}
