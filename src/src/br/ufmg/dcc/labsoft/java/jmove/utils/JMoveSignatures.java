package br.ufmg.dcc.labsoft.java.jmove.utils;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.IVariableBinding;

public class JMoveSignatures {
	public static String getFieldSignature(String classNameA,
			IVariableBinding iVariableBinding) throws JavaModelException {
		// TODO Auto-generated method stub
		
		if (iVariableBinding == null) {
			return classNameA;
		}
		
		String fieldName = iVariableBinding.getName();
		String fieldType = iVariableBinding.getType().getQualifiedName();
		String signature = classNameA + ";;" + fieldName + ":" + fieldType;
		return signature;
	}

	public static String getMethodSignature(String classNameA, IMethod imethod)
			throws JavaModelException {
		// TODO Auto-generated method stub

		if (imethod == null) {
			return classNameA;
		}
		
		String signature = Signature.toString(imethod.getSignature());

		String[] parts = signature.split(" ", 2);

		signature = classNameA + "::" + imethod.getElementName()
				+ parts[1].replace('/', '.') + ":" + parts[0].replace('/', '.');

		return signature;
	}
}
