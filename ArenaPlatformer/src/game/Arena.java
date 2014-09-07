package game;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import physics.Box;
import physics.BoxCollision;

public class Arena {

	private ArrayList<Box> colMap;
	private Image background;
	private double backgroundWidth, backgroundHeight;
	
	public Box bounds;
	private int minX = -1000;
	private int maxX = 1000;
	private int minY = -200;
	private int maxY = 1000;
	
	static int e = 30; // boxes thickness
	static int v = 110; // vertical jump's height
	static int h = 200; // horizontal jump's length
	
	public Arena() {
		
		String backgroundFilename = "file:sprites/background.jpg";
		background = new Image(backgroundFilename);
		if(background.isError()) { System.out.println("couldn't load " + backgroundFilename); }
		else { backgroundWidth = background.getWidth(); backgroundHeight = background.getHeight(); }
		
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
		
		bounds = new Box(minX, maxX, minY, maxY);
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
	
	public void paint(int offX, int offY, double screenWidth, double screenHeight, GraphicsContext g) {
		
		double kX = (backgroundWidth-screenWidth)/(maxX-minX);
		double kY = (backgroundHeight-screenHeight)/(maxY-minY);
		double backGX = kX*(offX - (-maxX-minX+screenWidth)/2) - backgroundWidth/2 + screenWidth/2;
		double backGY = kY*(offY - (maxY+minY+screenHeight)/2) - backgroundHeight/2 + screenHeight/2;
		g.drawImage(background, backGX, backGY);
		
		for(Box box : colMap) {
			box.paint(offX,offY,g);
		}
	}
}
