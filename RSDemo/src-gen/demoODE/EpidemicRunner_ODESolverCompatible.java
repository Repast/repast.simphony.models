package demoODE;

import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;

public class EpidemicRunner_ODESolverCompatible {

private double y[] = new double[2];
FirstOrderIntegrator integrator;
Epidemic_ODESolverCompatible ode;
double timeDelta;

public EpidemicRunner_ODESolverCompatible() {

	Parameters params = RunEnvironment.getInstance().getParameters();
	timeDelta = (Double) params.getValue("SAVEPER");
	integrator = new EulerIntegrator((Double) params.getValue("TIME_STEP"));
	ode = new Epidemic_ODESolverCompatible(
		(Double) params.getValue("rate_that_people_contact_other_people"),
		(Double) params.getValue("initial_susceptible"),
		(Double) params.getValue("initial_infected"),
		(Double) params.getValue("fraction_infected_from_contact")
	);
	y[0] = (Double) params.getValue("initial_susceptible");
	y[1] = (Double) params.getValue("initial_infected");
}

@ScheduledMethod(start = 1,interval = 1,shuffle = true)
public void step() {
	integrator.integrate(ode, 0.0, y, timeDelta, y);
}

public String getID() {
	return "EpidemicRunner_ODESolverCompatible";
}
public Epidemic_ODESolverCompatible getOde() {
	return ode;
}
public double getY0() {
	return y[0];
}

public double getY1() {
	return y[1];
}

}
