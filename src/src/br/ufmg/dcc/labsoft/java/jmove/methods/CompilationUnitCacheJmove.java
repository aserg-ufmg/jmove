package br.ufmg.dcc.labsoft.java.jmove.methods;

import java.util.AbstractMap;
import java.util.HashMap;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class CompilationUnitCacheJmove {

	private AbstractMap<ICompilationUnit, CompilationUnit> allcompilation = null;
	private AbstractMap<ICompilationUnit, TypeDeclaration> allTypeDeclaration = null;
	private static CompilationUnitCacheJmove instance = null;

	private CompilationUnitCacheJmove() {
		this.allcompilation = new HashMap<ICompilationUnit, CompilationUnit>();
		this.allTypeDeclaration = new HashMap<ICompilationUnit, TypeDeclaration>();
		// TODO Auto-generated constructor stub
	}

	public static CompilationUnitCacheJmove getInstance() {
		if (instance == null) {
			instance = new CompilationUnitCacheJmove();
		}
		return instance;
	}

	public void insertMapping(ICompilationUnit iUnit, CompilationUnit cUnit) {
		if (!allcompilation.containsKey(iUnit)) {
			allcompilation.put(iUnit, cUnit);

		}
	}

	public void insertMapping(ICompilationUnit iUnit, TypeDeclaration tdec) {
		if (!allTypeDeclaration.containsKey(iUnit)) {
			allTypeDeclaration.put(iUnit, tdec);

		}
	}

	public CompilationUnit getCompilation(ICompilationUnit iUnit) {
		return allcompilation.get(iUnit);
	}

	public TypeDeclaration getTypeDeclaration(ICompilationUnit iUnit) {
		return allTypeDeclaration.get(iUnit);
	}

}
