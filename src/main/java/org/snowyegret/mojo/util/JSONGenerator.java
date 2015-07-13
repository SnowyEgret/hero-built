package org.snowyegret.mojo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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

	final String assetsDir = "src/main/resources/assets/";
	final String modelsDir = assetsDir + MoJo.MODID + "/models/item/";
	final String texturesDir = assetsDir + MoJo.MODID + "/textures/items/";
	final String templateFile = "src/main/java/org/snowyegret/"+MoJo.MODID+"/util/JSONGenerator.template";

	public JSONGenerator() throws IOException {

		String template = null;
		Path templatePath = Paths.get(templateFile);
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
		Files.createDirectories(Paths.get(modelsDir));

		ClassPath classPath = ClassPath.from(this.getClass().getClassLoader());
		for (ClassInfo i : classPath.getTopLevelClassesRecursive(MoJo.DOMAIN + ".item")) {
			Class c = i.load();
			if (!IItem.class.isAssignableFrom(c) || Modifier.isAbstract(c.getModifiers())) {
				continue;
			}
			// String n = StringUtils.nameFor(c);
			String n = StringUtils.underscoreNameFor(c);
			String json = String.format(template, MoJo.MODID, n);
			// System.out.println("json=" + json);
			Path file;
			try {
				file = Files.createFile(Paths.get(modelsDir + n + ".json"));
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

		// http://stackoverflow.com/questions/24199679/rename-all-files-in-a-folder-using-java
		// Check that textures filenames are in underscore format
		File dir = new File(texturesDir);
		System.out.println("Verifying format of texture filenames in directory " + dir);
		for (final File f : dir.listFiles()) {
			try {
				String oldName = f.getName();
				String newName = StringUtils.underscoreNameFor(oldName);
				if (!newName.equals(oldName)) {
					File newfile = new File(texturesDir + newName);
					System.out.println("New name =" + newName);
					if (!f.renameTo(newfile)) {
						System.out.println("Could not rename file " + f);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new JSONGenerator();
	}
}
