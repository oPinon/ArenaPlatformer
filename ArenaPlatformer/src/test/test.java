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
	private Player player;
	private Arena arena;
	private Camera camera;
	
	public test(int width, int height) {
		
		arena = new Arena();
		player = new Player();
		camera = new Camera(player.pos,width,height);
		
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
		player.paint(camera.getXOffset(),camera.getYOffset(), g);
		
		g.setColor(Color.darkGray);
		//g.drawString("x:"+arena.collidesX(player.feetBox).name(), width/2-13, height/2);
		//g.drawString("y:"+arena.collidesY(player.feetBox).name(), width/2-13, height/2+10);
		
		Toolkit.getDefaultToolkit().sync();
        g.dispose();
	}
	
	public void actionPerformed(ActionEvent arg0) {
		repaint();
		if(player.pos.y<-500) { player = new Player(); camera.target = player.pos; }
		player.update(arena);
		camera.update();
	}
	
	public class Controller implements KeyListener {
		
		static final int step = 10;
		
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_LEFT) { player.goLeft(); }
			else if(e.getKeyCode()==KeyEvent.VK_RIGHT) { player.goRight(); }
			else if(e.getKeyCode()==KeyEvent.VK_SPACE) { player.jump(); }
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_LEFT) { player.stopLeft(); }
			else if(e.getKeyCode()==KeyEvent.VK_RIGHT) {  player.stopRight(); }
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}

	}
}

