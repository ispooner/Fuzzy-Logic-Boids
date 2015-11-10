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
import javafx.scene.text.Text;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Group root = new Group();
			Scene scene = new Scene(root,1024,768);
			
			//The first boid prototype. 
			// TODO create a class for boids that implements boidal behaviour
			// Flock boid shape(0, 25, 10, 0, 20, 25, 10, 19);
			double boidSpeed = 5; //Represents the speed of each boid. I am planning on creating boids that can move faster to intercept others.
			
			ArrayList<Boid2D> boids = new ArrayList<Boid2D>(); //arraylist of all boids currently alive.
			ArrayList<KeyCode> keys = new ArrayList<KeyCode>();//array list of keys that are currently pressed.
			
			for(int i = 0; i < 1; i++)
			{
				boids.add(new Boid2D(new Polygon(-10, 13, 0, -12, 10, 13, 0, 7), Math.random() * 360, 5, 5, 3, 10, Math.random() * scene.getWidth(), Math.random() * scene.getHeight(), BoidType.Flock));
			}
			
			for(Boid2D boid : boids)
			{
				root.getChildren().add(boid.getShape());
			}
			
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
						for(Boid2D boid : boids)
						{
							boid.getShape().setVisible(!boid.getShape().isVisible());
						}
					}
				}
			});
			
			Text mode = new Text();
			
			root.getChildren().add(mode);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			new AnimationTimer()
			{

				@Override
				public void handle(long arg0) {
					for(Boid2D boid : boids)
					{
//						for(KeyCode state : keys)
//						{
//							if(state == KeyCode.A)
//							{
//								boid.setRotation(270);
//							}
//							if(state == KeyCode.W)
//							{
//								boid.setRotation(0);
//							}
//							if(state == KeyCode.D)
//							{
//								boid.setRotation(90);
//							}
//							if(state == KeyCode.S)
//							{
//								boid.setRotation(180);
//							}
//						}
						//boid.calculateMove(boids);
						boid.move();
						if(boid.getShape().getTranslateX() > scene.getWidth())
						{
							boid.getShape().setTranslateX(0);
						}
						if(boid.getShape().getTranslateX() < 0)
						{
							boid.getShape().setTranslateX(scene.getWidth());
						}
						if(boid.getShape().getTranslateY() > scene.getHeight())
						{
							boid.getShape().setTranslateY(0);
						}
						if(boid.getShape().getTranslateY() < 0)
						{
							boid.getShape().setTranslateY(scene.getHeight());
						}
					}
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
