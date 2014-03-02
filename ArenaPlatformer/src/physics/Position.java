package physics;

public class Position {

	public int x;
	public int y;
	
	public Position() {
		this.x=0;
		this.y=0;
	}
	
	public Position(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public Position(Position other) {
		this.x = other.x;
		this.y = other.y;
	}
	
	static double distance(Position p1, Position p2) {
		double dx = (p1.x-p2.x);
		double dy = (p1.y-p2.y);
		return Math.sqrt(dx*dx+dy*dy);
	}
}
