package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Animation {

	public String character, animationName;
	private Animation nextAnimation;
	private int frameDelay;
	private ArrayList<BufferedImage> spritesLeft, spritesRight;
	private int counter, currentFrame;
	private int xOffset, yOffset;

	public Animation(String character, String animationName, Animation nextAnimation, int frameDelay) {
		this.character = character;
		this.animationName = animationName;
		this.nextAnimation = nextAnimation;
		this.frameDelay = frameDelay;

		spritesLeft = new ArrayList<BufferedImage>();
		spritesRight = new ArrayList<BufferedImage>();
		boolean loading = true;
		for(int i=0; loading; i++) {
			BufferedImage spriteLeft, spriteRight;
			try {
				spriteLeft = loadSprite(i,"left");
				spriteRight = loadSprite(i,"right");
			} catch (IOException e) {
				spriteLeft = null;
				spriteRight = null;
			}
			if(spriteLeft==null) { loading=false; }
			else {
				spritesLeft.add(spriteLeft);
				spritesRight.add(spriteRight);
			}
		}

		if(spritesLeft.get(0)!=null) {xOffset=spritesLeft.get(0).getWidth()/2; yOffset=spritesLeft.get(0).getHeight()-10;} // WARNING : shall be changed for a reading in the character's properties
	}
	
	public void setNextAnimation(Animation nextAnimation) {
		this.nextAnimation = nextAnimation;
	}

	public int getXOffset() { return xOffset; }
	public int getYOffset() { return yOffset; }

	public BufferedImage getSprite(Direction direction) {
		if(direction==Direction.RIGHT) { return spritesRight.get(currentFrame); }
		else { return spritesLeft.get(currentFrame); }
	}

	public Animation update() {
		if(counter<frameDelay) { counter++; return this;}
		else if(currentFrame < spritesLeft.size()-1) { currentFrame++; counter=0; return this;}
		else {
			if(nextAnimation==null) { this.reset(); return this;}
			else {nextAnimation.reset(); return nextAnimation;}
		}
	}

	public void reset() {
		counter=0; currentFrame=0;
	}

	private BufferedImage loadSprite(int frame, String direction) throws IOException {
		return ImageIO.read(new File("sprites/"+character+"/"+animationName+"/"+direction+"/"+frame+".png"));
	}
}
