package br.ufmg.dcc.labsoft.java.jmove.dependencies;

import br.ufmg.dcc.labsoft.java.jmove.enums.DependencyType;
import br.ufmg.dcc.labsoft.java.jmove.util.DCLUtil;

public class AnnotateDependency extends Dependency {
	
	public AnnotateDependency(String classNameA, String classNameB) {
		super(classNameA,classNameB);
	}
	
	@Override
	public DependencyType getDependencyType() {
		return DependencyType.USEANNOTATION;
	}
	
	@Override
	public String toShortString() {
		return "The annotation @" + DCLUtil.getSimpleClassName(this.classNameB) + " is disallowed for this location w.r.t. the architecture";
	}

}