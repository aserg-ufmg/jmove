package br.ufmg.dcc.labsoft.java.jmove.dependencies;



public final class DeclareFieldDependency extends DeclareDependency {
	private final String fieldNameA;
	
	public DeclareFieldDependency(String classNameA, String classNameB, String fieldNameA) {
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
				"' whose type is '" + this.classNameB + "'";
	}	
	
}