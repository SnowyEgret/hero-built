package org.snowyegret.plato.util;

public class StringUtils {

	public static String lastWordInCamelCase(String camelCase) {
		String[] tokens = camelCase.split("(?=[A-Z])");
		return tokens[tokens.length - 1];
	}

	//TODO use in SpellLoader
	public static String toCamelCase(Class c) {
		String n = c.getSimpleName();
		return n.substring(0, 1).toLowerCase() + n.substring(1);
	}

	private String idOf(Object o) {
		return o.getClass().getSimpleName() + "@" + Integer.toHexString(o.hashCode());
	}
}
