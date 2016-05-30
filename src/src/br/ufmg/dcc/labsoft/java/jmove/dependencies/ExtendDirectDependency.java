package br.ufmg.dcc.labsoft.java.jmove.dependencies;

public final class ExtendDirectDependency extends ExtendDependency {

	public ExtendDirectDependency(String classNameA, String classNameB) {
		super(classNameA, classNameB);
	}
	
	@Override
	public String toString() {
		return "'" + 
				this.classNameA + "' directly extends '" + this.classNameB + "'";
	}

	
}
