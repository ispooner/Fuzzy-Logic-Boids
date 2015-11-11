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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;


public class Main extends Application {

	Boolean mouseLeft = false;
	Boolean mouseRight = false;
	Point2D target = Point2D.ZERO;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Group root = new Group();
			Scene scene = new Scene(root,1024,768);
			
			//The first boid prototype. 
			// Flock boid shape(-10, 13, 0, -12, 10, 13, 0, 7);
			
			ArrayList<Boid2D> boids = new ArrayList<Boid2D>(); //arraylist of all boids currently alive.
			ArrayList<KeyCode> keys = new ArrayList<KeyCode>();//array list of keys that are currently pressed.
			
			for(int i = 0; i < 10; i++)
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
						
			scene.setOnMousePressed(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent arg0) 
				{
					target = new Point2D(arg0.getSceneX(), arg0.getSceneY());
					setLeft(arg0.isPrimaryButtonDown());
					setRight(arg0.isSecondaryButtonDown());
				}
			});
			
			scene.setOnMouseDragged(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent event)
				{
					target = new Point2D(event.getSceneX(), event.getSceneY());
				}
			});
			
			scene.setOnMouseReleased(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent event)
				{
					setLeft(event.isPrimaryButtonDown());
					setRight(event.isSecondaryButtonDown());
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
						
						boid.calculateMove(boids);
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
						
						Point2D currentVelocity = new Point2D(Math.sin(Math.toRadians(boid.getRotation())) * boid.getSpeed(),
														   -Math.cos(Math.toRadians(boid.getRotation())) * boid.getSpeed());
						double maxForce = 10;
						//implement a seek steering behavior
						if(mouseLeft)
						{
							Point2D desiredVelocity = new Point2D(target.getX() - boid.getShape().getTranslateX(), target.getY() - boid.getShape().getTranslateY());
							desiredVelocity = desiredVelocity.normalize().multiply(boid.getSpeed());
							
							Point2D steeringForce = desiredVelocity.subtract(currentVelocity);
							
							steeringForce = steeringForce.multiply(1/maxForce);
							
							
							boid.setDirection(currentVelocity.add(steeringForce));
						}
						//implement a flee steering behavior.
						else if(mouseRight)
						{
							
						}
						//implement a wandering behavior.
						else
						{
							double ANGLE_CHANGE = 5.0;
							Point2D circleCenter = new Point2D(Math.sin(Math.toRadians(boid.getRotation())),
														   -Math.cos(Math.toRadians(boid.getRotation())));
							circleCenter = circleCenter.multiply(2);
							Point2D displacement = new Point2D(0, 1);
							displacement = displacement.multiply(1);
							
							
							
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
	
	public void setLeft(Boolean b)
	{
		mouseLeft = b;
	}
	
	public void setRight(Boolean b)
	{
		mouseRight = b;
	}
}
