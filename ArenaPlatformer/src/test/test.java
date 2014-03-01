package test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import game.*;
import physics.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class test extends JPanel implements ActionListener{

	public static void main(String[] args) {
		new test(1280,720);
	}

	private int width, height;
	private Position pos;
	private Arena arena;
	private Camera camera;
	private HitBox hitBox;
	private int dx, dy;
	
	public test(int width, int height) {
		
		arena = new Arena();
		pos = new Position(); pos.x = 0; pos.y = 0;
		camera = new Camera(pos,width,height);
		hitBox = new HitBox(pos,-15,15,-15,15);
		
		this.width=width; this.height=height;
		JFrame frame = new JFrame();
		frame.add(this);
		Controller controller = new Controller();
		frame.addKeyListener(controller);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		Timer timer = new Timer(16,this);
		timer.start();
	}

	public void paint(Graphics g) {

		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		
		g.setColor(Color.white);
		arena.paint(camera.getXOffset(),camera.getYOffset(),g);
		
		g.setColor(Color.green);
		hitBox.paint(camera.getXOffset(),camera.getYOffset(), g);
		
		g.setColor(Color.darkGray);
		g.drawString(("x = "+arena.collidesX(hitBox)), width/2-13, height/2);
		g.drawString(("y = "+arena.collidesY(hitBox)), width/2-13, height/2+10);
		
		Toolkit.getDefaultToolkit().sync();
        g.dispose();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		repaint();
		pos.x += dx;
		pos.y += dy;
		hitBox.update();
	}
	
	public class Controller implements KeyListener {
		
		static final int step = 3;
		
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_LEFT) { dx=-step; }
			else if(e.getKeyCode()==KeyEvent.VK_RIGHT) { dx=step; }
			else if(e.getKeyCode()==KeyEvent.VK_UP) { dy=step; }
			else if(e.getKeyCode()==KeyEvent.VK_DOWN) { dy=-step; }
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_LEFT) { if(dx==-step) dx=0; }
			else if(e.getKeyCode()==KeyEvent.VK_RIGHT) {  if(dx==step) dx=0; }
			else if(e.getKeyCode()==KeyEvent.VK_UP) {  if(dy==step) dy=0; }
			else if(e.getKeyCode()==KeyEvent.VK_DOWN) {  if(dy==-step) dy=0; }
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}

	}
}


