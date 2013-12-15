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
	
	public BoxCollision collides(Box other){
		
		boolean ax = xMin < other.xMin;
		boolean bx = xMin < other.xMax;
		boolean cx = xMax < other.xMax;
		boolean dx = xMax < other.xMin;
		
		boolean ay = yMin < other.yMin;
		boolean by = yMin < other.yMax;
		boolean cy = yMax < other.yMax;
		boolean dy = yMax < other.yMin;
		
		if(dx||!bx||dy||!by) { return BoxCollision.out; }
		if(by&&!dy) {
			if(cx&&ax&&!dx) { return BoxCollision.left; }
			if(bx&&!cx&&!ax) { return BoxCollision.right; }
		}
		if(bx&&!dx) {
			if(cy&&ay&&!dy) { return BoxCollision.up; }
			if(by&&!cy&&!ay) { return BoxCollision.down; }
		}	
		return BoxCollision.in;
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
	
	public void move(double x, double y) {
		xMin += x;
		xMax += x;
		yMin += y;
		yMax += y;
	}
}
