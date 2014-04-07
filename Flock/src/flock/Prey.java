package flock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.vecmath.Vector3d;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.util.ContextUtils;

public class Prey extends Boid{
	private ArrayList<Prey> neighbors;
	private ArrayList<Predator> predators;

	public Prey(){
		neighbors = new ArrayList<Prey>();
		predators = new ArrayList<Predator>();
	}

	/// <summary>
	/// Update the boid
	/// </summary>
	/// <param name="rand"> Random number generator</param>
	/// <param name="boids"> List of boids in the flock</param>
	/// <param name="elapsedTime"> Elapsed time for this frame</param>
	/// <param name="flockCenter"> Center of the flock</param>
	/// <param name="flockVelocity"> Average velocity of the flock</param>
	
	@ScheduledMethod(start=0)
	public void init(){
		Context context = ContextUtils.getContext(this);
		ContinuousSpace space = (ContinuousSpace)context.getProjection("Space");
		ContinuousWithin query = new ContinuousWithin(context, this, 100);
		
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
		
		int count = 0;
		for (Map.Entry<Double, Prey> e : foundNeigh.entrySet()){
			neighbors.add(e.getValue());
			
			count++;
			if (count > 6) break;
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
		double elapsedTime = 0.05;// RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		// Vector which will modify the boids velocity vector
		Vector3d velocityUpdate = new Vector3d(0,0,0);     

		// Calculate Attraction and Avoidance for Neighbors
		// Calculate the avoidance vector for each boid
		Context context = ContextUtils.getContext(this);
		ContinuousSpace space = (ContinuousSpace)context.getProjection("Space");
		
		NdPoint q = space.getLocation(this);
		Vector3d position = new Vector3d(q.getX(), q.getY(), q.getZ());
		
		for (Prey neigh : neighbors){	
			NdPoint p = space.getLocation(neigh);
			
			Vector3d neighPosition = new Vector3d(p.getX(), p.getY(), p.getZ());
			
			Vector3d distanceToNeighbor = new Vector3d(0,0,0);
			distanceToNeighbor.sub(neighPosition, position);
			
			// If it's too far away, go chase it
			if ( distanceToNeighbor.lengthSquared() >= SwarmBuilder.PreySpacing * SwarmBuilder.PreySpacing ){
				distanceToNeighbor.scale(SwarmBuilder.AttractForce);
				velocityUpdate.add(distanceToNeighbor);
			}
			// If it's too close, fly away from it
			else{
				distanceToNeighbor.scale(SwarmBuilder.RepelForce);
				velocityUpdate.add(distanceToNeighbor);
			}
		}

		// Avoid the predators
		// Loop through each predator and get the vector to it
		for (Predator pred : predators){
			// Get the vector from this boid to its neighbor
			Vector3d distanceToFalcon = new Vector3d(0,0,0);
			
			NdPoint p = space.getLocation(pred);
			Vector3d predPosition = new Vector3d(p.getX(), p.getY(), p.getZ());
			
			distanceToFalcon.sub(predPosition, position);

			// If it is within range, add the vector to the repulsion vector
			if ( distanceToFalcon.lengthSquared() < SwarmBuilder.FearRadius * SwarmBuilder.FearRadius ){
				// The closer the predator is, the more weight it will have
				Vector3d v = new Vector3d(0,0,0);
				v.normalize(distanceToFalcon);
				v.scale(SwarmBuilder.FearForce);
				velocityUpdate.add(v);
			}
		}

		// Update the velocity of the boid
		// Apply the update to the velocity
		velocityUpdate.scale(SwarmBuilder.PreyAcceleration * elapsedTime);
		
		Velocity.add(velocityUpdate); 

		// If our velocity vector exceeds the max speed, throttle it back to the MAX_SPEED
		if( Velocity.lengthSquared() > SwarmBuilder.PreyMaxSpeed * SwarmBuilder.PreyMaxSpeed ){
			Velocity.normalize();
			Velocity.scale(SwarmBuilder.PreyMaxSpeed);
		}

		// Update the position of the boid
		Velocity.scale(elapsedTime);
		space.moveByDisplacement(this, Velocity.x, Velocity.y, Velocity.z);
	}

	public List<Prey> getNeighbors(){
		return neighbors;
	}
}