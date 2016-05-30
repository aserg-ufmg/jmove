package br.ufmg.dcc.labsoft.java.jmove.utils;

import java.util.List;

public class JavaTypes {

	private final static String[] anottationTypes = { "java.lang.Override",
			"java.lang.SuppressWarnings", "java.lang.Deprecated",
			"java.lang.annotation.ElementType",
			"java.lang.annotation.Documented",
			"java.lang.annotation.Inherited", "java.lang.annotation.Retention",
			"java.lang.annotation.Target" };

	private final static String[] primitivesTypes = { "boolean", "byte",
			"char", "int", "long", "float", "double" };

	private final static String[] warppersTypes = { "java.lang.Boolean",
			"java.lang.Byte", "java.lang.Character", "java.lang.Integer",
			"java.lang.Long", "java.lang.Float", "java.lang.Double",
			"java.lang.Short" };

	private final static String stringTypes[] = { "java.lang.String",
			"java.lang.StringBuilder", "java.lang.StringBuffer" };

	private final static String object = ("java.lang.Object");

	private final static String javaUtils = ("java.util");

	public static boolean isPrimitive(String dependency) {
		for (String primitive : primitivesTypes) {
			if (dependency.equals(primitive)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isAnnotation(String dependency) {
		for (String anottation : anottationTypes) {
			if (dependency.equals(anottation)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isWrapper(String dependency) {
		for (String wrapper : warppersTypes) {
			if (dependency.equals(wrapper)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isString(String dependency) {
		for (String string : stringTypes) {
			if (dependency.equals(string)) {
				return true;
			}
		}
		return false;
	}

	public static boolean Object(String dependency) {
		if (dependency.equals(object)) {
			return true;
		}
		return false;
	}

	public static boolean isUtil(String dependency) {
		if (dependency.startsWith(javaUtils)) {
			return true;
		}
		return false;
	}

	public static boolean mustRemoveTypes(String dependency) {

		if (JavaTypes.isUtil(dependency)) {
			return true;
		}

		if (JavaTypes.isAnnotation(dependency)) {
			return true;
		}

		if (JavaTypes.isPrimitive(dependency)) {
			return true;
		}

		if (JavaTypes.isString(dependency)) {
			return true;
		}

		if (JavaTypes.isWrapper(dependency)) {
			return true;
		}

		if (JavaTypes.Object(dependency)) {
			return true;
		}

		return false;
	}

	public static boolean ismethodOrAtribute(String dependency) {
		if (dependency.contains(":")) {
			return true;
		}
		return false;
	}

	public static boolean isInternalClass(List<String> dependencyList) {
		List<String> classList = InternalClass.getInstance().getClassList();

		for (String dependency : dependencyList) {
			if (classList.contains(dependency)) {
				return true;
			}
		}

		return false;
	}
}
