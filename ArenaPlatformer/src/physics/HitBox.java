package physics;

public class HitBox extends Box{

	private Position pos;
	
	private double xMinRel; // "Rel" stands for relative to the position
	private double xMaxRel;
	private double yMinRel;
	private double yMaxRel;
	
	public HitBox(Position pos, double xMinRel, double xMaxRel, double yMinRel, double yMaxRel) {
		super(0,0,0,0);
		this.pos=pos;
		this.xMinRel=xMinRel;
		this.xMaxRel=xMaxRel;
		this.yMinRel=yMinRel;
		this.yMaxRel=yMaxRel;
		update();
	}
	
	public void update() {
		this.xMin = pos.x + xMinRel;
		this.xMax = pos.x + xMaxRel;
		this.yMin = pos.y + yMinRel;
		this.yMax = pos.y + yMaxRel;
	}
}
