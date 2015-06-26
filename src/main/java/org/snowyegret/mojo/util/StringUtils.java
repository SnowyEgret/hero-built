package org.snowyegret.mojo.util;

public class StringUtils {

	public static String lastWordInCamelCase(String camelCase) {
		String[] tokens = camelCase.split("(?=[A-Z])");
		return tokens[tokens.length - 1];
	}

	public static String nameFor(Class c) {
		String n = c.getSimpleName();
		return n.substring(0, 1).toLowerCase() + n.substring(1);
	}

	private String idOf(Object o) {
		return o.getClass().getSimpleName() + "@" + Integer.toHexString(o.hashCode());
	}
}
