package physics;

import javafx.beans.property.DoubleProperty;

public class Camera {

	static double inertia = 10;
	
	public Position target, pos;
	private DoubleProperty width, height;
	
	public Camera(Position center, DoubleProperty width,  DoubleProperty height) {
		this.target = center;
		this.pos = new Position(center);
		this.width = width;
		this.height = height;
	}
	
	public int getXOffset() {
		return (int) width.get()/2 - pos.x;
	}
	
	public int getYOffset(){
		return (int) height.get()/2 + pos.y;
	}
	
	public void update(){
		double dx = target.x - pos.x;
		double dy = target.y - pos.y;

		pos.x += dx/inertia;
		pos.y += dy/inertia;
	}
}
