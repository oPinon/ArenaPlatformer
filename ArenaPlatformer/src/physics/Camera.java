package physics;

public class Camera {

	static double inertia = 10;
	
	public Position target, pos;
	private int width, height;
	
	public Camera(Position center, int width, int height) {
		this.target = center;
		this.pos = new Position(center);
		this.width = width;
		this.height = height;
	}
	
	public int getXOffset() {
		return width/2 - pos.x;
	}
	
	public int getYOffset(){
		return height/2 + pos.y;
	}
	
	public void update(){
		double dx = target.x - pos.x;
		double dy = target.y - pos.y;

		pos.x += dx/inertia;
		pos.y += dy/inertia;
	}
}
