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
	public void setRotation(double newRotation)
	{
		shape.setRotate(newRotation);
		//System.out.println(shape.getRotate());
	}
	
	public void setDirection(Point2D direction)
	{
		setRotation(Math.toDegrees(Math.atan2(direction.getX(), -direction.getY())));
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
		ArrayList<Double> angles = new ArrayList<Double>();
		Double[] a = new Double[0];
		for(Boid2D boid : boids)
		{
			angles.add(boid.getRotation());
		}
		
		double steer = averageAngles(angles.toArray(a)) - getRotation();
		
		steer *= .1;
		
		setRotation(getRotation() + steer);	
	}
	
	public double averageAngles(Double[] angles)
	{
		double x = 0, y = 0;
		for(int i = 0; i < angles.length; i++)
		{
			x += Math.cos(Math.toRadians(angles[i]));
			y += Math.sin(Math.toRadians(angles[i]));
		}
		
		return Math.toDegrees(Math.atan2(y, x));
	}
}
