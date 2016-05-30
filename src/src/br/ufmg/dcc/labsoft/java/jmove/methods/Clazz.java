package br.ufmg.dcc.labsoft.java.jmove.methods;

import java.util.AbstractMap;
import java.util.HashMap;

import org.eclipse.jdt.core.ICompilationUnit;

import br.ufmg.dcc.labsoft.java.jmove.utils.ClazzUtil;

public class Clazz {

	private AbstractMap<String, ICompilationUnit> allclass = null;
	private static Clazz instance = null;

	private Clazz() {
		this.allclass = new HashMap<String, ICompilationUnit>();
		// TODO Auto-generated constructor stub
	}

	public static Clazz getInstance() {
		if (instance == null) {
			instance = new Clazz();
		}
		return instance;
	}

	public void insertMapping(ICompilationUnit unit) {
		if (!allclass.containsKey(ClazzUtil.getUnitClazzName(unit))) {
			allclass.put(ClazzUtil.getUnitClazzName(unit), unit);

		}
	}

	

	public ICompilationUnit getICompilation(String clazzName) {
		return allclass.get(clazzName);
	}
}
