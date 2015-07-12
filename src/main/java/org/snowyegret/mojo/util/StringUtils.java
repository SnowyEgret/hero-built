package org.snowyegret.mojo.util;

import com.google.common.base.CaseFormat;

public class StringUtils {

	public static String lastWordInCamelCase(String camelCase) {
		String[] tokens = camelCase.split("(?=[A-Z])");
		return tokens[tokens.length - 1];
	}
	
	//http://stackoverflow.com/questions/10310321/regex-for-converting-camelcase-to-camel-case-in-java
	public static String underscoreNameFor(Class c) {
		return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, c.getSimpleName());
	}
	
	public static String underscoreNameFor(String s) {
		return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s);
	}

	public static String nameFor(Class c) {
		String n = c.getSimpleName();
		return n.substring(0, 1).toLowerCase() + n.substring(1);
	}

	private String idOf(Object o) {
		return o.getClass().getSimpleName() + "@" + Integer.toHexString(o.hashCode());
	}
}
