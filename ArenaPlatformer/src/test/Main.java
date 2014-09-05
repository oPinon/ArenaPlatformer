package test;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import game.*;
import physics.*;

public class Main extends Application {
	
	static int x = 0;
	static long previousTime = 0;

    @Override
    public void start(final Stage primaryStage) {

        Group root = new Group();
        Scene scene = new Scene(root, 600, 400);
        
        final Canvas canvas = new Canvas();
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
        
        final GraphicsContext ctx = canvas.getGraphicsContext2D();
        
        final Image sprite = new Image("file:sprites/guyFall/wait/right/0.png");
        ctx.drawImage(sprite, x, 30);
        
        root.getChildren().add(canvas);
        
        primaryStage.setTitle("ArenaPlatformer");
        primaryStage.setScene(scene);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreen(true);
        primaryStage.show();
        scene.setCursor(Cursor.NONE);
        
        new AnimationTimer() {
        	@Override
        	public void handle(long time) {
        		x+=3;
        		if(x>canvas.getWidth()) { x = 0; }
        		
        		ctx.setFill(Color.BEIGE);
        		ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        		ctx.drawImage(sprite, x, 0);
        		String dt = (time-previousTime)/1000000+" ms";
        		ctx.setFill(Color.BLACK);
        		ctx.fillText(dt, 10, 20);
        		previousTime = time;
        	}
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
