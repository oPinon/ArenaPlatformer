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
	private double verticalDamping = 0.99,  horizontalDamping = 0.8, airControl = 1, wallFriction = 0.8;
	static double gravity = -1;
	private double jumpForce = 20, runForce = 3, wallJumpXForce = 20, wallJumpYForce = 20;
	static double threshold = 1; // to avoid divergence


	public Player() {
		this.pos = new Position(0,200);
		this.feetBox = new HitBox(pos,-15,15,0,20);
		this.hitBox = new HitBox(pos,-20,20,50,100);
		this.state = PlayerState.WAIT;
		this.movDirection = Direction.NO;
		this.spriteDirection = Direction.RIGHT;
		this.dx = 0; this.dx = 0;
	}

	public void update(Arena arena) {
		hitBox.update();
		feetBox.update();

		switch(state){
		case WAIT : {
			if(tryFalling(arena)) { break; };
			if(!tryMoving(arena)) { break; };
			if(movDirection!=Direction.NO) { state = PlayerState.RUN; break;}
			if( Math.abs(dx)< threshold ) { dx=0; }
			moveX();
			break;
		}
		case RUN : {
			if(tryFalling(arena)) { break; };
			controlSpeedOnGround();
			if( movDirection==Direction.NO && Math.abs(dx)< threshold ) { state = PlayerState.WAIT; dx=0; break; }
			if(!tryMoving(arena)) { break; };
			moveX();
			break;
		}
		case JUMP : {
			if(!tryGoingUp(arena)) { state = PlayerState.FALL; break; }
			if(tryWalling(arena)) { break; }
			if(dy<=0) { state = PlayerState.FALL; }
			controlSpeedOnAir();
			dy += gravity;
			moveX(); moveY();
			break;
		}
		case ONWALL : {
			if(tryLanding(arena)) { break; };
			if(tryFallingFromWall(arena)) { state = PlayerState.FALL; break; }
			tryGoingUp(arena);
			dy += gravity*wallFriction;
			moveY();
			break;
		}
		case FALL : {
			if(tryLanding(arena)) { break; };
			if(tryWalling(arena)) { break; }
			controlSpeedOnAir();
			dy += gravity;
			moveX(); moveY();
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

	private boolean tryFallingFromWall(Arena arena) {
		if(movDirection==Direction.NO) {
			if(arena.collidesX(hitBox)==BoxCollision.OUT) { return true; }
			else { return false; }
		}
		else if(movDirection==Direction.LEFT&&!(arena.collidesX(hitBox)==BoxCollision.RIGHT)) { return true; }
		else if(movDirection==Direction.RIGHT&&!(arena.collidesX(hitBox)==BoxCollision.LEFT)) { return true; }
		else { return false; }
	}

	private boolean tryWalling(Arena arena) {
		if(arena.collidesX(hitBox)==BoxCollision.RIGHT&&(dx<0)) {
			spriteDirection=Direction.LEFT;
			state = PlayerState.ONWALL;
			dx=0;
			return true;
		}
		else if(arena.collidesX(hitBox)==BoxCollision.LEFT&&(dx>0)) {
			spriteDirection=Direction.RIGHT;
			state = PlayerState.ONWALL;
			dx=0;
			return true;
		}
		else { return  false; }
	}

	private void controlSpeedOnAir() {
		if(movDirection==Direction.RIGHT) { dx += runForce*airControl; }
		else if(movDirection==Direction.LEFT) { dx -= runForce*airControl; }
	}

	private void controlSpeedOnGround() {
		if(movDirection==Direction.RIGHT) { dx += runForce; }
		else if(movDirection==Direction.LEFT) { dx -= runForce; }
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

	private boolean tryFalling(Arena arena) {
		if(arena.collidesY(feetBox)!=BoxCollision.BELOW) { // is the arena not below the player's feet ?
			state = PlayerState.FALL;
			return true;
		}
		else { return false; }
	}

	private boolean tryGoingUp(Arena arena) {
		if(arena.collidesY(hitBox)==BoxCollision.ABOVE) {
			if(dy>0) { dy=0; }
			return false;
		}
		else { return true; }
	}

	private void moveX() {
		dx *= horizontalDamping;
		pos.x += dx;
	}

	private void moveY() {
		dy *= verticalDamping;
		pos.y += dy;
	}

	public void jump() {
		if(state==PlayerState.WAIT||state==PlayerState.RUN) { // if on the ground
			dy = jumpForce;
			state = PlayerState.JUMP;
		}
		if(state==PlayerState.ONWALL) { // if on wall
			dy=0;
			if(spriteDirection==Direction.RIGHT) { // jumps on the opposite direction of the wall
				dx = -wallJumpXForce;
				dy += wallJumpYForce;
				//if(movDirection==Direction.RIGHT) { movDirection=Direction.NO; } to avoid infinite wallJump
			}
			else if(spriteDirection==Direction.LEFT) {
				dx = wallJumpXForce;
				dy += wallJumpYForce;
				//if(movDirection==Direction.LEFT) { movDirection=Direction.NO; }
			}
			state = PlayerState.JUMP;
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

	public void paint(int offX, int offY, Graphics g, Arena arena){
		g.setColor(Color.cyan);
		hitBox.paint(offX, offY, g);
		g.setColor(Color.green);
		feetBox.paint(offX, offY, g);
		g.setColor(Color.lightGray);
		g.drawString("(state) "+state.name(), 10, 20);
		g.drawString("(movDir) "+movDirection.name(), 10, 40);
		g.drawString("(spritDir) "+spriteDirection.name(), 10, 60);

		g.drawString("hitBox", 10, 100);
		g.drawString("x: "+arena.collidesX(hitBox), 10, 120);
		g.drawString("y: "+arena.collidesY(hitBox), 10, 140);

		g.drawString("hitBox", 10, 160);
		g.drawString("x: "+arena.collidesX(feetBox), 10, 180);
		g.drawString("y: "+arena.collidesY(feetBox), 10, 200);
	}
}
