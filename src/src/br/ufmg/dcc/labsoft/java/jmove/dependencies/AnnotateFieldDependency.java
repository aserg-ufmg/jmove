package br.ufmg.dcc.labsoft.java.jmove.dependencies;



public final class AnnotateFieldDependency extends AnnotateDependency {
	private final String fieldNameA;
	
	public AnnotateFieldDependency(String classNameA, String classNameB, String fieldNameA) {
		super(classNameA,classNameB);
		this.fieldNameA = fieldNameA;
	}

	public String getFieldName() {
		return this.fieldNameA;
	}
	
	@Override
	public String toString() {
		return "'" + 
				this.classNameA + "' contains the field '" + this.fieldNameA + 
				"' which is annotated by '" + this.classNameB + "'";
	}
	
}