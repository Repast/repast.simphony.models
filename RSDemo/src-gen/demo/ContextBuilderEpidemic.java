package demo;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
public class ContextBuilderEpidemic implements ContextBuilder<Object> {
@Override
public Context<Object> build(Context<Object> context) {
Epidemic on = new Epidemic("Epidemic");
on.oneTime();
context.setId("Epidemic");
context.add(on);
context.add(on.getMemory());
context.add(new MemoryGetter1_Epidemic(on.getMemory()));
RunEnvironment.getInstance().endAt(on.getMemory().getFINALTIME()- on.getMemory().getINITIALTIME());
return context;
}
}
