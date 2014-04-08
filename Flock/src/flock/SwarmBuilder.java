package flock;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.AbstractPointTranslator;
import repast.simphony.space.continuous.StickyBorders;
import repast.simphony.space.continuous.WrapAroundBorders;
import repast.simphony.space.continuous.InfiniteBorders;

/**
 * Flocking model that simulates a large flock of smaller prey birds
 *   that attempt to avoid larger predator birds.
 * 
 * Adopted from the C# Swarm model by Daniel Greenheck: 
 *   http://greenhecktech.com/2014/04/03/throwback-thursday-swarm-intelligence/
 * 
 * @author Eric Tatara
 *
 */
public class SwarmBuilder implements ContextBuilder<Boid> {
	
	@Override
	public Context<Boid> build(Context<Boid> context) {
		Parameters param = RunEnvironment.getInstance().getParameters();
		
		int initialNumPrey = (Integer)param.getValue("initialNumPrey");
		int initialNumPred = (Integer)param.getValue("initialNumPred");
		
		String borderType = param.getValueAsString("borderType");
		AbstractPointTranslator border = null;
		
		if ("Wrap_Around".equals(borderType))
			border = new WrapAroundBorders();
		else if ("Bouncy".equals(borderType))
			border = new repast.simphony.space.continuous.BouncyBorders();
		else if ("Infinite".equals(borderType))
			border = new InfiniteBorders<>();
		else if ("Sticky".equals(borderType))
			border = new StickyBorders();  
			
		ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null)
				.createContinuousSpace("Space", context, 
						new Adder<Boid>(4),
						border, 
						500, 500, 500);
		
		// Add the prey
		for(int i=0; i<initialNumPrey; i++){
			context.add(new Prey());
		}

		// Add the predators
		for(int i=0; i<initialNumPred; i++){            	
			context.add(new Predator());
		}

		return context;
	}
}