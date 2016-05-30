package br.ufmg.dcc.labsoft.java.jmove.dependencies;

public final class ExtendIndirectDependency extends ExtendDependency {

	public ExtendIndirectDependency(String classNameA, String classNameB) {
		super(classNameA, classNameB);
	}
	
	@Override
	public String toString() {
		return "'" + 
				this.classNameA + "' indirectly extends '" + this.classNameB + "'";
	}
	
}
