package br.ufmg.dcc.labsoft.java.jmove.utils;

import org.eclipse.jdt.core.ICompilationUnit;

public class ClazzUtil {

	public static String getUnitClazzName(ICompilationUnit unit) {
		// TODO Auto-generated method stub
		if (unit.getParent().getElementName().equals("")) {
			return unit.getElementName().substring(0,
					unit.getElementName().length() - 5);
		} else {
			return unit.getParent().getElementName()
					+ "."
					+ unit.getElementName().substring(0,
							unit.getElementName().length() - 5);
		}
	}

	public static String getUnitClazzName(ICompilationUnit unit,
			String elementName) {
		// TODO Auto-generated method stub
		if (unit.getParent().getElementName().equals("")) {
			return unit.getElementName().substring(0,
					unit.getElementName().length() - 5)+"."+elementName;
		} else {
			return unit.getParent().getElementName()
					+ "."
					+ unit.getElementName().substring(0,
							unit.getElementName().length() - 5)+"."+elementName;
		}
	}
}
