package game.flora;

import java.util.Set;
import java.util.HashSet;

import java.util.Arrays;

public class Rule {

	private Set<Transformation> transformations;

	public Rule() {
		this.transformations = new HashSet<Transformation>();
	}

	public Rule(Set<Transformation> t) {
		this.transformations = t;
	}

	public Rule(Transformation... ts) {
		this.transformations = new HashSet<Transformation>();
		this.transformations.addAll(Arrays.asList(ts));
	}

	public void addTransformation(Transformation t) {
		transformations.add(t);
	}

	private Set<Branch> applyToHelper(Branch b) {
		Set<Branch> return_value = new HashSet<Branch>();
		for(Transformation t : this.transformations) {
			return_value.add(b.transform(t));
		}
		return return_value;
	}

	public Set<Branch> applyTo(Set<Branch> bs) {
		Set<Branch> return_value = new HashSet<Branch>();
		for(Branch b : bs) {
			return_value.addAll(this.applyToHelper(b));
		}
		return return_value;
	}
}