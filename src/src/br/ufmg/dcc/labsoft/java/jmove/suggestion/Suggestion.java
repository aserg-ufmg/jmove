package br.ufmg.dcc.labsoft.java.jmove.suggestion;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Position;

import br.ufmg.dcc.labsoft.java.jmove.methods.CompilationUnitCacheJmove;
import br.ufmg.dcc.labsoft.java.jmove.methods.MethodObjects;
import br.ufmg.dcc.labsoft.java.jmove.utils.ClazzUtil;
import br.ufmg.dcc.labsoft.java.jmove.utils.JMoveSignatures;

public class Suggestion {

	private IMethod iMethod;
	private ICompilationUnit clazz;

	public Suggestion(IMethod iMethod, ICompilationUnit clazz) {
		// TODO Auto-generated constructor stub
		this.iMethod = iMethod;
		this.clazz = clazz;
	}

	public String getMethodSignature() {
		// TODO Auto-generated method stub
		try {
			IJavaElement parent = iMethod.getParent();

			if (parent instanceof ICompilationUnit) {
				System.out.println("Icomp yep");
			}
			ICompilationUnit unit = iMethod.getCompilationUnit();
			if (parent.getElementName().equals(
					unit.getElementName().substring(0,
							unit.getElementName().length() - 5))) {
				return JMoveSignatures.getMethodSignature(ClazzUtil
						.getUnitClazzName(iMethod.getCompilationUnit()),
						iMethod);
			} else

				return JMoveSignatures.getMethodSignature(ClazzUtil
						.getUnitClazzName(iMethod.getCompilationUnit(),
								parent.getElementName()), iMethod);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Error";
	}

	public String getClassName() {
		// TODO Auto-generated method stub
		return ClazzUtil.getUnitClazzName(clazz);
	}

	public IFile getSourceIFile() {
		// TODO Auto-generated method stub
		return (IFile) iMethod.getCompilationUnit().getResource();
	}

	public IFile getTargetIFile() {
		// TODO Auto-generated method stub
		return (IFile) clazz.getResource();
	}

	public List<Position> getPositions() {
		// TODO Auto-generated method stub

		MethodDeclaration md = getSourceMethodDeclaration();
		ArrayList<Position> positions = new ArrayList<Position>();
		Position position = new Position(md.getStartPosition(), md.getLength());
		positions.add(position);
		return positions;
	}

	public String getAnnotationText() {
		Map<String, ArrayList<String>> accessMap = new LinkedHashMap<String, ArrayList<String>>();

		// String classOrigin = clazz.getElementName();
		// String entityName = clazz.getElementName();
		// if(accessMap.containsKey(classOrigin)) {
		// ArrayList<String> list = accessMap.get(classOrigin);
		// list.add(entityName);
		// }
		// else {
		// ArrayList<String> list = new ArrayList<String>();
		// list.add(entityName);
		// accessMap.put(classOrigin, list);
		// }

		StringBuilder sb = new StringBuilder();
		Set<String> keySet = accessMap.keySet();
		int i = 0;
		for (String key : keySet) {
			ArrayList<String> entities = accessMap.get(key);
			sb.append(key + ": " + entities.size());
			if (i < keySet.size() - 1)
				sb.append(" | ");
			i++;
		}
		return sb.toString();
	}

	public CompilationUnit getSourceCompilationUnit() {
		// TODO Auto-generated method stub
		return CompilationUnitCacheJmove.getInstance().getCompilation(
				iMethod.getCompilationUnit());
	}

	public CompilationUnit getTargetClassCompilationUnit() {
		// TODO Auto-generated method stub
		return CompilationUnitCacheJmove.getInstance().getCompilation(clazz);
	}

	public MethodDeclaration getSourceMethodDeclaration() {
		// TODO Auto-generated method stub
		return MethodObjects.getInstance().getMethodDeclaration(iMethod);
	}

	public TypeDeclaration recoverSourceClassTypeDeclaration() {
		// TODO Auto-generated method stub
		return CompilationUnitCacheJmove.getInstance().getTypeDeclaration(
				iMethod.getCompilationUnit());
	}

	public TypeDeclaration recovergetTargetClassTypeDeclaration() {
		// TODO Auto-generated method stub
		return CompilationUnitCacheJmove.getInstance()
				.getTypeDeclaration(clazz);
	}

	public String getMethodName() {
		// TODO Auto-generated method stub
		return iMethod.getElementName();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Move "+ this.getMethodSignature() +" to "+ ClazzUtil.getUnitClazzName(clazz);
	}
}
