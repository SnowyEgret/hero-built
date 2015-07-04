package org.snowyegret.mojo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.snowyegret.mojo.MoJo;
import org.snowyegret.mojo.item.IItem;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class JSONGenerator {

	public JSONGenerator() throws IOException {

		String template = null;
		Path templatePath = Paths.get("src/main/java/org/snowyegret/mojo/util/JSONGenerator.template");
		BufferedReader reader = Files.newBufferedReader(templatePath, Charset.forName("US-ASCII"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = reader.readLine();
			}
			template = sb.toString();
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		} finally {
			reader.close();
		}

		// Create a directory
		String root = "src/main/resources/assets/" + MoJo.MODID + "/models/item";
		Files.createDirectories(Paths.get(root));

		ClassPath classPath = ClassPath.from(this.getClass().getClassLoader());
		for (ClassInfo i : classPath.getTopLevelClassesRecursive(MoJo.DOMAIN + ".item")) {
			Class c = i.load();
			if (!IItem.class.isAssignableFrom(c) || Modifier.isAbstract(c.getModifiers())) {
				continue;
			}
			String n = StringUtils.nameFor(c);
			String json = String.format(template, MoJo.MODID, n);
			System.out.println("json=" + json);
			Path file;
			try {
				file = Files.createFile(Paths.get(root + "/" + n + ".json"));
			} catch (Exception e) {
				continue;
			}
			BufferedWriter writer = Files.newBufferedWriter(file, Charset.forName("US-ASCII"));
			try {
				writer.write(json, 0, json.length());
			} catch (IOException x) {
				System.err.format("IOException: %s%n", x);
			} finally {
				writer.close();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new JSONGenerator();
	}
}
