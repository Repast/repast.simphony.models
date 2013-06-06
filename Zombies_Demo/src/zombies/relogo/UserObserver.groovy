package zombies.relogo

import static repast.simphony.relogo.Utility.*;
import static repast.simphony.relogo.UtilityG.*;
import repast.simphony.relogo.BaseObserver;
import repast.simphony.relogo.Stop;
import repast.simphony.relogo.Utility;
import repast.simphony.relogo.UtilityG;
import repast.simphony.relogo.schedule.Go;
import repast.simphony.relogo.schedule.Setup;
import zombies.ReLogoObserver;

class UserObserver extends ReLogoObserver {

	/**
	 * Add observer methods here. For example:

		def setup(){
			clearAll()
			createTurtles(10){
				forward(random(10))
			}
		}
		
	 *
	 * or
	 * 	
	
		def go(){
			ask(turtles()){
				left(random(90))
				right(random(90))
				forward(random(10))
			}
		}

	 */
	
	@Setup
	def setup(){
		clearAll()
		setDefaultShape(Human,"person")
		createHumans(numHumans){
			setxy(randomXcor(),randomYcor())
		}
		setDefaultShape(Zombie,"zombie")
		createZombies(numZombies){
			setxy(randomXcor(),randomYcor())
			size = 2
		}
		
	}
	
	@Go
	def go(){
		
		if (emptyQ(humans())){
			pause()
		}
		
		ask (zombies()){
			step()
			zombieSignal++
		}
		diffuseAndEvaporate()
		ask (patches()){
			recolorPatch()
		}
		
		ask (humans()){
			step()
		}
		
	}
	
	def remainingHumans(){
		count(humans())
	}
	
	def remainingZombies(){
		count(zombies())
	}
	
	def timestamp(){
		ticks()
	}
	
	def diffuseAndEvaporate(){
		diffuse("zombieSignal",zombieDiffusionRate)
		diffusibleMultiply("zombieSignal", 1 - zombieEvaporationRate)
	}

}