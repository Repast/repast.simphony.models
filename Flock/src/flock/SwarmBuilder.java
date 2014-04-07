package flock;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;

/**
 * Adopted from the C# Swarm model by Daniel Greenheck: 
 *   http://greenhecktech.com/2014/04/03/throwback-thursday-swarm-intelligence/
 * 
 * @author Eric Tatara
 *
 */
public class SwarmBuilder implements ContextBuilder<Boid> {
	public static int preyCount = 3000;    // Initial number of prey rows  
	public static int predCount = 2;    // Initial number of predators

	// Parameters
	public static float PreySpacing = 8;           // Smaller cloth give a more organized, cloth like look
	public static float PreyAcceleration = 0.75f;  // Starling acceleration
	public static float PreyMaxSpeed = 70;         // Max speed of the starling
	public static float PredAcceleration = 3;
	public static float PredMaxSpeed = 300;
	public static float AttractForce = 160;         // Attraction force for prey
	public static float RepelForce = -180;          // Higher repulsion force gives more randomness
	public static float FearForce = -500000;       // Repulsion force for predator
	public static float FearRadius = 80;           // The distance a starling can see a falcon
	public static float KillRadius = 10;           // How close the predator has to be to the prey to kill it

	@Override
	public Context<Boid> build(Context<Boid> context) {
		
		ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
				.createContinuousSpace("Space", context, 
						new Adder<Boid>(),
						new repast.simphony.space.continuous.WrapAroundBorders(), 
						500, 500, 500);
		
		// Add the prey
		for(int i=0; i<preyCount; i++){
			context.add(new Prey());
		}

		// Add the predators
		for(int i=0; i<predCount; i++){            	
			context.add(new Predator());
		}

		return context;
	}
}