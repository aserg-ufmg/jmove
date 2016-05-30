package br.ufmg.dcc.labsoft.java.jmove.dependencies;

import br.ufmg.dcc.labsoft.java.jmove.enums.DependencyType;
import br.ufmg.dcc.labsoft.java.jmove.util.DCLUtil;

public class AccessDependency extends HandleDependency {

	public AccessDependency(String classNameA, String classNameB) {
		super(classNameA, classNameB);
	}
	
	@Override
	public DependencyType getDependencyType() {
		return DependencyType.ACCESS;
	}
	
	@Override
	public String toShortString() {
		return "The access to " + DCLUtil.getSimpleClassName(this.classNameB) + " is disallowed for this location w.r.t. the architecture";
	}
	
}