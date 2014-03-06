package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Animation {

	private String character, animationName;
	private Animation nextAnimation;
	private int frameDelay;
	private ArrayList<BufferedImage> sprites;
	private int counter, currentFrame;
	private int xOffset, yOffset;

	public Animation(String character, String animationName, Animation nextAnimation, int frameDelay) {
		this.character = character;
		this.animationName = animationName;
		this.nextAnimation = nextAnimation;
		this.frameDelay = frameDelay;

		sprites = new ArrayList<BufferedImage>();
		boolean loading = true;
		for(int i=0; loading; i++) {
			BufferedImage sprite;
			try {
				sprite = loadSprite(i);
			} catch (IOException e) {
				sprite = null;
			}
			if(sprite==null) { loading=false; }
			else { sprites.add(sprite); }
		}
		
		if(sprites.get(0)!=null) {xOffset=sprites.get(0).getWidth()/2; yOffset=sprites.get(0).getHeight()-10;} // WARNING : shall be changed for a reading in the character's properties
	}
	
	public int getXOffset() { return xOffset; }
	public int getYOffset() { return yOffset; }

	public BufferedImage getSprite() {
		return sprites.get(currentFrame);
	}

	public Animation update() {
		if(counter<frameDelay) { counter++; return this;}
		else if(currentFrame < sprites.size()-1) { currentFrame++; counter=0; return this;}
		else {
			if(nextAnimation==null) { this.reset(); return this;}
			else {nextAnimation.reset(); return nextAnimation;}
			}
	}

	public void reset() {
		counter=0; currentFrame=0;
	}

	private BufferedImage loadSprite(int frame) throws IOException {
		return ImageIO.read(new File("sprites/"+character+"/"+animationName+"/"+frame+".png"));
	}
}
