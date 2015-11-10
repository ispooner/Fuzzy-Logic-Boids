package application;
	
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Polygon;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Group root = new Group();
			Scene scene = new Scene(root,400,400);
			
			//The first boid prototype. 
			// TODO create a class for boids that implements boidal behaviour
			Polygon boid = new Polygon(50, 50, 60, 25, 70, 50, 60, 44);
			Point2D boidDirection= new Point2D(0, -1); //currently not used. Will represent the direction of the boid.
			double boidSpeed = 5; //Represents the speed of each boid. I am planning on creating boids that can move faster to intercept others.
			
			ArrayList<KeyCode> keys = new ArrayList<KeyCode>();
			
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() 
			{

				@Override
				public void handle(KeyEvent event) 
				{
					if(!keys.contains(event.getCode()))
					{
						keys.add(event.getCode());
						//System.out.println(event.getCode());
					}
				}
			});
			
			scene.setOnKeyReleased(new EventHandler<KeyEvent>()
			{
				@Override
				public void handle(KeyEvent event)
				{
					keys.remove(event.getCode());
					//System.out.println(event.getCode());
					if(event.getCode() == KeyCode.X)
					{
						boid.setVisible(!boid.isVisible());
					}
				}
			});
			
			root.getChildren().add(boid);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
			new AnimationTimer()
			{

				@Override
				public void handle(long arg0) {
					for(KeyCode state : keys)
					{
						//System.out.println(keys);
						if(state == KeyCode.A)
						{
							boid.setRotate((boid.getRotate() - 5) % 360);
						}
						if(state == KeyCode.W)
						{
							boid.setTranslateX(boid.getTranslateX() + Math.sin(Math.toRadians(boid.getRotate())) * boidSpeed);
							boid.setTranslateY(boid.getTranslateY() - Math.cos(Math.toRadians(boid.getRotate())) * boidSpeed);
						}
						if(state == KeyCode.D)
						{
							boid.setRotate((boid.getRotate() + 5) % 360);
						}
						if(state == KeyCode.S)
						{
							boid.setTranslateX(boid.getTranslateX() - Math.sin(Math.toRadians(boid.getRotate())) * boidSpeed);
							boid.setTranslateY(boid.getTranslateY() + Math.cos(Math.toRadians(boid.getRotate())) * boidSpeed);
						}
					}
					System.out.println(boid.getRotate());					
				}
				
			}.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
