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
	private boolean isFullscreen;
	private boolean isPaused;
	private GameLoop gameLoop;
	private Stage stage;

    @Override
    public void start(final Stage primaryStage) {
    	
        Group root = new Group();
        Scene scene = new Scene(root, 600, 400);
        
        this.stage = primaryStage;
        
        this.canvas = new Canvas();
        this.canvas.widthProperty().bind(scene.widthProperty());
        this.canvas.heightProperty().bind(scene.heightProperty());
        
        this.arena = new Arena();
        this.player = new Player();
        this.camera = new Camera(player.pos,canvas.widthProperty(),canvas.heightProperty());
		
		root.getChildren().add(this.canvas);
        
        primaryStage.setTitle("ArenaPlatformer");
        primaryStage.setScene(scene);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreen(true);
        this.isFullscreen = true;
        primaryStage.show();
        scene.setCursor(Cursor.NONE);
        
        scene.setOnKeyPressed(new ControlsPressed(player,this));
        scene.setOnKeyReleased(new ControlsReleased(player));
        
        this.gameLoop = new GameLoop(this);
        this.gameLoop.start();
        this.isPaused = false;
    }
    
    public void repaint() {
		
    	double width = canvas.getWidth(), height = canvas.getHeight();
    	GraphicsContext g = canvas.getGraphicsContext2D();
    	
		if(player.pos.y<-500) { reset(); }
		player.update(arena);
		camera.update(arena.bounds);
		updateFPS();
    	
    	g.setFill(Color.GREEN);
		g.fillRect(0, 0, width, height);
		
		g.setFill(Color.DARKGRAY);
		arena.paint(camera.getXOffset(),camera.getYOffset(), width, height ,g);
		
		g.setFill(Color.GREEN);
		player.paint(camera.getXOffset(),camera.getYOffset(), g, arena);
		
		g.setFill(Color.DARKGRAY);
		//g.drawString("x:"+arena.collidesX(player.feetBox).name(), width/2-13, height/2);
		//g.drawString("y:"+arena.collidesY(player.feetBox).name(), width/2-13, height/2+10);
		
		g.setFill(Color.WHITE);
		g.fillText("fps : "+fps, width-60, 20);

		//Toolkit.getDefaultToolkit().sync();
    }
    
    public void switchFullscreen() { this.isFullscreen = !this.isFullscreen; this.stage.setFullScreen(this.isFullscreen); }
    public void startPause() {
    	if(this.isPaused) { this.gameLoop.stop(); }
    	else { this.gameLoop.start(); }
    	this.isPaused = !this.isPaused;
    }
    
	private void updateFPS() {
		fps = (int) (0.9*fps + 0.1*1000/Math.max((System.currentTimeMillis()-time),1));
		time = System.currentTimeMillis();
	}
	
	public void reset() { this.player.pos.x = 0; this.player.pos.y = 200; }

    public static void main(String[] args) {
        launch(args);
    }
}

class ControlsPressed implements EventHandler<KeyEvent>{

	private Player player;
	private Game game;
	
	public ControlsPressed(Player player, Game game) {
		this.player = player;
		this.game = game;
	}
	
	@Override
	public void handle(KeyEvent e) {
		if(e.getCode()==KeyCode.LEFT) { player.goLeft(); }
		else if(e.getCode()==KeyCode.RIGHT) { player.goRight(); }
		else if(e.getCode()==KeyCode.SPACE) { player.jump(); }
		else if(e.getCode()==KeyCode.F) { game.switchFullscreen(); }
		else if(e.getCode()==KeyCode.CONTROL) { game.startPause(); }
		else if(e.getCode()==KeyCode.DELETE) { game.reset(); }
		else if(e.getCode()==KeyCode.ESCAPE) { System.exit(0); }
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
