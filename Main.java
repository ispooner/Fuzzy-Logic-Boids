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
import javafx.scene.paint.Color;
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
			Scene scene = new Scene(root,1600, 900);
			
			// Flock boid shape(-10, 13, 0, -12, 10, 13, 0, 7);
			
			ArrayList<Boid2D> boids = new ArrayList<Boid2D>(); //arraylist of all boids currently alive.
			ArrayList<KeyCode> keys = new ArrayList<KeyCode>();//array list of keys that are currently pressed.
			
			for(int i = 0; i < 50; i++)
			{
				boids.add(new Boid2D(new Polygon(-10, 13, 0, -12, 10, 13, 0, 7), 8.0, 3.0, new Point2D(scene.getWidth(), scene.getHeight()),
						new Point2D(Math.random() * 5 - 2.5, Math.random() * 5 - 2.5), new Point2D(Math.random() * scene.getWidth(), Math.random() * scene.getHeight()),
						BoidType.Flock, Color.color(Math.random() * .5, Math.random(), Math.random() * .5)));
				root.getChildren().add(boids.get(i).getShape());
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
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
			new AnimationTimer()
			{

				@Override
				public void handle(long arg0) {
					for(Boid2D boid : boids)
					{
						boid.move(boids);
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
