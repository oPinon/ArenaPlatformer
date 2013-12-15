package test;

import physics.*;

public class BoxTest {

	public static void main(String[] args) {
		
		Box b1 = new Box(0,4,0,1);
		Box b2 = new Box(2,5,0.3,2);
		
		System.out.println(b1.collides(b2).name());
		
	}

}
