package demoODE;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

import repast.simphony.systemdynamics.ode.ODEFunctionSupport;

public class Epidemic_ODESolverCompatible implements FirstOrderDifferentialEquations {

	private double rate_of_potential_infectious_contacts;
	private double Fraction_of_population_infected;
	private double total_population;
	private double Contacts_between_infected_and_unaffected;
	private double rate_that_people_contact_other_people = 5;
	private double initial_susceptible = 1000000.0;
	private double getting_sick;
	private double initial_infected = 10;
	private double fraction_infected_from_contact = 0.1;
	private ODEFunctionSupport sdFunctions = new ODEFunctionSupport();


	public Epidemic_ODESolverCompatible() {
	}
	public Epidemic_ODESolverCompatible(double rate_that_people_contact_other_people, double initial_susceptible, double initial_infected, double fraction_infected_from_contact) {

	this.rate_that_people_contact_other_people = rate_that_people_contact_other_people;
	this.initial_susceptible = initial_susceptible;
	this.initial_infected = initial_infected;
	this.fraction_infected_from_contact = fraction_infected_from_contact;
	}

	 public int getDimension() {
		return 2;
	}

	public void computeDerivatives(double t, double[] y, double[] yDot) {

	// auxiliary assignments

/*
	rate of potential infectious contacts=Healthy  * rate that people contact other people
*/
	rate_of_potential_infectious_contacts=(y[0]*rate_that_people_contact_other_people);

/*
	total population=Healthy + Infected
*/
	total_population=(y[0]+y[1]);

/*
	Fraction of population infected=Infected / total population
*/
	Fraction_of_population_infected=(y[1]/total_population);

/*
	Contacts between infected and unaffected=rate of potential infectious contacts  * Fraction of population infected
*/
	Contacts_between_infected_and_unaffected=(rate_of_potential_infectious_contacts*Fraction_of_population_infected);

/*
	getting sick=Contacts between infected and unaffected  * fraction infected from contact
*/
	getting_sick=(Contacts_between_infected_and_unaffected*fraction_infected_from_contact);


	// "stock" delta assignments

/*
	Healthy= INTEG(-getting sick,initial susceptible)
*/
	yDot[0]=(-getting_sick);

/*
	Infected= INTEG(getting sick,initial infected)
*/
	yDot[1]=getting_sick;

	}

	public double getRate_that_people_contact_other_people() {
		return rate_that_people_contact_other_people;
	}

	public void setRate_that_people_contact_other_people(double value) {
		rate_that_people_contact_other_people = value;
	}

	public double getInitial_susceptible() {
		return initial_susceptible;
	}

	public void setInitial_susceptible(double value) {
		initial_susceptible = value;
	}

	public double getInitial_infected() {
		return initial_infected;
	}

	public void setInitial_infected(double value) {
		initial_infected = value;
	}

	public double getFraction_infected_from_contact() {
		return fraction_infected_from_contact;
	}

	public void setFraction_infected_from_contact(double value) {
		fraction_infected_from_contact = value;
	}

}
