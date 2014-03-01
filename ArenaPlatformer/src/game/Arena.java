package game;

import java.awt.Graphics;
import java.util.ArrayList;

import physics.Box;

public class Arena {

	private ArrayList<Box> colMap;
	
	public Arena() {
		colMap = new ArrayList<Box>();
		
		int e = 30; // boxes thickness
		int v = 100; // vertical jump's height
		int h = 150; // horizontal jump's length
		
		colMap.add(new Box(-2*h,2*h,0,e));
		
		colMap.add(new Box(2*h,3*h,-v,-v+e));
		colMap.add(new Box(-3*h,-2*h,-v,-v+e));
	}
	
	public int collidesX(Box box) {
		for(Box b: colMap) {
			int col = box.collidesX(b);
			if(col!=0) { return col; }
		}
		return 0;
	}
	
	public int collidesY(Box box) {
		for(Box b: colMap) {
			int col = box.collidesY(b);
			if(col!=0) { return col; }
		}
		return 0;
	}
	
	public void paint(int offX, int offY, Graphics g) {
		for(Box box : colMap) {
			box.paint(offX,offY,g);
		}
	}
}
