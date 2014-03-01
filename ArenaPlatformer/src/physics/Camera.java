package physics;

public class Camera {

	private Position center;
	private int width, height;
	
	public Camera(Position center, int width, int height) {
		this.center = center;
		this.width = width;
		this.height = height;
	}
	
	public int getXOffset() {
		return width/2 - center.x;
	}
	
	public int getYOffset(){
		return height/2 + center.y;
	}
}
