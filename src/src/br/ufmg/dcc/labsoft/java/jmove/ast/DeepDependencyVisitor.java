package br.ufmg.dcc.labsoft.java.jmove.ast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import br.ufmg.dcc.labsoft.java.jmove.dependencies.AccessFieldDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.AccessMethodDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.AnnotateClassDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.AnnotateFieldDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.AnnotateFormalParameterDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.AnnotateMethodDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.AnnotateVariableDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.CreateFieldDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.CreateMethodDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.DeclareFieldDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.DeclareLocalVariableDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.DeclareParameterDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.DeclareReturnDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.Dependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.SimpleNameDependency;
import br.ufmg.dcc.labsoft.java.jmove.dependencies.ThrowDependency;
import br.ufmg.dcc.labsoft.java.jmove.methods.CompilationUnitCacheJmove;
import br.ufmg.dcc.labsoft.java.jmove.methods.MethodObjects;
import br.ufmg.dcc.labsoft.java.jmove.suggestion.MethodHasAssigment;
import br.ufmg.dcc.labsoft.java.jmove.utils.PrintOutput;

public class DeepDependencyVisitor extends ASTVisitor {
	private List<Dependency> dependencies;

	private ICompilationUnit unit;

	public ICompilationUnit getUnit() {
		return unit;
	}

	private CompilationUnit fullClass;
	private String className;

	public DeepDependencyVisitor(ICompilationUnit unit) {
		this.dependencies = new ArrayList<Dependency>();
		this.unit = unit;

		// Por causa de classes dentro pacote padrao
		if (unit.getParent().getElementName().equals("")) {
			this.className = unit.getElementName().substring(0,
					unit.getElementName().length() - 5);
		} else {
			this.className = unit.getParent().getElementName()
					+ "."
					+ unit.getElementName().substring(0,
							unit.getElementName().length() - 5);
		}

		ASTParser parser = ASTParser.newParser(AST.JLS4);

		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);

		this.fullClass = (CompilationUnit) parser.createAST(null); // parse
		this.fullClass.accept(this);
		CompilationUnitCacheJmove.getInstance().insertMapping(unit, fullClass);
		// System.out.println("name " + className);
	}

	public final List<Dependency> getDependencies() {
		return this.dependencies;
	}

	public final String getClassName() {
		return this.className;
	}

	public boolean visit(TypeDeclaration node) {
		CompilationUnitCacheJmove.getInstance().insertMapping(getUnit(), node);
		return true;
	}

	// @Override
	// public boolean visit(TypeDeclaration node) {
	// if (!node.isLocalTypeDeclaration() && !node.isMemberTypeDeclaration()) {
	// // Para
	// // evitar
	// // fazer
	// // vrias
	// // vezes
	// try {
	// IType type = (IType) unit.getTypes()[0];
	// ITypeHierarchy typeHierarchy = type.newSupertypeHierarchy(null);
	//
	// IType[] typeSuperclasses = typeHierarchy
	// .getAllSuperclasses(type);
	//
	// for (IType t : typeSuperclasses) {
	// if (node.getSuperclassType() != null
	// && t.getFullyQualifiedName().equals(
	// node.getSuperclassType().resolveBinding()
	// .getQualifiedName())) {
	// this.dependencies.add(new ExtendDirectDependency(
	// this.className, t.getFullyQualifiedName()));
	// } else {
	// this.dependencies.add(new ExtendIndirectDependency(
	// this.className, t.getFullyQualifiedName()));
	// }
	// }
	//
	// IType[] typeSuperinter = typeHierarchy.getAllInterfaces();
	//
	// externo: for (IType t : typeSuperinter) {
	// for (Object it : node.superInterfaceTypes()) {
	// switch (((Type) it).getNodeType()) {
	// case ASTNode.SIMPLE_TYPE:
	// SimpleType st = (SimpleType) it;
	// if (t.getFullyQualifiedName().equals(
	// st.getName().resolveTypeBinding()
	// .getQualifiedName())) {
	// if (!type.isInterface()) {
	// this.dependencies
	// .add(new ImplementDirectDependency(
	// this.className,
	// t.getFullyQualifiedName()));
	// } else {
	// this.dependencies
	// .add(new ExtendDirectDependency(
	// this.className,
	// t.getFullyQualifiedName()));
	// }
	// continue externo;
	// }
	// break;
	// case ASTNode.PARAMETERIZED_TYPE:
	// ParameterizedType pt = (ParameterizedType) it;
	// if (t.getFullyQualifiedName().equals(
	// pt.getType().resolveBinding()
	// .getBinaryName())) {
	// if (!type.isInterface()) {
	// this.dependencies
	// .add(new ImplementDirectDependency(
	// this.className,
	// t.getFullyQualifiedName()));
	// } else {
	// this.dependencies
	// .add(new ExtendDirectDependency(
	// this.className,
	// t.getFullyQualifiedName()));
	// }
	// continue externo;
	// }
	// break;
	// }
	// }
	// this.dependencies.add(new ImplementIndirectDependency(
	// this.className, t.getFullyQualifiedName()));
	// }
	// } catch (JavaModelException e) {
	// throw new RuntimeException("AST Parser error.", e);
	// }
	// }
	// return true;
	// }

	@Override
	public boolean visit(MarkerAnnotation node) {
		if (node.getParent().getNodeType() == ASTNode.FIELD_DECLARATION) {
			FieldDeclaration field = (FieldDeclaration) node.getParent();
			this.dependencies.add(new AnnotateFieldDependency(this.className,
					node.getTypeName().resolveTypeBinding().getQualifiedName(),
					((VariableDeclarationFragment) field.fragments().get(0))
							.getName().getIdentifier()));
		} else if (node.getParent().getNodeType() == ASTNode.METHOD_DECLARATION) {
			MethodDeclaration md = (MethodDeclaration) node.getParent();

			IMethod iMethodA = getIMethod(md);

			this.dependencies.add(new AnnotateMethodDependency(this.className,
					node.getTypeName().resolveTypeBinding().getQualifiedName(),
					md.getName().getIdentifier(), iMethodA));
		} else if (node.getParent().getNodeType() == ASTNode.TYPE_DECLARATION) {
			this.dependencies
					.add(new AnnotateClassDependency(this.className, node
							.getTypeName().resolveTypeBinding()
							.getQualifiedName()));
		} else if (node.getParent().getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT) {
			VariableDeclarationStatement st = (VariableDeclarationStatement) node
					.getParent();
			VariableDeclarationFragment vdf = ((VariableDeclarationFragment) st
					.fragments().get(0));
			ASTNode relevantParent = this.getRelevantParent(node);
			if (relevantParent.getNodeType() == ASTNode.METHOD_DECLARATION) {
				MethodDeclaration md = (MethodDeclaration) relevantParent;
				this.dependencies
						.add(new AnnotateVariableDependency(this.className,
								node.getTypeName().resolveTypeBinding()
										.getQualifiedName(), md.getName()
										.getIdentifier(), vdf.getName()
										.getIdentifier()));
			}
		} else if (node.getParent().getNodeType() == ASTNode.SINGLE_VARIABLE_DECLARATION) {
			SingleVariableDeclaration sv = (SingleVariableDeclaration) node
					.getParent();
			ASTNode relevantParent = this.getRelevantParent(node);
			if (relevantParent.getNodeType() == ASTNode.METHOD_DECLARATION) {
				MethodDeclaration md = (MethodDeclaration) relevantParent;
				this.dependencies
						.add(new AnnotateFormalParameterDependency(
								this.className, node.getTypeName()
										.resolveTypeBinding()
										.getQualifiedName(), md.getName()
										.getIdentifier(), sv.getName()
										.getIdentifier()));
			}

		}
		return true;
	}

	@Override
	public boolean visit(SingleMemberAnnotation node) {
		if (node.getParent().getNodeType() == ASTNode.FIELD_DECLARATION) {
			FieldDeclaration field = (FieldDeclaration) node.getParent();
			this.dependencies.add(new AnnotateFieldDependency(this.className,
					node.getTypeName().resolveTypeBinding().getQualifiedName(),
					((VariableDeclarationFragment) field.fragments().get(0))
							.getName().getIdentifier()));
		} else if (node.getParent().getNodeType() == ASTNode.METHOD_DECLARATION) {
			MethodDeclaration md = (MethodDeclaration) node.getParent();

			IMethod iMethodA = getIMethod(md);

			this.dependencies.add(new AnnotateMethodDependency(this.className,
					node.getTypeName().resolveTypeBinding().getQualifiedName(),
					md.getName().getIdentifier(), iMethodA));
		} else if (node.getParent().getNodeType() == ASTNode.TYPE_DECLARATION) {
			this.dependencies
					.add(new AnnotateClassDependency(this.className, node
							.getTypeName().resolveTypeBinding()
							.getQualifiedName()));
		}
		return true;
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		ASTNode relevantParent = getRelevantParent(node);

		switch (relevantParent.getNodeType()) {
		case ASTNode.FIELD_DECLARATION:
			FieldDeclaration fd = (FieldDeclaration) relevantParent;
			this.dependencies.add(new CreateFieldDependency(this.className,
					this.getTargetClassName(node.getType().resolveBinding()),
					((VariableDeclarationFragment) fd.fragments().get(0))
							.getName().getIdentifier()));
			break;
		case ASTNode.METHOD_DECLARATION:
			MethodDeclaration md = (MethodDeclaration) relevantParent;

			IMethod iMethodA = getIMethod(md);

			this.dependencies.add(new CreateMethodDependency(this.className,
					this.getTargetClassName(node.getType().resolveBinding()),
					md.getName().getIdentifier(), iMethodA));
			break;
		// case ASTNode.INITIALIZER:
		// this.dependencies.add(new CreateMethodDependency(this.className,
		// this.getTargetClassName(node.getType().resolveBinding()),
		// fullClass.getLineNumber(node.getStartPosition()), node
		// .getStartPosition(), node.getLength(),
		// "initializer static block"));
		// break;
		}

		return true;
	}

	@Override
	public boolean visit(FieldDeclaration node) {

		this.dependencies.add(new DeclareFieldDependency(this.className, this
				.getTargetClassName(node.getType().resolveBinding()),
				((VariableDeclarationFragment) node.fragments().get(0))
						.getName().getIdentifier()));
		return true;
	}

	@Override
	public boolean visit(MethodDeclaration node) {

		IMethod iMethodA = getIMethod(node);
		MethodObjects.getInstance().insertMapping(iMethodA, node);
		
		if(!MethodObjects.getInstance().hasTheyLocalDeclaration(iMethodA)){
			Set<IVariableBinding> localDeclarationList = getLocalVariables(node);
			MethodObjects.getInstance().insertLocalDeclaration(iMethodA, localDeclarationList);
		}
		
		// pegar ifile from imethod
		// CompilationUnitCacheJmove.getInstance().insertMapping(iMethodA.getCompilationUnit(),fullClass);
		// System.out.println((IFile)iMethodA.getCompilationUnit().getResource());

		for (Object o : node.parameters()) {
			if (o instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) o;

				IVariableBinding iVariable = svd.resolveBinding();

				this.dependencies.add(new DeclareParameterDependency(
						this.className, this.getTargetClassName(svd.getType()
								.resolveBinding()), node.getName()
								.getIdentifier(),
						svd.getName().getIdentifier(), iMethodA, iVariable));

				if (svd.getType().getNodeType() == Type.PARAMETERIZED_TYPE) {
					// TODO: Adjust the way that we handle parameter types
					for (Object t : ((ParameterizedType) svd.getType())
							.typeArguments()) {
						if (t instanceof SimpleType) {
							SimpleType st = (SimpleType) t;
							this.dependencies
									.add(new DeclareParameterDependency(
											this.className, this
													.getTargetClassName(st
															.resolveBinding()),
											node.getName().getIdentifier(), svd
													.getName().getIdentifier(),
											iMethodA, iVariable));
						} else if (t instanceof ParameterizedType) {
							ParameterizedType pt = (ParameterizedType) t;
							this.dependencies
									.add(new DeclareParameterDependency(
											this.className, this
													.getTargetClassName(pt
															.getType()
															.resolveBinding()),
											node.getName().getIdentifier(), svd
													.getName().getIdentifier(),
											iMethodA, iVariable));
						}
					}
				}

			}
		}
		for (Object o : node.thrownExceptions()) {
			Name name = (Name) o;
			this.dependencies.add(new ThrowDependency(this.className, this
					.getTargetClassName(name.resolveTypeBinding()), node
					.getName().getIdentifier(), iMethodA));
		}

		if (node.getReturnType2() != null
				&& !(node.getReturnType2().isPrimitiveType() && ((PrimitiveType) node
						.getReturnType2()).getPrimitiveTypeCode() == PrimitiveType.VOID)) {
			if (!node.getReturnType2().resolveBinding().isTypeVariable()) {
				this.dependencies.add(new DeclareReturnDependency(
						this.className, this.getTargetClassName(node
								.getReturnType2().resolveBinding()), node
								.getName().getIdentifier(), iMethodA));
			} else {
				if (node.getReturnType2().resolveBinding().getTypeBounds().length >= 1) {
					this.dependencies.add(new DeclareReturnDependency(
							this.className, this.getTargetClassName(node
									.getReturnType2().resolveBinding()
									.getTypeBounds()[0]), node.getName()
									.getIdentifier(), iMethodA));
				}
			}

		}
		return true;
	}

	private Set<IVariableBinding> getLocalVariables(MethodDeclaration md) {
		// TODO Auto-generated method stub
		Set<IVariableBinding> ivarSet = new HashSet<IVariableBinding>();
		
		List<Statement> statements = new ArrayList<Statement>();
		statements.add(md.getBody());
		
		findLocalVariables(statements,ivarSet);
		
		return ivarSet;
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		ASTNode relevantParent = getRelevantParent(node);

		switch (relevantParent.getNodeType()) {
		case ASTNode.METHOD_DECLARATION:
			MethodDeclaration md = (MethodDeclaration) relevantParent;

			IMethod iMethodA = getIMethod(md);

			ITypeBinding iTBinding = node.getType().resolveBinding();

			this.dependencies.add(new DeclareLocalVariableDependency(
					this.className, this.getTargetClassName(node.getType()
							.resolveBinding()), md.getName().getIdentifier(),
					((VariableDeclarationFragment) node.fragments().get(0))
							.getName().getIdentifier(), iMethodA, iTBinding));

			break;
		// case ASTNode.INITIALIZER:
		// this.dependencies.add(new DeclareLocalVariableDependency(
		// this.className, this.getTargetClassName(node.getType()
		// .resolveBinding()), fullClass.getLineNumber(node
		// .getStartPosition()), node.getType()
		// .getStartPosition(), node.getType().getLength(),
		// "initializer static block",
		// ((VariableDeclarationFragment) node.fragments().get(0))
		// .getName().getIdentifier()));
		// break;
		}

		return true;
	}

	@Override
	public boolean visit(MethodInvocation node) {

		System.out.println("MethodInvocation" + node);
		System.out.println(node.getName());
		System.out.println(node.getExpression());

		ASTNode relevantParent = getRelevantParent(node);

		int isStatic = node.resolveMethodBinding().getModifiers()
				& Modifier.STATIC;

		switch (relevantParent.getNodeType()) {
		case ASTNode.METHOD_DECLARATION:
			MethodDeclaration md = (MethodDeclaration) relevantParent;
			if (node.getExpression() != null) {

				IMethod iMethodA = getIMethod(md);

				IJavaElement javaElementB = node.resolveMethodBinding()
						.getJavaElement();
				IMethod iMethodB = null;
				if (javaElementB instanceof IMethod) {
					iMethodB = (IMethod) javaElementB;
				}

				this.dependencies.add(new AccessMethodDependency(
						this.className, this.getTargetClassName(node
								.getExpression().resolveTypeBinding()), md
								.getName().getIdentifier(), node.getName()
								.getIdentifier(), iMethodA, iMethodB,
						isStatic != 0));

				// Forma 2
				// this.dependencies.add(new AccessMethodDependency(
				// this.className, node.resolveMethodBinding()
				// .getDeclaringClass().getQualifiedName(), md
				// .getName().getIdentifier(), node.getName()
				// .getIdentifier(), iMethodA, iMethodB,
				// isStatic != 0));

				// Forma original
				// this.dependencies.add(new AccessMethodDependency(
				// this.className, this.getTargetClassName(node
				// .getExpression().resolveTypeBinding()), md
				// .getName().getIdentifier(), node.getName()
				// .getIdentifier(), iMethodA, iMethodB,
				// isStatic != 0));

				// System.out.println("aki ");
				// System.out.println(md.getName().getIdentifier());
				// System.out.println(md.resolveBinding().getName());
				//
				// System.out.println(this.getTargetClassName(node.getExpression()
				// .resolveTypeBinding()));
				// System.out.println("imethod abaixo");
				// System.out.println(((IMethod) node.resolveMethodBinding()
				// .getJavaElement()).getElementName());
				// try {
//				 String str = Signature
//				 .getSignatureSimpleName(((IMethod) node
//				 .resolveMethodBinding().getJavaElement())
//				 .getSignature());
//				 System.out.println(str);
//				 } catch (JavaModelException e) {
//				 // TODO Auto-generated catch block
//				 e.printStackTrace();
				// }
				// System.out.println();

			} else if (node.resolveMethodBinding() != null) {

				IMethod iMethodA = getIMethod(md);

				IJavaElement javaElementB = node.resolveMethodBinding()
						.getJavaElement();
				IMethod iMethodB = null;
				if (javaElementB instanceof IMethod) {
					iMethodB = (IMethod) javaElementB;
				}
				this.dependencies.add(new AccessMethodDependency(
						this.className, this.getTargetClassName(node
								.resolveMethodBinding().getDeclaringClass()),
						md.getName().getIdentifier(), node.getName()
								.getIdentifier(), iMethodA, iMethodB,
						isStatic != 0));
			}
			break;

		// case ASTNode.INITIALIZER:
		// if (node.getExpression() != null) {
		// this.dependencies.add(new AccessMethodDependency(
		// this.className, this.getTargetClassName(node
		// .getExpression().resolveTypeBinding()),
		// fullClass.getLineNumber(node.getStartPosition()), node
		// .getStartPosition(), node.getLength(),
		// "initializer static block", node.getName()
		// .getIdentifier(), isStatic != 0));
		// } else if (node.resolveMethodBinding() != null) {
		// this.dependencies.add(new AccessMethodDependency(
		// this.className, this.getTargetClassName(node
		// .resolveMethodBinding().getDeclaringClass()),
		// fullClass.getLineNumber(node.getStartPosition()), node
		// .getStartPosition(), node.getLength(),
		// "initializer static block", node.getName()
		// .getIdentifier(), isStatic != 0));
		// }
		// break;
		}
		return true;
	}

	@Override
	public boolean visit(FieldAccess node) {
		/*
		 * Tomar cuidado: Pode considerar acesso com o this duplicado.
		 * Dependencia duplicada com this por causa do metodo public boolean
		 * visit(SimpleName node)
		 */

//		System.out.println("type " + node.resolveTypeBinding().getName());
//		System.out.println("aqui " + node);
//		String atribute = node.toString();
		// if (atribute.startsWith("this.")) {
		// // System.out.println("acesso feito com this");
		// return false;
		// }

		ASTNode relevantParent = getRelevantParent(node);

		int isStatic = node.resolveFieldBinding().getModifiers()
				& Modifier.STATIC;

		switch (relevantParent.getNodeType()) {
		case ASTNode.METHOD_DECLARATION:
			if (!this.className.equals(this.getTargetClassName(node
					.getExpression().resolveTypeBinding()))) {
				MethodDeclaration md = (MethodDeclaration) relevantParent;

				IMethod iMethodA = getIMethod(md);

				IVariableBinding iVariableBinding = node.resolveFieldBinding();

				this.dependencies.add(new AccessFieldDependency(this.className,
						this.getTargetClassName(node.getExpression()
								.resolveTypeBinding()), md.getName()
								.getFullyQualifiedName(), node.getName()
								.getFullyQualifiedName(), iMethodA,
						iVariableBinding, isStatic != 0));
			}
			break;
		// case ASTNode.INITIALIZER:
		// this.dependencies.add(new AccessFieldDependency(this.className,
		// this.getTargetClassName(node.getExpression()
		// .resolveTypeBinding()), fullClass
		// .getLineNumber(node.getStartPosition()), node
		// .getStartPosition(), node.getLength(),
		// "initializer static block", node.getName()
		// .getFullyQualifiedName(), isStatic != 0));
		// break;
		}
		return true;
	}

	@Override
	public boolean visit(QualifiedName node) {

		System.out.println("QualifiedName " + node.getFullyQualifiedName());

		if ((node.getParent().getNodeType() == ASTNode.METHOD_INVOCATION
				|| node.getParent().getNodeType() == ASTNode.INFIX_EXPRESSION
				|| node.getParent().getNodeType() == ASTNode.VARIABLE_DECLARATION_FRAGMENT || node
				.getParent().getNodeType() == ASTNode.ASSIGNMENT)
				&& node.getQualifier().getNodeType() != ASTNode.QUALIFIED_NAME) {

			ASTNode relevantParent = getRelevantParent(node);
			int isStatic = node.resolveBinding().getModifiers()
					& Modifier.STATIC;

			switch (relevantParent.getNodeType()) {
			case ASTNode.METHOD_DECLARATION:
				MethodDeclaration md = (MethodDeclaration) relevantParent;

				IMethod iMethodA = getIMethod(md);

				IVariableBinding ivBinding = null;

				if (node.resolveBinding() instanceof IVariableBinding) {
					ivBinding = (IVariableBinding) node.resolveBinding();
				}

				this.dependencies.add(new AccessFieldDependency(this.className,
						this.getTargetClassName(node.getQualifier()
								.resolveTypeBinding()), md.getName()
								.getFullyQualifiedName(), node.getName()
								.getFullyQualifiedName(), iMethodA, ivBinding,
						isStatic != 0));
				break;
			// case ASTNode.INITIALIZER:
			// this.dependencies.add(new AccessFieldDependency(this.className,
			// this.getTargetClassName(node.getQualifier()
			// .resolveTypeBinding()), fullClass
			// .getLineNumber(node.getStartPosition()), node
			// .getStartPosition(), node.getLength(),
			// "initializer static block", node.getName()
			// .getFullyQualifiedName(), isStatic != 0));
			// break;
			}

		}

		return true;
	}

	// @Override
	// public boolean visit(Modifier node) {
	// // TODO Auto-generated method stub
	// ASTNode relevantParent = getRelevantParent(node);
	//
	// if (relevantParent instanceof MethodDeclaration) {
	//
	// MethodDeclaration md = (MethodDeclaration) relevantParent;
	//
	// if(node.isSynchronized()){
	// getIMethod(md);
	// System.out.println(md.getName() + "modificador "+ node);}
	//
	// }
	// return super.visit(node);
	// }

	// todos os campos de dados incluido herdados
	private List<String> getAllFields(MethodDeclaration md) {

		// List<?> propertyDescriptors = md.propertyDescriptors(AST.JLS4);
		//
		// System.out.println(md.getName());
		// for (Object object : propertyDescriptors) {
		// System.out.println("descriptor "+ object);
		// if(object instanceof Modifier){
		// System.out.println("modificador "+ (Modifier)object);
		// }
		// }
		// System.out.println();

		List<String> fieldVariableList = getFields(this);

		IMethodBinding methodBinding = md.resolveBinding();
		ITypeBinding declaringClassTypeBinding = methodBinding
				.getDeclaringClass();
		Set<ITypeBinding> typeBindings = new LinkedHashSet<ITypeBinding>();
		ITypeBinding superClassTypeBinding = declaringClassTypeBinding
				.getSuperclass();
		if (superClassTypeBinding != null)
			typeBindings.add(superClassTypeBinding);
		ITypeBinding[] interfaceTypeBindings = declaringClassTypeBinding
				.getInterfaces();
		for (ITypeBinding interfaceTypeBinding : interfaceTypeBindings)
			typeBindings.add(interfaceTypeBinding);

		for (ITypeBinding iTypeBinding : typeBindings) {
			IVariableBinding[] var = iTypeBinding.getDeclaredFields();
			for (int i = 0; i < var.length; i++) {
				if (!fieldVariableList.contains(var[i])) {
					fieldVariableList.add(var[i].getName());
				}
			}
		}
		return fieldVariableList;
	}

	@Override
	public boolean visit(Assignment node) {
		// TODO Auto-generated method stub

		ASTNode relevantParent = getRelevantParent(node);

		if (relevantParent instanceof MethodDeclaration) {

			MethodDeclaration md = (MethodDeclaration) relevantParent;

			List<String> fieldVariableList = getAllFields(md);
			String leftSide = node.getLeftHandSide().toString();

			// System.out.println(this.getClassName()+"."+md.getName() );
			// System.out.println(leftSide + " nome da varaivel");
			// System.out.println();

			if (fieldVariableList.contains(leftSide)) {
				
				Set<String> localVariableSet = 	getlocalVariableSetNames(md);

				if (!localVariableSet.contains(leftSide)) {
					IMethod iMethod = getIMethod(md);
					MethodHasAssigment.getInstance().insertMapping(iMethod);
					// System.out.println(this.getClassName()+"."+md.getName()
					// + " foi inserido por causa de " + node);
					PrintOutput.write(this.getClassName() + "." + md.getName()
							+ " foi inserido por causa de " + node + "\n",
							"Assignment");
				}

			}
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(SimpleName node) {
		// TODO Auto-generated method stub
		// olha acesso de atributos da propria classe por ela mesma

		ASTNode relevantParent = getRelevantParent(node);

		if (node.resolveBinding() != null) {
			int isStatic = node.resolveBinding().getModifiers()
					& Modifier.STATIC;

			if (node.resolveBinding().getKind() == IBinding.VARIABLE) {

				switch (relevantParent.getNodeType()) {
				case ASTNode.METHOD_DECLARATION:

					MethodDeclaration md = (MethodDeclaration) relevantParent;

					IMethod iMethodA = getIMethod(md);

					IVariableBinding ivBinding = null;

					if (node.resolveBinding() instanceof IVariableBinding) {
						ivBinding = (IVariableBinding) node.resolveBinding();
					}

					Set<String> parameterVariableSet = new HashSet<String>();

					Set<String> variablesNames = getlocalVariableSetNames(md);

					getMethodParameters(md, parameterVariableSet);

					List<String> fieldVariableList = getFields(this);
					// System.out.println(md.getName() + "\n Variavies");

					// System.out.println("VARIAVEL OLHADA "
					// + node.resolveBinding().getName());
					//
					// System.out.println("FIELDS");
					// for (String f : fieldVariableList) {
					// System.out.println(f);
					// }
					//
					// System.out.println("LOCAL");
					// for (String lw : localVariableSet) {
					// System.out.println(lw);
					// }

					if (!variablesNames.contains(node.resolveBinding()
							.getName())
							&& fieldVariableList.contains(node.resolveBinding()
									.getName())
							&& !parameterVariableSet.contains(node
									.resolveBinding().getName())) {

						this.dependencies.add(new SimpleNameDependency(
								this.className, this.className, md.getName()
										.getFullyQualifiedName(), node
										.getFullyQualifiedName(), iMethodA,
								ivBinding, isStatic != 0));
						break;
					}

					break;

				default:

				}
			}
		}
		return true;
	}

	private Set<String> getlocalVariableSetNames(MethodDeclaration md) {
		// TODO Auto-generated method stub

		Set<String> namesSet = new HashSet<String>();
		
		Set<IVariableBinding> localVariableSet = new HashSet<IVariableBinding>();
		
		List<Statement> statements = new ArrayList<Statement>();
		statements.add(md.getBody());

		findLocalVariables(statements, localVariableSet);

		for (IVariableBinding ivar : localVariableSet) {
			namesSet.add(ivar.getName());
		}
		return namesSet;
	}

	private void getMethodParameters(MethodDeclaration md,
			Set<String> parameterVariableSet) {

		List<Object> parameterList = md.parameters();

		for (Object object : parameterList) {
			if (object instanceof SingleVariableDeclaration) {
				parameterVariableSet.add(((SingleVariableDeclaration) object)
						.getName().toString());
			}
		}
	}

	private List<String> getFields(DeepDependencyVisitor DependencyVisitor) {
		List<String> fields = new ArrayList<String>();
		try {

			IField[] lista = DependencyVisitor.unit.findPrimaryType()
					.getFields();
			for (IField iField : lista) {
				fields.add(iField.getElementName());
			}

		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fields;
	}

	private void findLocalVariables(List<Statement> statementList,
			Set<IVariableBinding> localIvarSet) {

		Iterator<Statement> it = statementList.iterator();

		while (it.hasNext()) {
			Statement currentStatement = it.next();

			if (currentStatement instanceof VariableDeclarationStatement) {
				// System.out.println("VariableDeclarationStatement");
				VariableDeclarationStatement declaration = (VariableDeclarationStatement) currentStatement;

				List<VariableDeclarationFragment> fragmentList = declaration
						.fragments();

				for (VariableDeclarationFragment fragment : fragmentList) {
					IVariableBinding var = fragment.resolveBinding();
					localIvarSet.add(var);

				}
			}

			if (currentStatement instanceof Block) {
				// System.out.println("Block");

				Block block = (Block) currentStatement;
				findLocalVariables(block.statements(), localIvarSet);
			}

			if (currentStatement instanceof IfStatement) {
				// System.out.println("IfStatement");

				IfStatement ifStatement = (IfStatement) currentStatement;

				Statement s1 = ifStatement.getThenStatement();
				Statement s2 = ifStatement.getElseStatement();
				List<Statement> list = new ArrayList<Statement>();
				list.add(s1);
				list.add(s2);

				findLocalVariables(list, localIvarSet);

			}

			if (currentStatement instanceof ForStatement) {
				// System.out.println("ForStatement");

				ForStatement forStatement = (ForStatement) currentStatement;

				// ###### variaveis locais dentro do for
				List<Expression> declarationList = forStatement.initializers();

				for (Expression declaration : declarationList) {
					if (declaration instanceof VariableDeclarationExpression) {
						VariableDeclarationExpression vdeclaration = (VariableDeclarationExpression) declaration;
						List<VariableDeclarationFragment> fragmentList = vdeclaration
								.fragments();
						for (VariableDeclarationFragment fragment : fragmentList) {
							IVariableBinding var = fragment.resolveBinding();
							localIvarSet.add(var);
						}
					}
				}
				// ###### end

				List<Statement> list = new ArrayList<Statement>();
				list.add(forStatement.getBody());
				findLocalVariables(list, localIvarSet);

			}

			if (currentStatement instanceof WhileStatement) {
				// System.out.println("WhileStatement");

				WhileStatement whileStatement = (WhileStatement) currentStatement;

				List<Statement> list = new ArrayList<Statement>();
				list.add(whileStatement.getBody());
				findLocalVariables(list, localIvarSet);

			}

			if (currentStatement instanceof DoStatement) {
				// System.out.println("DoStatement");

				DoStatement doStatement = (DoStatement) currentStatement;

				List<Statement> list = new ArrayList<Statement>();
				list.add(doStatement.getBody());
				findLocalVariables(list, localIvarSet);

			}

			if (currentStatement instanceof SwitchStatement) {
				// System.out.println("SwitchStatement");

				SwitchStatement switchStatement = (SwitchStatement) currentStatement;
				List<Statement> list = switchStatement.statements();

				findLocalVariables(list, localIvarSet);

			}

			if (currentStatement instanceof TryStatement) {
				// System.out.println("TryStatement");

				TryStatement tryStatement = (TryStatement) currentStatement;

				List<CatchClause> tryList = tryStatement.catchClauses();

				List<Statement> list = new ArrayList<Statement>();

				for (CatchClause statement : tryList) {
					list.add(statement.getBody());
					;
				}

				list.add(tryStatement.getBody()); // bloco do try
				list.add(tryStatement.getFinally()); // bloco do finaly
				findLocalVariables(list, localIvarSet);

			}
		}

	}

	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		return super.visit(node);
	}

	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		return super.visit(node);
	}

	public boolean visit(org.eclipse.jdt.core.dom.NormalAnnotation node) {

		if (node.getParent().getNodeType() == ASTNode.FIELD_DECLARATION) {
			FieldDeclaration field = (FieldDeclaration) node.getParent();
			this.dependencies.add(new AnnotateFieldDependency(this.className,
					node.getTypeName().resolveTypeBinding().getQualifiedName(),
					((VariableDeclarationFragment) field.fragments().get(0))
							.getName().getIdentifier()));
		} else if (node.getParent().getNodeType() == ASTNode.METHOD_DECLARATION) {
			MethodDeclaration md = (MethodDeclaration) node.getParent();

			IMethod iMethodA = getIMethod(md);

			this.dependencies.add(new AnnotateMethodDependency(this.className,
					node.getTypeName().resolveTypeBinding().getQualifiedName(),
					md.getName().getIdentifier(), iMethodA));
		} else if (node.getParent().getNodeType() == ASTNode.TYPE_DECLARATION) {
			this.dependencies
					.add(new AnnotateClassDependency(this.className, node
							.getTypeName().resolveTypeBinding()
							.getQualifiedName()));
		}
		return true;
	}

	// @Override
	// public boolean visit(ParameterizedType node) {
	// ASTNode relevantParent = this.getRelevantParent(node);
	// if (node.getNodeType() == ASTNode.PARAMETERIZED_TYPE) {
	// ParameterizedType pt = (ParameterizedType) node;
	// if (pt.typeArguments() != null) {
	// for (Object o : pt.typeArguments()) {
	// Type t = (Type) o;
	// if (relevantParent.getNodeType() == ASTNode.METHOD_DECLARATION) {
	// MethodDeclaration md = (MethodDeclaration) relevantParent;
	// this.dependencies
	// .add(new DeclareParameterizedTypeDependency(
	// this.className, this
	// .getTargetClassName(t
	// .resolveBinding()), md
	// .getName().getIdentifier()));
	// } else {
	// this.dependencies
	// .add(new DeclareParameterizedTypeDependency(
	// this.className, this
	// .getTargetClassName(t
	// .resolveBinding())));
	// }
	// }
	// }
	// }
	// return true;
	// }

	private ASTNode getRelevantParent(final ASTNode node) {
		for (ASTNode aux = node; aux != null; aux = aux.getParent()) {
			switch (aux.getNodeType()) {
			case ASTNode.FIELD_DECLARATION:
			case ASTNode.METHOD_DECLARATION:
			case ASTNode.INITIALIZER:
				return aux;
			}
		}
		return node;
	}

	private String getTargetClassName(ITypeBinding type) {
		String result = "";
		if (!type.isAnonymous() && type.getQualifiedName() != null
				&& !type.getQualifiedName().isEmpty()) {
			result = type.getQualifiedName();
		} else if (type.isLocal() && type.getName() != null
				&& !type.getName().isEmpty()) {
			result = type.getName();

		} else if (type.getInterfaces() == null
				|| type.getInterfaces().length == 0
				|| !type.getSuperclass().getQualifiedName()
						.equals("java.lang.Object")) {
			if (type.getSuperclass() != null) {
				result = type.getSuperclass().getQualifiedName();
			} else
				return "java.lang.Object";

		}

		else if (type.getInterfaces() != null
				&& type.getInterfaces().length == 1) {
			result = type.getInterfaces()[0].getQualifiedName();
		}

		if (result.equals("")) {
			throw new RuntimeException("AST Parser error.");
		} else if (result.endsWith("[]")) {
			result = result.substring(0, result.length() - 2);
		} else if (result.matches(".*<.*>")) {
			result = result.replaceAll("<.*>", "");
		}

		return result;
	}

	private IMethod getIMethod(MethodDeclaration md) {
		IJavaElement javaElementA = md.resolveBinding().getJavaElement();
		IMethod iMethodA = null;
		if (javaElementA instanceof IMethod) {
			iMethodA = (IMethod) javaElementA;
		}
		return iMethodA;
	}

}