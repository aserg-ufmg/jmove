package br.ufmg.dcc.labsoft.java.jmove.methods;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufmg.dcc.labsoft.java.jmove.basic.AllEntitiesMapping;
import br.ufmg.dcc.labsoft.java.jmove.utils.JavaTypes;

public class MethodJMove {

	private int sourceClassID;
	private int NameID;
	private Set<Integer> methodsDependenciesID;

	// Atributo para feature envy
	// private List<Integer> methodsAcessDependenciesID;

	public MethodJMove(int methodId, int sourceClassID) {
		this.NameID = methodId;
		this.sourceClassID = sourceClassID;
		this.methodsDependenciesID = new HashSet<Integer>();
		// this.methodsAcessDependenciesID = new ArrayList<Integer>();
	}

	public int getSourceClassID() {
		return sourceClassID;
	}

	public void setSourceClassID(int classeOrigemID) {
		sourceClassID = classeOrigemID;
	}

	public int getNameID() {
		return NameID;
	}

	public void setNameID(int nameID) {
		NameID = nameID;
	}

	public Set<Integer> getMethodsDependencies() {
		return methodsDependenciesID;
	}

	// public List<Integer> getMethodsAcessDependenciesID() {
	// return methodsAcessDependenciesID;
	// }

	public void setNewMethodsDependencies(List<String> dependeciesList) {

		for (String dependecy : dependeciesList) {
			if (JavaTypes.mustRemoveTypes(dependecy))
				return;
		}

		int depedencyID;

		for (String name : dependeciesList) {
			if (!JavaTypes.ismethodOrAtribute(name)) {
//				System.out.println("aki " + name);
				depedencyID = AllEntitiesMapping.getInstance().createEntityID(name);
				methodsDependenciesID.add(depedencyID);
				AllDependenciesMethods.getInstance()
						.getAllDependenciesMethodID().add(depedencyID);
			}

		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 17;
		result = prime * result + NameID * sourceClassID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MethodJMove) {
			MethodJMove other = (MethodJMove) obj;

			if (this.NameID != other.NameID
					|| this.sourceClassID != other.sourceClassID) {
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return AllEntitiesMapping.getInstance().getByID(NameID);
	}

}
