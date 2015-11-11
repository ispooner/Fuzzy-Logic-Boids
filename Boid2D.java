package application;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

enum BoidType
{
	Flock,						//These boids will flock together, evade predators, and avoid obstacles.
	Predator,					//These boids won't flock well, but will chase the closest flocker and avoid obstacles.
	Obstacle					//These boids will sit and do nothing. They are an obstacle for the other boids to avoid.
}

public class Boid2D{

	private Shape shape;		//The shape of this boid. Will change with each boid type.
	private double maxSpeed;	//The maximum speed of this boid.
	private double minSpeed;	//The minimum speed of this boid.

	private final double maxForce = 3.0; 	//This value is used to determine how much effect the total forces have on the boid.
											//This value may be split up so that each force may have a different "weight"
	
	private Point2D velocity;	//The current direction and speed of this boid.
	private Point2D steering;	//The amount of change added to the velocity on each frame.
	private Point2D seekForce;	//The current seek force for this boid. It will seek the center of mass of the boids around it.
	private Point2D fleeForce;	//The current flee force. It will flee from predators.(May change to evade force later.)
	private Point2D avoidForce;	//The current avoid force. Used to avoid Obstacle boids.
	
	//The random wander force of each boid. This will likely be the only random force directly acting on the boids.
	private Point2D wanderForce;
	private double wanderAngle; //Need to store this so the boid doesn't twitch randomly.
	private final double angleChange = 10; //Determines how much the wanderAngle can change between frames. Tweaking this value will produce more realistic wandering behavior.
	
	private double close; 		//Used to determine how close another boid is. The closer a boid, the effect it will have on this boid.
								/*
								 * This is where fuzzy logic comes in. How close something is defines an inaccurate measurement based on 
								 * perception. If there are no boids nearby, then this boid becomes lonely in a sense and should begin
								 * to seek out boids that are further away. If there are a lot of boids nearby then the effect of each 
								 * boid should drop off since there are so many. 
								 */
	
	private BoidType type;		//Used to define the behavior of this boid. Behaviors are defined above.
	
	//Initialize this boids values to the given parameters
	public Boid2D(Shape s, double maxS, double minS, Point2D v, BoidType t)
	{
		shape = s;
		maxSpeed = maxS;
		minSpeed = minS;
		velocity = v;
		type = t;
		
		steering = Point2D.ZERO;
		seekForce = Point2D.ZERO;
		fleeForce = Point2D.ZERO;
		avoidForce = Point2D.ZERO;
		wanderForce = Point2D.ZERO;
		wanderAngle = velocity.angle(0, 1);
		
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
