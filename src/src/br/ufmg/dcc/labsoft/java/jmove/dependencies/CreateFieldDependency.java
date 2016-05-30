package br.ufmg.dcc.labsoft.java.jmove.dependencies;



public class CreateFieldDependency extends CreateDependency {
	private String fieldNameA;
	
	public CreateFieldDependency(String classNameA, String classNameB, String fieldNameA) {
		super(classNameA,classNameB);
		this.fieldNameA = fieldNameA;
	}
	
	public String toString() {
		return "'" + 
				this.classNameA + "' contains the field '" + this.fieldNameA + 
				"' that receives an instantiation of an object of '" + this.classNameB + "'";
	}
	
}