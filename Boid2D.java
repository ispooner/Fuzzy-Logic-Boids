package application;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;
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
	
	private Point2D bounds;		//The bounding frame. Used to wrap the boids back onto the screen Asteroids style.
	private Point2D velocity;	//The current direction and speed of this boid.
	private Point2D steering;	//The amount of change added to the velocity on each frame.
	private Point2D seekForce;	//The current seek force for this boid. It will seek the center of mass of the boids around it.
	private Point2D fleeForce;	//The current flee force. It will flee from predators.(May change to evade force later.)
	private Point2D avoidForce;	//The current avoid force. Used to avoid Obstacle boids.
	
	//The random wander force of each boid. This will likely be the only random force directly acting on the boids.
	private Point2D wanderForce;
	private double wanderAngle; //Need to store this so the boid doesn't twitch randomly.
	private final double angleChange = 15; //Determines how much the wanderAngle can change between frames. Tweaking this value will produce more realistic wandering behavior.
	
	private double close; 		//Used to determine how close another boid is. The closer a boid, the effect it will have on this boid.
								/*
								 * This is where fuzzy logic comes in. How close something is defines an inaccurate measurement based on 
								 * perception. If there are no boids nearby, then this boid becomes lonely in a sense and should begin
								 * to seek out boids that are further away. If there are a lot of boids nearby then the effect of each 
								 * boid should drop off since there are so many. 
								 */
	
	private BoidType type;		//Used to define the behavior of this boid. Behaviors are defined above.
	
	//getters for all the variables I need to get.
	public Shape getShape()
	{
		return shape;
	}
	
	public Point2D getVelocity()
	{
		return velocity;
	}
	
	public Point2D getPosition()
	{
		return new Point2D(shape.getTranslateX(), shape.getTranslateY());
	}
	
	//Setters for variables.
	public void setVelocity(Point2D v)
	{
		//I am checking to make sure that the boid will continue to move within the speed limits.
		if(v.magnitude() < minSpeed)
		{
			v = v.normalize().multiply(minSpeed);
		}
		if(v.magnitude() > maxSpeed)
		{
			v = v.normalize().multiply(maxSpeed);
		}
		
		velocity = v;
		
		shape.setRotate(Math.toDegrees(Math.atan2(velocity.getX(), -velocity.getY())));
	}
	
	public void setPosition(Point2D p)
	{
		shape.setTranslateX(p.getX() % bounds.getX());
		shape.setTranslateY(p.getY() % bounds.getY());
		if(shape.getTranslateX() < 0)
		{
			shape.setTranslateX(bounds.getX());
		}
		
		if(shape.getTranslateY() < 0)
		{
			shape.setTranslateY(bounds.getY());;
		}
	}
	
	//Initialize this boids values to the given parameters
	public Boid2D(Shape s, double maxS, double minS, Point2D b, Point2D v, Point2D p, BoidType t, Paint c)
	{
		shape = s;
		maxSpeed = maxS;
		minSpeed = minS;
		bounds = b;
		setVelocity(v);
		setPosition(p);
		type = t;
		shape.setFill(c);
		
		steering = Point2D.ZERO;
		seekForce = Point2D.ZERO;
		fleeForce = Point2D.ZERO;
		avoidForce = Point2D.ZERO;
		wanderForce = Point2D.ZERO;
		wanderAngle = velocity.angle(0, 1); //I set the wanderAngle to the angle of the velocity so the boid moves straight at first.
		
	}
	
	/*
	 * The move function will take the array of boids and calculate this boid's movement based on the positions and
	 * velocities of the boids around it. This function will be called on all boids. 
	 */
	public void move(ArrayList<Boid2D> boids)
	{
		Boid2D[] c = closeBoids(boids);
		steering = Point2D.ZERO;
		steering = steering.add(seek(c));
		steering = steering.add(flee(c));
		steering = steering.add(avoid(c));
		steering = steering.add(wander());
		
		if(steering.magnitude() > maxForce)
		{
			steering = steering.normalize().multiply(maxForce);
		}
		
		setVelocity(getVelocity().add(steering));
		
		setPosition(getPosition().add(velocity));
	}
	
	private Boid2D[] closeBoids(ArrayList<Boid2D> boids)
	{
		Boid2D[] c = new Boid2D[0];
		return boids.toArray(c);
	}
	
	//Calculates the seekForce for the center of all close boids.
	private Point2D seek(Boid2D[] boids)
	{
		
		return seekForce;
	}
	
	//Calculates the flee force from predators and boids that are too close.
	private Point2D flee(Boid2D[] boids)
	{
		
		return fleeForce;
	}
	
	//Calculates the force of avoiding obstacles in the way of the boid.
	private Point2D avoid(Boid2D[] boids)
	{
		
		return avoidForce;
	}
	
	//Calculates how much the boid will randomly wander this update.
	/*
	 * This function uses a "circle" in front of the boid represented by vectors to change the velocity of
	 * the boid over time. 
	 */
	private final double circleScale = 2;
	private final double circleRadius = 1;
	private Point2D wander()
	{
		Point2D circleCenter = velocity.normalize().multiply(circleScale);
		Point2D circleDisplacement = new Point2D(Math.cos(Math.toRadians(wanderAngle)), Math.sin(Math.toRadians(wanderAngle)));
		
		wanderAngle += ((Math.random() * angleChange) - (angleChange * .5));
		
		wanderForce = circleCenter.add(circleDisplacement);
		
		return wanderForce;
	}
	
	
	
	/*
	 * averageVelocities will average an array of velocities by adding them all together and 
	 * dividing by the total number of velocities. Used in the seek behavior to help align the boids to eachother.
	 */
	public Point2D averageVelocities(Point2D[] velocities)
	{
		Point2D ave = Point2D.ZERO;
		for(int i = 0; i < velocities.length; i++)
		{
			ave = ave.add(velocities[i]);
		}
		return ave.multiply(1/velocities.length);
	}
}
