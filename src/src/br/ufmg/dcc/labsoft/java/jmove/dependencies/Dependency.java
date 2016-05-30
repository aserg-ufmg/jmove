package br.ufmg.dcc.labsoft.java.jmove.dependencies;

import java.io.Serializable;

import br.ufmg.dcc.labsoft.java.jmove.enums.DependencyType;


public abstract class Dependency implements Serializable {
	protected final String classNameA;
	protected final String classNameB;


	protected Dependency(String classNameA, String classNameB) {
		super();
		this.classNameA = classNameA;
		this.classNameB = classNameB;
	}

	public String getClassNameA() {
		return this.classNameA;
	}

	public String getClassNameB() {
		return this.classNameB;
	}

	public final boolean sameType(Dependency other) {
		return (this.getDependencyType().equals(other.getDependencyType()) && this.classNameB.equals(other.classNameB));
	}

	public abstract DependencyType getDependencyType();
	
	public abstract String toShortString();
}