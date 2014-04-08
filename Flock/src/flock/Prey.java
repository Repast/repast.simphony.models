package flock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.vecmath.Vector3d;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.util.ContextUtils;

/**
 * Prey tries to stay close to his neighbors and avoid the Predator.
 * 
 * Adopted from the C# Swarm model by Daniel Greenheck: 
 *   http://greenhecktech.com/2014/04/03/throwback-thursday-swarm-intelligence/
 * 
 * @author Eric Tatara
 * 
 */
public class Prey extends Boid{
	private ArrayList<Prey> neighbors;       // List of neighboring Prey
	private ArrayList<Predator> predators;   // List of Predators

	public Prey(){
		neighbors = new ArrayList<Prey>();
		predators = new ArrayList<Predator>();
	}
	
	/**
	 * Prey initialization.
	 */
	@ScheduledMethod(start=0)
	public void init(){
		Parameters param = RunEnvironment.getInstance().getParameters();
		Context context = ContextUtils.getContext(this);
		ContinuousSpace space = (ContinuousSpace)context.getProjection("Space");
		
		// Find initial neighboring Prey to build a list of the closet Prey. 
		//  This list will be remain unchanged during the simulation and, 
		//  although this is somewhat of an approximation to the Prey behavior,
		//  the computational efficiency of not needing to find neighbors each
		//  step allows the simulation to run very fast.
		
		// Continuous space query for all Prey within a certain distance.
		int initialPreyNeighborDistance = (Integer)param.getValue("initialPreyNeighborDistance");
		ContinuousWithin query = new ContinuousWithin(context, this, initialPreyNeighborDistance);
		
		NdPoint q = space.getLocation(this);
		Vector3d position = new Vector3d(q.getX(), q.getY(), q.getZ());
		
		TreeMap<Double,Prey> foundNeigh = new TreeMap<Double,Prey>();
		for (Object o : query.query()){
			Prey neigh = null;
			if (o instanceof Prey)
				neigh = (Prey)o;
			else
				continue;
			
			NdPoint p = space.getLocation(neigh);
			Vector3d neighPosition = new Vector3d(p.getX(), p.getY(), p.getZ());
			Vector3d distanceToNeighbor = new Vector3d();
			distanceToNeighbor.sub(neighPosition, position);
			foundNeigh.put(distanceToNeighbor.length(), neigh);
		}
		
		// Now select only the closest neighbors
		int maxPreyNeighbors = (Integer)param.getValue("maxPreyNeighbors");
		int count = 0;
		for (Map.Entry<Double, Prey> e : foundNeigh.entrySet()){
			neighbors.add(e.getValue());
			
			count++;
			if (count >= maxPreyNeighbors) break;
		}
		
		// Initially find the predators and keep track of them here so each
		//   prey doesn't need to re-check each time.
		Iterator<Predator> iter = context.getAgentLayer(Predator.class).iterator();
		context.getObjects(Predator.class);
		while (iter.hasNext()){
			predators.add(iter.next());
		}
	}
	
	@ScheduledMethod(start=1, interval=1)
	public void update(){
		Parameters param = RunEnvironment.getInstance().getParameters();
		
		// A smaller time scale results in smoother movement, but over a shorter
		//  distance.  Reduce to speed up simluation speed.
		double timeScale = (Double)param.getValue("timeScale");
		
		// Vector which will modify the boids velocity vector
		Vector3d velocityUpdate = new Vector3d();     

		// Calculate Attraction and Avoidance for Neighbors
		// Calculate the avoidance vector for each boid
		Context context = ContextUtils.getContext(this);
		ContinuousSpace space = (ContinuousSpace)context.getProjection("Space");
		
		NdPoint q = space.getLocation(this);
		Vector3d position = new Vector3d(q.getX(), q.getY(), q.getZ());
		
		// Smaller cloth give a more organized, cloth like look
		double preySpacing = (Double)param.getValue("preySpacing");
		
		// Attract force for prey
		double preyAttractForce = (Double)param.getValue("preyAttractForce");
		
		// Higher repulsion force gives more randomness
		double preyRepelForce = (Double)param.getValue("preyRepelForce");
		
		for (Prey neigh : neighbors){	
			NdPoint p = space.getLocation(neigh);
			
			Vector3d neighPosition = new Vector3d(p.getX(), p.getY(), p.getZ());
			Vector3d distanceToNeighbor = new Vector3d();
			distanceToNeighbor.sub(neighPosition, position);
			
			
			// If it's too far away, go chase it
			if ( distanceToNeighbor.lengthSquared() >= preySpacing * preySpacing){
				distanceToNeighbor.scale(preyAttractForce);
				velocityUpdate.add(distanceToNeighbor);
			}
			// If it's too close, fly away from it
			else{
				distanceToNeighbor.scale(preyRepelForce);
				velocityUpdate.add(distanceToNeighbor);
			}
		}

		// Avoid the predators
		// Loop through each predator and get the vector to it
		
		// The distance a starling can see a falcon
		double preyFearRadius = (Double)param.getValue("preyFearRadius");
		
		// Repulsion force for predator
		double preyFearForce = (Double)param.getValue("preyFearForce");
		
		for (Predator pred : predators){
			// Get the vector from this boid to its neighbor
			Vector3d distanceToFalcon = new Vector3d();
			
			NdPoint p = space.getLocation(pred);
			Vector3d predPosition = new Vector3d(p.getX(), p.getY(), p.getZ());
			
			distanceToFalcon.sub(predPosition, position);

			// If it is within range, add the vector to the repulsion vector
			if ( distanceToFalcon.lengthSquared() < preyFearRadius * preyFearRadius){
				// The closer the predator is, the more weight it will have
				Vector3d v = new Vector3d(0,0,0);
				v.normalize(distanceToFalcon);
				v.scale(preyFearForce);
				velocityUpdate.add(v);
			}
		}

		// Update the velocity of the boid
		// Apply the update to the velocity
		double preyAcceleration = (Double)param.getValue("preyAcceleration");
		velocityUpdate.scale(preyAcceleration * timeScale);
		
		velocity.add(velocityUpdate); 

		// If our velocity vector exceeds the max speed, throttle it back to the MAX_SPEED
		double preyMaxSpeed = (Double)param.getValue("preyMaxSpeed");
		if (velocity.lengthSquared() > preyMaxSpeed * preyMaxSpeed){
			velocity.normalize();
			velocity.scale(preyMaxSpeed);
		}

		// Update the position of the boid
		velocity.scale(timeScale);
		space.moveByDisplacement(this, velocity.x, velocity.y, velocity.z);
	}
}