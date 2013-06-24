package demoODE;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;

public class ContextBuilderEpidemic_ODESolverCompatible implements ContextBuilder<Object> {
	@Override
	public Context<Object> build(Context<Object> context) {
		EpidemicRunner_ODESolverCompatible ode = new EpidemicRunner_ODESolverCompatible();
		context.setId("Epidemic_ODESolverCompatible");
		context.add(ode);
		context.add(ode.getOde());

		Parameters params = RunEnvironment.getInstance().getParameters();
		RunEnvironment.getInstance().endAt((Double) params.getValue("FINAL_TIME"));
		return context;
	}
}
