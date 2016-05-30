package br.ufmg.dcc.labsoft.java.jmove.methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;

import br.ufmg.dcc.labsoft.java.jmove.ast.DeepDependencyVisitor;
import br.ufmg.dcc.labsoft.java.jmove.basic.AllEntitiesMapping;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.AccessFieldDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.AccessMethodDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.AnnotateMethodDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.CreateMethodDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.DeclareLocalVariableDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.DeclareParameterDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.DeclareReturnDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.Dependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.SimpleNameDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.ThrowDependency;
import br.ufmg.dcc.labsoft.java.jmove.util.DCLUtil;
import br.ufmg.dcc.labsoft.java.jmove.utils.ClazzUtil;
import br.ufmg.dcc.labsoft.java.jmove.utils.MoveMethod;
import br.ufmg.dcc.labsoft.java.jmove.utils.PrintOutput;
import br.ufmg.dcc.labsoft.java.jmove.utils.JMoveSignatures;

public class AllMethods {

	private List<MethodJMove> allMethodsList;
	private int numberOfExcluded;
	private Map<Integer, IMethod> iMethodMapping;
	private Set<Integer> moveIspossible;
	private IProgressMonitor monitor;

	public AllMethods(List<DeepDependencyVisitor> allDeepDependency,
			IProgressMonitor monitor) throws JavaModelException {
		// TODO Auto-generated method stub

		this.numberOfExcluded = 0;
		this.allMethodsList = new ArrayList<MethodJMove>();
		this.iMethodMapping = new HashMap<Integer, IMethod>();
		this.moveIspossible = new HashSet<Integer>();
		this.monitor = monitor;

		createMethodsDependeciesList(allDeepDependency);

	}

	private void createMethodsDependeciesList(
			List<DeepDependencyVisitor> allDeepDependency)
			throws JavaModelException {
		float i = 0;

		monitor.beginTask(
				"Creating Dependencies for selected Java Project (2/4)",
				(allDeepDependency.size()));

		for (DeepDependencyVisitor deep : allDeepDependency) {
			System.out.printf("Criando metodo  %2.2" + "f %c \n",
					(100) * (++i / allDeepDependency.size()), '%');
			// for (IMethod m : deep.getUnit().findPrimaryType().getMethods()) {
			// System.out.println("element " + m.getElementName() + " "
			// + m.getDeclaringType().getFullyQualifiedName());
			// }
			// System.out.println();

			monitor.worked(1);
			for (Dependency dep : deep.getDependencies()) {

				if (dep instanceof AccessMethodDependency) {

					AccessMethodDependency acDependency = (AccessMethodDependency) dep;

					// String method = dep.getClassNameA() + "."
					// + ((AccessMethodDependency) dep).getMethodNameA()
					// + "()";
					String sourceClass = dep.getClassNameA();
					String dependecyClass = dep.getClassNameB();

					String methodA = JMoveSignatures.getMethodSignature(
							dep.getClassNameA(), acDependency.getImethodA());
					String methodB = JMoveSignatures.getMethodSignature(
							dep.getClassNameB(), acDependency.getImethodB());

					// String methodB = dep.getClassNameB() + "."
					// + ((AccessMethodDependency) dep).getMethodNameB()
					// + "()";

					List<String> dependeciesList = new ArrayList<String>();

					if (!recursive(sourceClass, methodA, dependecyClass,
							methodB)) {
						dependeciesList.add(dependecyClass);
						dependeciesList.add(methodB);
					}
					// System.out.println(sourceClass + " " + methodA
					// + " classe que depednde e metodo " + dependecyClass
					// + " " + methodB);

					putIMethodInMapping(methodA, acDependency.getImethodA());
					putIMethodInMapping(methodB, acDependency.getImethodB());

					createMethod(methodA, sourceClass, dependeciesList,
							acDependency.getImethodA());

				} else

				if (dep instanceof AccessFieldDependency) {

					AccessFieldDependency acFieldDependency = (AccessFieldDependency) dep;

					String methodA = JMoveSignatures.getMethodSignature(
							dep.getClassNameA(),
							acFieldDependency.getImethodA());

					String field = JMoveSignatures.getFieldSignature(
							dep.getClassNameB(),
							acFieldDependency.getiVariableBinding());

					// String method = dep.getClassNameA() + "."
					// + ((AccessFieldDependency) dep).getMethodName()
					// + "()";

					String sourceClass = dep.getClassNameA();
					String dependecyClass = dep.getClassNameB();
					// String field = dep.getClassNameB() + "."
					// + ((AccessFieldDependency) dep).getFieldName();

					List<String> dependeciesList = new ArrayList<String>();
					dependeciesList.add(dependecyClass);
					dependeciesList.add(field);

					putIMethodInMapping(methodA,
							acFieldDependency.getImethodA());

					createMethod(methodA, sourceClass, dependeciesList,
							acFieldDependency.getImethodA());

				} else

				if (dep instanceof SimpleNameDependency) {

					SimpleNameDependency snDependency = (SimpleNameDependency) dep;

					// String method = dep.getClassNameA() + "."
					// + ((SimpleNameDependency) dep).getMethodName()
					// + "()";

					String sourceClass = dep.getClassNameA();
					String dependecyClass = dep.getClassNameB();

					// String field = dep.getClassNameB() + "."
					// + ((SimpleNameDependency) dep).getVariableName();

					String methodA = JMoveSignatures.getMethodSignature(
							dep.getClassNameA(), snDependency.getImethodA());

					String field = JMoveSignatures.getFieldSignature(
							dep.getClassNameB(),
							snDependency.getiVariableBinding());

					List<String> dependeciesList = new ArrayList<String>();
					dependeciesList.add(dependecyClass);
					dependeciesList.add(field);

					putIMethodInMapping(methodA, snDependency.getImethodA());

					createMethod(methodA, sourceClass, dependeciesList,
							snDependency.getImethodA());

				} else

				if (dep instanceof AnnotateMethodDependency) {

					AnnotateMethodDependency anDependency = (AnnotateMethodDependency) dep;

					String sourceClass = dep.getClassNameA();
					String dependecyClass = dep.getClassNameB();
					// String method = dep.getClassNameA() + "."
					// + ((AnnotateMethodDependency) dep).getMethodName()
					// + "()";

					String methodA = JMoveSignatures.getMethodSignature(
							dep.getClassNameA(), anDependency.getImethodA());

					List<String> dependeciesList = new ArrayList<String>();
					dependeciesList.add(dependecyClass);

					putIMethodInMapping(methodA, anDependency.getImethodA());

					createMethod(methodA, sourceClass, dependeciesList,
							anDependency.getImethodA());

				} else

				if (dep instanceof CreateMethodDependency) {

					CreateMethodDependency cmDependency = (CreateMethodDependency) dep;

					String sourceClass = dep.getClassNameA();
					String dependecyClass = dep.getClassNameB();

					String methodA = JMoveSignatures.getMethodSignature(
							dep.getClassNameA(), cmDependency.getImethodA());

					// String method = dep.getClassNameA() + "."
					// + ((CreateMethodDependency) dep).getMethodNameA()
					// + "()";

					List<String> dependeciesList = new ArrayList<String>();
					dependeciesList.add(dependecyClass);

					putIMethodInMapping(methodA, cmDependency.getImethodA());

					createMethod(methodA, sourceClass, dependeciesList,
							cmDependency.getImethodA());

				} else

				if (dep instanceof DeclareParameterDependency) {

					DeclareParameterDependency dpDependency = (DeclareParameterDependency) dep;

					// String method = dep.getClassNameA()
					// + "."
					// + ((DeclareParameterDependency) dep)
					// .getMethodName() + "()";

					String methodA = JMoveSignatures.getMethodSignature(
							dep.getClassNameA(), dpDependency.getImethodA());

					String sourceClass = dep.getClassNameA();
					String dependecyClass = dep.getClassNameB();
					// String field = dep.getClassNameB() + "."
					// + ((DeclareParameterDependency) dep).getFieldName();

					List<String> dependeciesList = new ArrayList<String>();
					dependeciesList.add(dependecyClass);
					// dependeciesList.add(field);

					putIMethodInMapping(methodA, dpDependency.getImethodA());

					createMethod(methodA, sourceClass, dependeciesList,
							dpDependency.getImethodA());

				} else

				if (dep instanceof DeclareReturnDependency) {

					DeclareReturnDependency drDependency = (DeclareReturnDependency) dep;

					String methodA = JMoveSignatures.getMethodSignature(
							dep.getClassNameA(), drDependency.getImethodA());

					String sourceClass = dep.getClassNameA();
					String dependecyClass = dep.getClassNameB();
					// String method = dep.getClassNameA() + "."
					// + ((DeclareReturnDependency) dep).getMethodName()
					// + "()";

					List<String> dependeciesList = new ArrayList<String>();
					dependeciesList.add(dependecyClass);

					putIMethodInMapping(methodA, drDependency.getImethodA());

					createMethod(methodA, sourceClass, dependeciesList,
							drDependency.getImethodA());

				} else

				if (dep instanceof DeclareLocalVariableDependency) {

					DeclareLocalVariableDependency dlvDependency = (DeclareLocalVariableDependency) dep;

					String methodA = JMoveSignatures.getMethodSignature(
							dep.getClassNameA(), dlvDependency.getImethodA());

					// String method = dep.getClassNameA()
					// + "."
					// + ((DeclareLocalVariableDependency) dep)
					// .getMethodName() + "()";
					String sourceClass = dep.getClassNameA();
					String dependecyClass = dep.getClassNameB();
					// String field = dep.getClassNameB()
					// + "."
					// + ((DeclareLocalVariableDependency) dep)
					// .getFieldName();

					List<String> dependeciesList = new ArrayList<String>();
					dependeciesList.add(dependecyClass);

					// System.out.println("DeclareLocalVariableDependency");
					// System.out.println(dependecyClass);
					// System.out.println(method);
					// System.out.println(field);
					// dependeciesList.add(field);

					putIMethodInMapping(methodA, dlvDependency.getImethodA());

					createMethod(methodA, sourceClass, dependeciesList,
							dlvDependency.getImethodA());

				} else

				if (dep instanceof ThrowDependency) {

					ThrowDependency throwDependency = (ThrowDependency) dep;

					String methodA = JMoveSignatures.getMethodSignature(
							dep.getClassNameA(), throwDependency.getImethodA());

					String sourceClass = dep.getClassNameA();
					String dependecyClass = dep.getClassNameB();
					// String method = dep.getClassNameA() + "."
					// + ((ThrowDependency) dep).getMethodName() + "()";

					List<String> dependeciesList = new ArrayList<String>();
					dependeciesList.add(dependecyClass);

					putIMethodInMapping(methodA, throwDependency.getImethodA());

					createMethod(methodA, sourceClass, dependeciesList,
							throwDependency.getImethodA());

				}

			}

			if (monitor != null && monitor.isCanceled()) {
				if (monitor != null)
					monitor.done();
				throw new OperationCanceledException();
			}
		}

		// // #............ Imprime dependencias

		Iterator<MethodJMove> it = allMethodsList.iterator();
		while (it.hasNext()) {
			MethodJMove m = it.next();

			PrintOutput.write(
					AllEntitiesMapping.getInstance().getByID(m.getNameID())
							+ "\n", "conjuntos");

			for (Integer chaves : m.getMethodsDependencies()) {
				PrintOutput
						.write(AllEntitiesMapping.getInstance().getByID(chaves)
								+ "\n", "conjuntos");
			}

			PrintOutput.write("\n \n", "conjuntos");
		}
		// #............

	}

	private boolean recursive(String sourceClass, String methodA,
			String dependecyClass, String methodB) {
		// TODO Auto-generated method stub

		if (sourceClass.equals(dependecyClass) && methodA.equals(methodB)) {
			return true;
		}

		return false;
	}

	private void createMethod(String methodName, String sourceClass,
			List<String> dependeciesList, IMethod iMethod) {
		// TODO Auto-generated method stub

		int methodId = AllEntitiesMapping.getInstance().createEntityID(
				methodName);

		int sourceClassID = AllEntitiesMapping.getInstance().createEntityID(
				sourceClass);

		// System.out.println("Aki primeiro "+ sourceClass);
		MethodJMove method2 = new MethodJMove(methodId, sourceClassID);

		if (allMethodsList.contains(method2)) {

			method2 = allMethodsList.get(allMethodsList.indexOf(method2));
			method2.setNewMethodsDependencies(dependeciesList);

		} else {

			// List<String> moveReachable = MoveMethod
			// .getpossibleRefactoring(getIMethod(method2));
			if (this.getIMethod(method2) != null
					&& MoveMethod.IsRefactoringPossible(this
							.getIMethod(method2))) {
				moveIspossible.add(methodId);
			}

			allMethodsList.add(method2);
			method2.setNewMethodsDependencies(dependeciesList);

		}

	}

	private void putIMethodInMapping(String methodName, IMethod iMethod) {
		// TODO Auto-generated method stub
		int methodId;
		methodId = AllEntitiesMapping.getInstance().createEntityID(methodName);

		iMethodMapping.put(methodId, iMethod);

	}

	public IMethod getIMethod(MethodJMove method) {
		// TODO Auto-generated method stub
		return iMethodMapping.get(method.getNameID());
	}

	public List<MethodJMove> getAllMethodsList() {
		return allMethodsList;
	}

	public MethodJMove getMethodByID(int methodID) {
		for (MethodJMove method : allMethodsList) {
			if (methodID == method.getNameID())
				return method;
		}
		return null;

	}

	public int getNumberOfExcluded() {
		return numberOfExcluded;
	}

	private void setNumberOfExcluded(int numberOfExcluded) {
		this.numberOfExcluded = numberOfExcluded;
	}

	public Set<Integer> getMoveIspossible() {
		return moveIspossible;
	}

	public void excludeDependeciesLessThan(int Numdepedencies) {

		List<MethodJMove> smallMethod = new ArrayList<MethodJMove>();

		if (Numdepedencies > getNumberOfExcluded()) {

			setNumberOfExcluded(Numdepedencies);

			for (MethodJMove method : allMethodsList) {
				if (method.getMethodsDependencies().size() < Numdepedencies) {
					smallMethod.add(method);
				}
			}

			allMethodsList.removeAll(smallMethod);
		}
	}

	public void excludeConstructors() {

		Iterator<Entry<Integer, IMethod>> it = iMethodMapping.entrySet()
				.iterator();

		while (it.hasNext()) {
			Entry<Integer, IMethod> entry = it.next();
			IMethod method = entry.getValue();
			try {
				if (method != null && method.isConstructor()) {
					MethodJMove methodTemp = getMethodByID(entry.getKey());
					int index = allMethodsList.indexOf(methodTemp);
					allMethodsList.remove(index);
				}
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
