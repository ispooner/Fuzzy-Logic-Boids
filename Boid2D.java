package application;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

enum BoidType
{
	Flock,
	Predator,
	Obstacle
}

public class Boid2D{

	private Shape shape;
	private double speed;
	private double maxSpeed;
	private double minSpeed;
	private double maxRotate;
	private BoidType type;
	

	public BoidType getType()
	{
		return type;
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public double getRotation()
	{
		return shape.getRotate();
	}
	
	public Shape getShape()
	{
		return shape;
	}
	
	//changes the rotation by rotationChange
	public void setRotation(double rotationChange)
	{
		//I expect rotationChange to be how far and in what direction the boid wishes to go.
		shape.setRotate((shape.getRotate() + Math.max(-maxRotate, Math.min(maxRotate, rotationChange))) % 360);
		
		if(shape.getRotate() < 0)
		{
			shape.setRotate(shape.getRotate() + 360);
		}
		//System.out.println(shape.getRotate());
	}
	
	//sets the speed of the boid between two values.
	public void setSpeed(double newSpeed)
	{
		//I expect the input to be the final speed the boid wishes to travel at.
		//As such I am going to limit the highest and lowest speed of the boid here in the setter.
		speed = Math.max(minSpeed, Math.min(maxSpeed, newSpeed));
	}
	
	public Boid2D(Shape s, double r, double maxR, double s1, double maxS, double minS, double posX, double posY, BoidType t)
	{
		shape = s;
		shape.setRotate(r);
		shape.setTranslateX(posX);
		shape.setTranslateY(posY);
		maxRotate = maxR;
		speed = s1;
		maxSpeed = maxS;
		minSpeed = minS;
		type = t;
	}
	
	public void move()
	{
		shape.setTranslateX(shape.getTranslateX() + Math.sin(Math.toRadians(shape.getRotate())) * speed);
		shape.setTranslateY(shape.getTranslateY() - Math.cos(Math.toRadians(shape.getRotate())) * speed);
	}
	
	public void calculateMove(ArrayList<Boid2D> boids)
	{
		
	}
}