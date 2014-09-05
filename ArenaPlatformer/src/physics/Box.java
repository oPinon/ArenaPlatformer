package physics;

import javafx.scene.canvas.GraphicsContext;

public class Box {

	protected double xMin;
	protected double xMax;
	protected double yMin;
	protected double yMax;

	public Box(double xMin, double xMax, double yMin, double yMax) {
		if(xMax<xMin){ double temp=xMax; xMax=xMin; xMin=temp; }
		if(yMax<yMin){ double temp=yMax; yMax=yMin; yMin=temp; }
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
	
	public BoxCollision collidesX(Box other) {
		
		if( this.yMax>other.yMin && this.yMin<other.yMax) {
			if(this.xMax>other.xMin && this.xMin<other.xMax) {
				if(this.xMin<other.xMin&&this.xMax<other.xMax) { return BoxCollision.LEFT; }
				else if(this.xMin>other.xMin&&this.xMax>other.xMax) { return BoxCollision.RIGHT; }
				else { return BoxCollision.IN; }
			}
			else { return BoxCollision.OUT; }
		}
		else { return BoxCollision.OUT; }
	}
	
	public BoxCollision collidesY(Box other) {
		
		if( this.xMax>other.xMin && this.xMin<other.xMax) {
			if(this.yMax>other.yMin && this.yMin<other.yMax) {
				if(this.yMin<other.yMin&&this.yMax<other.yMax) { return BoxCollision.BELOW; }
				else if(this.yMin>other.yMin&&this.yMax>other.yMax) { return BoxCollision.ABOVE; }
				else { return BoxCollision.IN; }
			}
			else { return BoxCollision.OUT; }
		}
		else { return BoxCollision.OUT; }
	}
	
	public void paint(int offX, int offY, GraphicsContext g) {
		g.fillRect(offX+(int)+xMin, offY-(int)yMax, (int)(xMax-xMin), (int)(yMax-yMin));
	}
}
