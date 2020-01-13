package com.app.continuum.generators;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class DirectoryGenerator {

	public HashMap<String,String> generateFolders(Map<String, List<String>> dirKey) {
		String javaSource = "\\src\\main\\java";
		String testSource = "\\src\\test\\java";
		String root = "C:\\Users\\prajwbhat\\Massage_Envy\\";
		HashMap<String, String> pathMap = new HashMap<String, String>();
		String L1path = root + dirKey.get("L1").get(0);
		// artifactId - path
		pathMap.put(dirKey.get("L1").get(0), L1path + "\\");
		Path p1 = Paths.get(L1path, dirKey.get("L2-applications").get(0));
		Path p2 = Paths.get(L1path, dirKey.get("L2-components").get(0));
		
		pathMap.put(dirKey.get("L2-applications").get(0), L1path + "\\" + dirKey.get("L2-applications").get(0) + "\\");
		pathMap.put(dirKey.get("L2-components").get(0), L1path + "\\" + dirKey.get("L2-components").get(0) + "\\");
		
		dirKey.forEach((key, value) -> {
			if (key.equals("L3-applications")) {
				dirKey.get("L3-applications").stream().forEach(application -> {
					// java source
					createDirectory(Paths.get(L1path, dirKey.get("L2-applications").get(0), application, javaSource));
					//test source
					createDirectory(Paths.get(L1path, dirKey.get("L2-applications").get(0), application, testSource));
					pathMap.put(application, L1path +"\\"+ dirKey.get("L2-applications").get(0) + "\\"+ application + "\\");
				});
			} else if (key.equals("L3-components")) {
				dirKey.get("L3-components").stream().forEach(component -> {
					//java source
					createDirectory(Paths.get(L1path, dirKey.get("L2-components").get(0), component, javaSource));
					//test source
					createDirectory(Paths.get(L1path, dirKey.get("L2-components").get(0), component, testSource));
					pathMap.put(component, L1path +"\\"+ dirKey.get("L2-components").get(0) + "\\"+ component +"\\");
				});
			}
		});
		createDirectory(p1);
		createDirectory(p2);
		return pathMap;

	}

	private void createDirectory(Path path) {
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 
	public HashMap<String, String> createPathMap() {

		return null;
	}

	public static void main(String[] args) {
		DirectoryGenerator generator = new DirectoryGenerator();
		HashMap<String, List<String>> dirKey = new HashMap<String, List<String>>();
		dirKey.put("L1", Arrays.asList("application-continnum-root"));
		dirKey.put("L2-applications", Arrays.asList("applications"));
		dirKey.put("L2-components", Arrays.asList("components"));
		List<String> applications = new ArrayList<String>();
		List<String> components = new ArrayList<String>();
		applications.add("app-sample1");
		applications.add("app-sample2");
		components.add("component-sample1");
		components.add("component-sample2");
		dirKey.put("L3-applications", applications);
		dirKey.put("L3-components", components);
		generator.generateFolders(dirKey);

	}

}
