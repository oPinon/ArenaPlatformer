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
	private PlayerDirection movDirection, spriteDirection;
	private double dx, dy;
	private double verticalDamping = 0.9,  horizontalDamping = 0.8, airSpeed = 0.6, wallFriction = 0.8;
	private double wallJumpAngle = 70*Math.PI/180;
	static double gravity = -1;
	private double jumpForce = 30, runForce = 3;


	public Player() {
		this.pos = new Position(0,200);
		this.feetBox = new HitBox(pos,-15,15,0,20);
		this.hitBox = new HitBox(pos,-20,20,50,100);
		this.state = PlayerState.WAIT;
		this.movDirection = PlayerDirection.NO;
		this.spriteDirection = PlayerDirection.RIGHT;
		this.dx = 0; this.dx = 0;
	}

	public void update(Arena arena) {
		hitBox.update();
		feetBox.update();

		switch(state){
		case WAIT : {
			if(arena.collidesY(feetBox)!=BoxCollision.ABOVE) { state = PlayerState.FALL; }
			move();
			break;
		}
		case RUN : {
			if(arena.collidesY(feetBox)!=BoxCollision.ABOVE) { state = PlayerState.FALL; }
			else {
				if(movDirection==PlayerDirection.RIGHT) { dx += runForce; }
				else if(movDirection==PlayerDirection.LEFT) { dx -= runForce; }
			}
			if(dx>0) { spriteDirection = PlayerDirection.RIGHT; }
			if(dx<0) { spriteDirection = PlayerDirection.LEFT; }
			move();
			break;
		}
		case JUMP : {
			if(arena.collidesY(hitBox)==BoxCollision.DOWN) {
				dy=0;
			}
			if(dy<0) {
				state = PlayerState.FALL;
			}
			else {
				dy += gravity;
			}
			if((arena.collidesX(hitBox)==BoxCollision.LEFT||arena.collidesX(feetBox)==BoxCollision.LEFT)&&movDirection==PlayerDirection.RIGHT) {
				movDirection = PlayerDirection.NO;
				spriteDirection = PlayerDirection.LEFT;
				state = PlayerState.ONWALL;
				dx=0;
			}
			else if((arena.collidesX(hitBox)==BoxCollision.RIGHT||arena.collidesX(feetBox)==BoxCollision.RIGHT)&&movDirection==PlayerDirection.LEFT) {
				movDirection = PlayerDirection.NO;
				spriteDirection = PlayerDirection.RIGHT;
				state = PlayerState.ONWALL;
				dx=0;
			}
			else {
				if(movDirection==PlayerDirection.RIGHT) { dx += runForce*airSpeed; }
				else if(movDirection==PlayerDirection.LEFT) { dx -= runForce*airSpeed; }
			}
			move();
			break;
		}
		case ONWALL : {
			if(arena.collidesX(hitBox)!=BoxCollision.LEFT&&arena.collidesX(hitBox)!=BoxCollision.RIGHT){
				state = PlayerState.FALL;
				dx += runForce*airSpeed;
			}
			else if(movDirection==PlayerDirection.LEFT&&spriteDirection==PlayerDirection.LEFT){
				state = PlayerState.FALL;
				dx -= runForce*airSpeed;
			}
			else if(movDirection==PlayerDirection.RIGHT&&spriteDirection==PlayerDirection.RIGHT){
				state = PlayerState.FALL;
			}
			dy += gravity;
			dy *= wallFriction;
			move();
			break;
		}
		case FALL : {
			if(arena.collidesY(feetBox)==BoxCollision.ABOVE) {
				if(movDirection==PlayerDirection.NO) { state = PlayerState.WAIT; }
				else { state = PlayerState.RUN; }
				dy = 0;
			}
			else {
				dy += gravity;
			}
			if((arena.collidesX(hitBox)==BoxCollision.RIGHT||arena.collidesX(feetBox)==BoxCollision.RIGHT)&&movDirection==PlayerDirection.LEFT) {
				movDirection = PlayerDirection.NO;
				spriteDirection = PlayerDirection.RIGHT;
				state = PlayerState.ONWALL;
				dx=0;
			}
			else if((arena.collidesX(hitBox)==BoxCollision.LEFT||arena.collidesX(feetBox)==BoxCollision.LEFT)&&movDirection==PlayerDirection.RIGHT) {
				movDirection = PlayerDirection.NO;
				spriteDirection = PlayerDirection.LEFT;
				state = PlayerState.ONWALL;
				dx=0;
			}
			else {
				if(movDirection==PlayerDirection.RIGHT) { dx += runForce*airSpeed; }
				else if(movDirection==PlayerDirection.LEFT) { dx -= runForce*airSpeed; }
			}
			move();
			break;
		}
		case PUNCH : {

			break;
		}
		}
	}

	private void move() {
		dx *= horizontalDamping;
		dy *= verticalDamping;
		pos.x += dx;
		pos.y += dy;
	}

	public void jump() {
		if(state==PlayerState.WAIT||state==PlayerState.RUN) {
			state = PlayerState.JUMP;
			dy += jumpForce;
		}
		if(state==PlayerState.ONWALL){
			if(spriteDirection==PlayerDirection.LEFT) {
				state = PlayerState.JUMP;
				dy += jumpForce*Math.sin(wallJumpAngle);
				dx -= jumpForce*Math.cos(wallJumpAngle);
				movDirection = PlayerDirection.LEFT;
			}
			else if(spriteDirection==PlayerDirection.RIGHT) {
				state = PlayerState.JUMP;
				dy += jumpForce*Math.sin(wallJumpAngle);
				dx += jumpForce*Math.cos(wallJumpAngle);
				movDirection = PlayerDirection.RIGHT;
			}
		}
	}

	public void goLeft() {
		if(state==PlayerState.WAIT) { state = PlayerState.RUN; }
		movDirection = PlayerDirection.LEFT;
	}

	public void stopLeft() {
		if(movDirection==PlayerDirection.LEFT) {
			state = PlayerState.WAIT; movDirection = PlayerDirection.NO;
		}
	}

	public void goRight() {
		if(state==PlayerState.WAIT) { state = PlayerState.RUN; }
		movDirection = PlayerDirection.RIGHT;
	}

	public void stopRight() {
		if(movDirection==PlayerDirection.RIGHT) {
			state = PlayerState.WAIT;
			movDirection = PlayerDirection.NO;
		}
	}

	public void paint(int offX, int offY, Graphics g){
		g.setColor(Color.cyan);
		hitBox.paint(offX, offY, g);
		g.setColor(Color.green);
		feetBox.paint(offX, offY, g);
		g.setColor(Color.lightGray);
		g.drawString("(state) "+state.name(), 10, 20);
		g.drawString("(movDir) "+movDirection.name(), 10, 40);
		g.drawString("(spritDir) "+spriteDirection.name(), 10, 60);
	}
}
