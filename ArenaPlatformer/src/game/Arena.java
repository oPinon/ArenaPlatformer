package game;

import java.awt.Graphics;
import java.util.ArrayList;

import physics.Box;
import physics.BoxCollision;

public class Arena {

	private ArrayList<Box> colMap;
	
	static int e = 30; // boxes thickness
	static int v = 110; // vertical jump's height
	static int h = 200; // horizontal jump's length
	
	public Arena() {
		colMap = new ArrayList<Box>();
		
		colMap.add(new Box(-2*h,2*h,0,e));
		
		colMap.add(new Box(2*h,4*h,-v,-v+e));
		colMap.add(new Box(-4*h,-2*h,-v,-v+e));
		
		colMap.add(new Box(5*h,6*h,-v,-v+e));
		colMap.add(new Box(-6*h,-5*h,-v,-v+e));
		
		colMap.add(new Box(2*h-e,2*h,2*v,6*v));
		colMap.add(new Box(-2*h,-2*h+e,2*v,6*v));
		
		colMap.add(new Box(h/2,2*h,6*v,6*v+e));
		colMap.add(new Box(-2*h,-h/2,6*v,6*v+e));
		
		colMap.add(new Box(3*h,3*h+e,1*v,3*v));
		colMap.add(new Box(-3*h-e,-3*h,1*v,3*v));
		
		colMap.add(new Box(3*h,4*h,3*v,3*v+e));
		colMap.add(new Box(-4*h,-3*h,3*v,3*v+e));
		
		colMap.add(new Box(3*h,3*h+e,5*v,8*v));
		colMap.add(new Box(-3*h-e,-3*h,5*v,8*v));
	}
	
	public BoxCollision collidesX(Box box) {
		for(Box b: colMap) {
			BoxCollision col = box.collidesX(b);
			if(col!=BoxCollision.OUT) { return col; }
		}
		return BoxCollision.OUT;
	}
	
	public BoxCollision collidesY(Box box) {
		for(Box b: colMap) {
			BoxCollision col = b.collidesY(box);
			if(col!=BoxCollision.OUT) { return col; }
		}
		return BoxCollision.OUT;
	}
	
	public void paint(int offX, int offY, Graphics g) {
		for(Box box : colMap) {
			box.paint(offX,offY,g);
		}
	}
}
