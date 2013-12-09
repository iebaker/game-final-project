package game.flora;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Rule implements Serializable {

	private static final long serialVersionUID = 5421954006757107393L;
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