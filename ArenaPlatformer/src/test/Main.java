package test;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(final Stage primaryStage) {

        Group root = new Group();
        Scene scene = new Scene(root, 500, 300);
        
        Canvas canvas = new Canvas();
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
        
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        ctx.setFill(Color.GREEN);
        ctx.fillRect(0, 0, 100, 100);
        
        Image sprite = new Image("file:sprites/guyFall/wait/right/0.png");
        ctx.drawImage(sprite, 0, 0);
        
        root.getChildren().add(canvas);
        
        primaryStage.setTitle("ArenaPlatformer");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
