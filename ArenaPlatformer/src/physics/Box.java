package physics;

import java.awt.Graphics;

public class Box {

	protected double xMin;
	protected double xMax;
	protected double yMin;
	protected double yMax;

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
			if(this.xMax>other.xMin && this.xMin<other.xMax) {
				if(this.xMin<other.xMin&&this.xMax<other.xMax) { return -1; }
				else if(this.xMin>other.xMin&&this.xMax>other.xMax) { return +1; }
				else { return 0; }
			}
			else { return 0; }
		}
		else { return 0; }
	}
	
	/*
	 * returns :
	 * -1 if collides from below
	 * 0 if doesn't collides
	 * 1 if collides from above
	 */
	public int collidesY(Box other) {
		
		if( this.xMax>other.xMin && this.xMin<other.xMax) {
			if(this.yMax>other.yMin && this.yMin<other.yMax) {
				if(this.yMin<other.yMin&&this.yMax<other.yMax) { return -1; }
				else if(this.yMin>other.yMin&&this.yMax>other.yMax) { return +1; }
				else { return 0; }
			}
			else { return 0; }
		}
		else { return 0; }
	}
	
	public void paint(int offX, int offY, Graphics g) {
		g.fillRect(offX+(int)+xMin, offY-(int)yMax, (int)(xMax-xMin), (int)(yMax-yMin));
	}
}
