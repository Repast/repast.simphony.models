package flock;

import javax.vecmath.Vector3d;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.util.ContextUtils;
public class Predator extends Boid {
	
	public Prey target;         // The current target
	public Vector3d attackVector = new Vector3d(0,0,0);    // Vector to the prey

	public Predator(){
	}

	/// <summary>
	/// Update the boid
	/// </summary>
	/// <param name="rand"> Random number generator</param>
	/// <param name="boids"> List of boids in the flock</param>
	/// <param name="elapsedTime"> Elapsed time for this frame</param>
	/// <param name="flockCenter"> Center of the flock</param>
	/// <param name="flockVelocity"> Average velocity of the flock</param>
	@ScheduledMethod(start=1, interval=1)
	public void update(){
		double elapsedTime = 0.05; //RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		Vector3d velocityUpdate = new Vector3d();     // Vector which will modify the boids velocity vector

		// If we have no target or we killed this one, acquire a new one
		Context context = ContextUtils.getContext(this);
		ContinuousSpace space = (ContinuousSpace)context.getProjection("Space");
		if( target == null || attackVector.lengthSquared() < SwarmBuilder.KillRadius * SwarmBuilder.KillRadius ){
			target = (Prey)context.getRandomObjects(Prey.class, 1).iterator().next();
		}

		NdPoint p = space.getLocation(target);
		NdPoint q = space.getLocation(this);
		
		Vector3d targetPosition = new Vector3d(p.getX(), p.getY(), p.getZ());
		Vector3d position = new Vector3d(q.getX(), q.getY(), q.getZ());
		
		if(target != null){
			attackVector.sub(targetPosition, position);
			velocityUpdate.add(attackVector);
		}

		// Update the velocity of the boid
		// Modify our velocity update vector to take into account acceleration over time
		velocityUpdate.scale(SwarmBuilder.PredAcceleration * SwarmBuilder.PreyAcceleration * elapsedTime);

		// Apply the update to the velocity
		Velocity.add(velocityUpdate);

		// If our velocity vector exceeds the max speed, throttle it back to the MAX_SPEED
		if( Velocity.length() > SwarmBuilder.PredMaxSpeed ){
			Velocity.normalize();
			Velocity.scale(SwarmBuilder.PredMaxSpeed);
		}

		// Update the position of the boid
		Velocity.scale(elapsedTime);
		space.moveByDisplacement(this, Velocity.x, Velocity.y, Velocity.z);
	}
}