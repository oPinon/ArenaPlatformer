package test;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import game.*;
import physics.*;

public class Game extends Application {
	
	private Player player;
	private Arena arena;
	private Camera camera;
	private int fps = 60;
	private long time;
	private Canvas canvas;

    @Override
    public void start(final Stage primaryStage) {
    	
        Group root = new Group();
        Scene scene = new Scene(root, 600, 400);
        
        canvas = new Canvas();
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
        
    	arena = new Arena();
		player = new Player();
		camera = new Camera(player.pos,canvas.widthProperty(),canvas.heightProperty());
		
		root.getChildren().add(canvas);
        
        primaryStage.setTitle("ArenaPlatformer");
        primaryStage.setScene(scene);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreen(true);
        primaryStage.show();
        scene.setCursor(Cursor.NONE);
        
        scene.setOnKeyPressed(new ControlsPressed(player));
        scene.setOnKeyReleased(new ControlsReleased(player));
        
        new GameLoop(this).start();
    }
    
    public void repaint() {
		
    	double width = canvas.getWidth(), height = canvas.getHeight();
    	GraphicsContext g = canvas.getGraphicsContext2D();
    	
		if(player.pos.y<-500) { reset(); }
		player.update(arena);
		camera.update();
		updateFPS();
    	
    	g.setFill(Color.LIGHTGRAY);
		g.fillRect(0, 0, width, height);
		
		g.setFill(Color.DARKGRAY);
		arena.paint(camera.getXOffset(),camera.getYOffset(),g);
		
		g.setFill(Color.GREEN);
		player.paint(camera.getXOffset(),camera.getYOffset(), g, arena);
		
		g.setFill(Color.DARKGRAY);
		//g.drawString("x:"+arena.collidesX(player.feetBox).name(), width/2-13, height/2);
		//g.drawString("y:"+arena.collidesY(player.feetBox).name(), width/2-13, height/2+10);
		
		g.setFill(Color.WHITE);
		g.fillText("fps : "+fps, width-60, 20);

		//Toolkit.getDefaultToolkit().sync();
    }
    
	private void updateFPS() {
		fps = (int) (0.9*fps + 0.1*1000/Math.max((System.currentTimeMillis()-time),1));
		time = System.currentTimeMillis();
	}
	
	private void reset() { player = new Player(); camera.target = player.pos; }

    public static void main(String[] args) {
        launch(args);
    }
}

class ControlsPressed implements EventHandler<KeyEvent>{

	private Player player;
	
	public ControlsPressed(Player player) {
		this.player = player;
	}
	
	@Override
	public void handle(KeyEvent e) {
		if(e.getCode()==KeyCode.LEFT) { player.goLeft(); }
		else if(e.getCode()==KeyCode.RIGHT) { player.goRight(); }
		else if(e.getCode()==KeyCode.SPACE) { player.jump(); }
	}	
}

class ControlsReleased implements EventHandler<KeyEvent>{

	private Player player;
	public ControlsReleased(Player player) { this.player = player; }
	
	@Override
	public void handle(KeyEvent e) {
		if(e.getCode()==KeyCode.LEFT) { player.stopLeft(); }
		else if(e.getCode()==KeyCode.RIGHT) {  player.stopRight(); }
	}
	
}

class GameLoop extends AnimationTimer {

	private Game game;
	public GameLoop(Game game) { this.game = game; }
	
	@Override
	public void handle(long arg0) {
		game.repaint();
	}
	
}
