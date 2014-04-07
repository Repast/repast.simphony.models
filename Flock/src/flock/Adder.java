package flock;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousAdder;
import repast.simphony.space.continuous.ContinuousSpace;

public class Adder <T> implements ContinuousAdder<T> {
	/**
	 * Adds the specified object to the space at a random location.
	 * 
	 * @param space
	 *            the space to add the object to.
	 * @param obj
	 *            the object to add.
	 */
	public void add(ContinuousSpace<T> space, T obj) {
		Dimensions dims = space.getDimensions();
		double[] location = new double[dims.size()];
		findLocation(location, dims);
		while (!space.moveTo(obj, location)) {
			findLocation(location, dims);
		}
	}

	private void findLocation(double[] location, Dimensions dims) {
		double[] origin = dims.originToDoubleArray(null);
		for (int i = 0; i < location.length; i++) {
			try{
				location[i] = RandomHelper.getUniform().nextDoubleFromTo(0, dims.getDimension(i)/4) - origin[i];
			}
			catch(Exception e){
				
			}
		}
	}
}
