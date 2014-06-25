package demo;

public class MemoryGetter1_Epidemic extends MemoryGetterEpidemic{
public MemoryGetter1_Epidemic(MemoryEpidemic memory) {
super(memory);

}

public String getID() {
return "Epidemic";
}
public double get_Contacts_between_infected_and_unaffected() {
return memory.Contacts_between_infected_and_unaffected;
}
public double get_FINAL_TIME() {
return memory.FINAL_TIME;
}
public double get_Fraction_of_population_infected() {
return memory.Fraction_of_population_infected;
}
public double get_Healthy() {
return memory.Healthy;
}
public double get_INITIAL_TIME() {
return memory.INITIAL_TIME;
}
public double get_Infected() {
return memory.Infected;
}
public double get_NAREPLACEMENT() {
return memory.NAREPLACEMENT;
}
public double get_SAVEPER() {
return memory.SAVEPER;
}
public double get_TIME_STEP() {
return memory.TIME_STEP;
}
public double get_Time() {
return memory.Time;
}
public double get_fraction_infected_from_contact() {
return memory.fraction_infected_from_contact;
}
public double get_getting_sick() {
return memory.getting_sick;
}
public double get_initial_infected() {
return memory.initial_infected;
}
public double get_initial_susceptible() {
return memory.initial_susceptible;
}
public double get_rate_of_potential_infectious_contacts() {
return memory.rate_of_potential_infectious_contacts;
}
public double get_rate_that_people_contact_other_people() {
return memory.rate_that_people_contact_other_people;
}
public double get_total_population() {
return memory.total_population;
}
}
