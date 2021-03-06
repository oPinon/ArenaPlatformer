package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import physics.*;

public class Player {
		
	public Position pos;
	public HitBox hitBox, feetBox;
	private PlayerState state;
	private Direction movDirection, spriteDirection;
	private double dx, dy;
	private double gravity = -1;
	private double jumpForce = 20, runSpeed = 10, airControl = 0.05, wallJumpXForce = 10, wallJumpYForce = jumpForce;
	private int groundStartInertia = 20, groundStopInertia = 30;
	private int animationFramePerFrame = 2;
	
	private Animation currentAnimation, brake, fall, fallStart, jump, jumpStart, punch, run, startRun, wait, wall, wallJump, wallStart;
	
	static int maxSpeed = Arena.e;

	public Player() {
		this.pos = new Position(0,200);
		this.feetBox = new HitBox(pos,-15,15,0,30);
		this.hitBox = new HitBox(pos,-20,20,30,100);
		this.state = PlayerState.WAIT;
		this.movDirection = Direction.NO;
		this.spriteDirection = Direction.RIGHT;
		this.dx = 0; this.dx = 0;
		
		wait = new Animation("testChar","wait",null,animationFramePerFrame);
		brake = new Animation("testChar","brake",wait,animationFramePerFrame);
		fall = new Animation("testChar","fall",null,animationFramePerFrame);
		fallStart = new Animation("testChar","fallStart",fall,animationFramePerFrame);
		jump = new Animation("testChar","jump",null,animationFramePerFrame);
		jumpStart = new Animation("testChar","jumpStart",jump,animationFramePerFrame);
		//punch = new Animation("testChar","punch",wait,animationFramePerFrame);
		run = new Animation("testChar","run",null,animationFramePerFrame);
		startRun = new Animation("testChar","startRun",run,animationFramePerFrame);
		wall = new Animation("testChar","wall",null,animationFramePerFrame);
		wallJump = new Animation("testChar","wallJump",jump,animationFramePerFrame);
		wallStart = new Animation("testChar","wallStart",wall,animationFramePerFrame);
		
		setAnimation(fall);
	}

	public void update(Arena arena) {
		hitBox.update();
		feetBox.update();

		switch(state){
		case WAIT : {
			if(tryFalling(arena)) { setAnimation(fallStart); update(arena); break; };
			if(!tryMoving(arena)) { break; };
			if(movDirection!=Direction.NO) { state = PlayerState.RUN; update(arena); break;}
			brake();
			moveXGround();
			break;
		}
		case RUN : {
			if(tryFalling(arena)) { setAnimation(fallStart); update(arena); break; };
			if( movDirection==Direction.NO ) { setAnimation(brake); state = PlayerState.WAIT; update(arena); break; }
			accelerate();
			if(dx>0) { spriteDirection=Direction.RIGHT;} else { spriteDirection=Direction.LEFT;}
			if(currentAnimation!=run&&currentAnimation!=startRun){ setAnimation(startRun);}
			if(!tryMoving(arena)) {break; };
			moveXGround();
			break;
		}
		case JUMP : {
			if(!tryGoingUp(arena)) { state = PlayerState.FALL; update(arena); break; }
			if(tryWalling(arena)) { setAnimation(wallStart); update(arena); break; }
			if(dy<=0) { state = PlayerState.FALL; setAnimation(fallStart); update(arena); break;}
			airControl();
			dy += gravity;
			moveXAir(); moveYAir();
			break;
		}
		case ONWALL : {
			if(tryLanding(arena)) { update(arena); break; };
			if(tryFallingFromWall(arena)) { setAnimation(fallStart); state = PlayerState.FALL; update(arena); break; }
			tryGoingUp(arena);
			dy += gravity;
			moveYWall();
			break;
		}
		case FALL : {
			if(tryLanding(arena)) { setAnimation(wait); update(arena); break; };
			if(tryWalling(arena)) { setAnimation(wallStart); update(arena); break; }
			airControl();
			dy += gravity;
			moveXAir(); moveYAir();
			break;
		}
		case PUNCH : {

			break;
		}
		}
	}

	private boolean tryLanding(Arena arena) {
		if(arena.collidesY(feetBox)==BoxCollision.BELOW) { // is the arena below the player's feet ?
			dy = 0;
			if(movDirection==Direction.NO) { state = PlayerState.WAIT; }
			else { state = PlayerState.RUN; }
			return true;
		}
		else { return false; }
	}
	
	private boolean tryWalling(Arena arena) {
		if(arena.collidesX(hitBox)==BoxCollision.RIGHT&&(dx<0)) {
			spriteDirection=Direction.RIGHT;
			state = PlayerState.ONWALL;
			dx=0;
			return true;
		}
		else if(arena.collidesX(hitBox)==BoxCollision.LEFT&&(dx>0)) {
			spriteDirection=Direction.LEFT;
			state = PlayerState.ONWALL;
			dx=0;
			return true;
		}
		else { return  false; }
	}
	
	private boolean tryFalling(Arena arena) {
		if(arena.collidesY(feetBox)!=BoxCollision.BELOW) { // is the arena not below the player's feet ?
			state = PlayerState.FALL;
			return true;
		}
		else { return false; }
	}

	private boolean tryFallingFromWall(Arena arena) {
		if(movDirection==Direction.NO) {
			if(arena.collidesX(hitBox)==BoxCollision.OUT) { return true; }
			else { return false; }
		}
		else if(movDirection==Direction.LEFT&&!(arena.collidesX(hitBox)==BoxCollision.RIGHT)) { return true; }
		else if(movDirection==Direction.RIGHT&&!(arena.collidesX(hitBox)==BoxCollision.LEFT)) { return true; }
		else { return false; }
	}
	
	private boolean tryMoving(Arena arena) {
		if(dx>0) {
			if(arena.collidesX(hitBox)==BoxCollision.LEFT) {
				dx=0; return false;
			}
			else { return true; }
		}
		else if(dx<0) {
			if(arena.collidesX(hitBox)==BoxCollision.RIGHT) {
				dx=0; return false;
			}
			else { return true; }
		}
		return true;
	}
	
	private void airControl() {
		if( movDirection==Direction.LEFT ) { dx-=runSpeed*airControl; }
		else if( movDirection==Direction.RIGHT ) { dx+=runSpeed*airControl; }
		dx = Math.max(-runSpeed, Math.min(dx,+runSpeed));
	}
	
	private void brake() {
		if(dx>0) { dx = Math.max(0, dx - runSpeed/groundStopInertia); }
		else if (dx<0) { dx = Math.min(0, dx + runSpeed/groundStopInertia); }
	}
	
	private void accelerate() {
		if(movDirection==Direction.RIGHT) { dx = Math.min(runSpeed,dx+runSpeed/groundStartInertia); }
		else if (movDirection==Direction.LEFT) { dx = Math.max(-runSpeed,dx-runSpeed/groundStartInertia); }
	}

	private boolean tryGoingUp(Arena arena) {
		if(arena.collidesY(hitBox)==BoxCollision.ABOVE) {
			if(dy>0) { dy=0; }
			return false;
		}
		else { return true; }
	}
	
	private void moveXGround() {
		pos.x += dx;
	}
	
	private void moveXAir() {
		if(Math.abs(dx)>maxSpeed) { dx = Math.signum(dx)*maxSpeed; }
		pos.x += dx;
	}

	private void moveYAir() {
		if(Math.abs(dy)>maxSpeed) { dy = Math.signum(dy)*maxSpeed; }
		pos.y += dy;
	}
	
	private void moveYWall() {
		if(Math.abs(dy)>maxSpeed) { dy = Math.signum(dy)*maxSpeed; }
		pos.y += dy;
	}

	public void jump() {
		if(state==PlayerState.WAIT||state==PlayerState.RUN) { // if on the ground
			dy = jumpForce;
			state = PlayerState.JUMP;
			setAnimation(jumpStart);
		}
		if(state==PlayerState.ONWALL) { // if on wall
			dy=0;
			if(spriteDirection==Direction.LEFT) { // jumps on the opposite direction of the wall
				dx = -wallJumpXForce;
				dy = wallJumpYForce;
				if(movDirection==Direction.RIGHT) { movDirection = Direction.NO; }
			}
			else if(spriteDirection==Direction.RIGHT) {
				dx = wallJumpXForce;
				dy = wallJumpYForce;
				if(movDirection==Direction.RIGHT) { movDirection = Direction.NO; }
			}
			state = PlayerState.JUMP;
			setAnimation(wallJump);
		}
	}

	public void goLeft() {
		movDirection = Direction.LEFT;
	}

	public void stopLeft() {
		if(movDirection==Direction.LEFT) { movDirection=Direction.NO; }
	}

	public void goRight() {
		movDirection = Direction.RIGHT;
	}

	public void stopRight() {
		if(movDirection==Direction.RIGHT) { movDirection=Direction.NO; }
	}
	
	public void setAnimation(Animation animation) { animation.reset(); currentAnimation=animation;}

	public void paint(int offX, int offY, Graphics g, Arena arena){
		/*g.setColor(Color.cyan);
		hitBox.paint(offX, offY, g);
		g.setColor(Color.green);
		feetBox.paint(offX, offY, g);*/
		
		g.drawImage(currentAnimation.getSprite(spriteDirection),offX+pos.x-currentAnimation.getXOffset(),offY-pos.y-currentAnimation.getYOffset(),null);
		currentAnimation = currentAnimation.update();
		
		g.setColor(Color.white);
		g.drawString("(state) "+state.name(), 10, 20);
		g.drawString("(movDir) "+movDirection.name(), 10, 40);
		g.drawString("(spritDir) "+spriteDirection.name(), 10, 60);
		g.drawString("(animation) "+currentAnimation.animationName, 10, 80);

		g.drawString("hitBox", 10, 120);
		g.drawString("x: "+arena.collidesX(hitBox), 10, 140);
		g.drawString("y: "+arena.collidesY(hitBox), 10, 160);

		g.drawString("hitBox", 10, 180);
		g.drawString("x: "+arena.collidesX(feetBox), 10, 200);
		g.drawString("y: "+arena.collidesY(feetBox), 10, 220);
	}
}
