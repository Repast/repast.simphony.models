/**
 * 
 */
package geozombies;

import com.vividsolutions.jts.geom.Coordinate;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

/**
 * @author nick
 *
 */
public class Human {
	
	private int energy, startingEnergy;

	public Human(int energy) {
		this.energy = startingEnergy = energy;
	}
	
	@ScheduledMethod(start=1, interval=1)
	public void step(){
		randomWalk();
	}
	
//	@Watch(watcheeClassName = "jzombies.Zombie", watcheeFieldNames = "moved", 
//			query = "within_vn 1", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
//	public void run() {
//		// get the grid location of this Human
//		GridPoint pt = grid.getLocation(this);
//
//		// use the GridCellNgh class to create GridCells for
//		// the surrounding neighborhood.
//		GridCellNgh<Zombie> nghCreator = new GridCellNgh<Zombie>(grid, pt,
//				Zombie.class, 1, 1);
//		List<GridCell<Zombie>> gridCells = nghCreator.getNeighborhood(true);
//		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
//
//		GridPoint pointWithLeastZombies = null;
//		int minCount = Integer.MAX_VALUE;
//		for (GridCell<Zombie> cell : gridCells) {
//			if (cell.size() < minCount) {
//				pointWithLeastZombies = cell.getPoint();
//				minCount = cell.size();
//			}
//		}
//		
//		if (energy > 0) {
//			moveTowards(pointWithLeastZombies);
//		} else {
//			energy = startingEnergy;
//		}
//	}
	
	public void moveTowards(GridPoint pt) {
//		// only move if we are not already in this grid location
//		if (!pt.equals(grid.getLocation(this))) {
//			NdPoint myPoint = space.getLocation(this);
//			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
//			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
//			space.moveByVector(this, 2, angle, 0);
//			myPoint = space.getLocation(this);
//			grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
//			//energy--;
//		}
	}

	protected void randomWalk(){
		Context context = ContextUtils.getContext(this);
		Geography geography = (Geography)context.getProjection("Geography");

		Coordinate currentPosisition = geography.getGeometry(this).getCoordinate();
		
		geography.moveByDisplacement(this, RandomHelper.nextDoubleFromTo(-0.0001, 0.0001), 
				RandomHelper.nextDoubleFromTo(-0.0001, 0.0001));
		
	}
}
