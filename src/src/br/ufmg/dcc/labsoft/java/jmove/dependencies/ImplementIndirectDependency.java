package br.ufmg.dcc.labsoft.java.jmove.dependencies;

public final class ImplementIndirectDependency extends ImplementDependency {

	public ImplementIndirectDependency(String classNameA, String classNameB) {
		super(classNameA, classNameB);
	}
	
	
	@Override
	public String toString() {
		return "'" + 
				this.classNameA + "' indirectly implements '" + this.classNameB + "'";
	}

	
}
