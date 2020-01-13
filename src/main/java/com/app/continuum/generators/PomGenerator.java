package com.app.continuum.generators;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.springframework.stereotype.Component;

import com.app.continuum.model.ApplicationComponentCommonModel;
import com.app.continuum.model.CommonDependencyModel;
import com.app.continuum.model.Project;

@Component
public class PomGenerator {

	public void generateModels(Project projectModel, HashMap<String, String> pathMap) {
		// L1 model
		generatePomL1(projectModel, pathMap);
		// L2 model
		generatePomL2(projectModel, pathMap);
		// L3 Model
		generatePomL3(projectModel, pathMap);

	}

	public void generatePomL1(Project projectModel, HashMap<String, String> pathMap) {
		// parent pom
		Model model = new Model();
		// parent
		Parent parent = new Parent();
		parent.setGroupId(projectModel.getParent().getGroupId());
		parent.setArtifactId(projectModel.getParent().getArtifactId());
		parent.setVersion(projectModel.getParent().getVersion());
		model.setParent(parent);

		model.setArtifactId(projectModel.getArtifactId());
		model.setPackaging("pom");
		model.setModelVersion("4.0.0");
		model.setGroupId(projectModel.getGroupId());
		model.setVersion(projectModel.getVersion());
		// should name be a separate field?
		//model.setName(projectModel.getArtifactId());
		Properties property = new Properties();
		property.put("create.metainf", "true");
		property.put("java.version", projectModel.getJavaVersion());
		property.put("springboot.version", projectModel.getSpringbootVersion());
		model.setProperties(property);
		// Dependencies
		model.setDependencies(buildDependancyFromParent(projectModel.getDependencies()));
		// modules -- applications , components
		// let components get built first
		model.setModules(Arrays.asList("components", "applications"));
		printPom(model, pathMap.get(projectModel.getArtifactId()));
	}

	public void generatePomL2(Project projectModel, HashMap<String, String> pathMap) {
		// L2 --- applications
		// parent pom
		Model model = new Model();
		model.setModelVersion("4.0.0");
		// parent
		Parent parent = new Parent();
		parent.setGroupId(projectModel.getGroupId());
		parent.setArtifactId(projectModel.getArtifactId());
		parent.setVersion(projectModel.getVersion());
		model.setParent(parent);
		model.setPackaging("pom");
		model.setArtifactId("applications");
		model.setName("Applications - Parent Project");
		Properties property = new Properties();
		property.put("create.metainf", "true");
		model.setProperties(property);
		// modules
		model.setModules(projectModel.getApplications().stream().map(application -> application.getArtifactId())
				.collect(Collectors.toList()));
		printPom(model, pathMap.get("applications"));
		// L2 -- components
		Model modelComp = new Model();
		modelComp.setModelVersion("4.0.0");
		// parent
		Parent parentComp = new Parent();
		parentComp.setGroupId(projectModel.getGroupId());
		parentComp.setArtifactId(projectModel.getArtifactId());
		parentComp.setVersion(projectModel.getVersion());
		modelComp.setParent(parent);
		modelComp.setPackaging("pom");
		modelComp.setArtifactId("components");
		modelComp.setName("Components - Parent Project");
		Properties propertyComp = new Properties();
		propertyComp.put("create.metainf", "true");
		modelComp.setProperties(property);
		// modules
		modelComp.setModules(projectModel.getComponents().stream().map(component -> component.getArtifactId())
				.collect(Collectors.toList()));
		printPom(modelComp, pathMap.get("components"));

	}

	public void generatePomL3(Project projectModel, HashMap<String, String> pathMap) {
		// application
		projectModel.getApplications().stream().forEach(application -> {
			List<CommonDependencyModel> commonDependencyList = new ArrayList<CommonDependencyModel>();
			List<List<CommonDependencyModel>> componentDependencyList = application.getComponentDependancyList()
					.stream().map(component -> {
						return projectModel.getComponents().stream()
								.filter(comp1 -> comp1.getArtifactId().equals(component.getComponentArtifactId()))
								.map(it -> {
									return new CommonDependencyModel(it.getArtifactId(), it.getGroupId(),
											it.getVersion());
								}).collect(Collectors.toList());
					}).collect(Collectors.toList());
			// generate POM
			if (!componentDependencyList.isEmpty() && componentDependencyList.get(0) != null) {
				commonDependencyList = componentDependencyList.get(0);
			}
			generateCommonL3Pom(application, pathMap, commonDependencyList, projectModel, "applications");
		});
		// components
		projectModel.getComponents().stream().forEach(component -> {
			List<CommonDependencyModel> commonDependencyList = new ArrayList<CommonDependencyModel>();
			List<List<CommonDependencyModel>> componentDependencyList = component.getComponentDependancyList().stream()
					.map(componentSub -> {
						return projectModel.getComponents().stream()
								.filter(comp1 -> comp1.getArtifactId().equals(componentSub.getComponentArtifactId()))
								.map(it -> {
									return new CommonDependencyModel(it.getArtifactId(), it.getGroupId(),
											it.getVersion());
								}).collect(Collectors.toList());
					}).collect(Collectors.toList());
			// generate POM
			if (!componentDependencyList.isEmpty() && componentDependencyList.get(0) != null) {
				commonDependencyList = componentDependencyList.get(0);
			}
			generateCommonL3Pom(component, pathMap, commonDependencyList, projectModel, "components");
		});
	}

	private void generateCommonL3Pom(ApplicationComponentCommonModel application, HashMap<String, String> pathMap,
			List<CommonDependencyModel> componentDependencyList, Project projectModel, String parentProject) {
		Model model = new Model();
		model.setModelVersion("4.0.0");
		model.setArtifactId(application.getArtifactId());
		model.setGroupId(application.getGroupId());
		model.setVersion(application.getVersion());
		Parent parent = new Parent();
		parent.setGroupId(projectModel.getGroupId());
		parent.setArtifactId(parentProject);
		parent.setVersion(projectModel.getVersion());
		model.setParent(parent);
		// set properties
		Properties property = new Properties();
		application.getResourceDetails().forEach((key, value) -> {
			property.put(key, value);
		});
		model.setProperties(property);
		// dependencies
		List<Dependency> appDependencyList = new ArrayList<Dependency>();
		appDependencyList.addAll(buildDependancyFromParent(application.getDependancyList()));
		// components
		appDependencyList.addAll(buildDependancyFromParent(componentDependencyList));
		model.setDependencies(appDependencyList);
		printPom(model, pathMap.get(application.getArtifactId()));
	}

	private void printPom(Model model, String location) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(location + "\\" + "pom.xml")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			new MavenXpp3Writer().write(writer, model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<Dependency> buildDependancyFromParent(List<? extends CommonDependencyModel> dependancyList) {
		List<Dependency> mavenDependancyList = new ArrayList<Dependency>();
		dependancyList.stream().forEach(dependancy -> {
			Dependency dependancyObj = new Dependency();
			dependancyObj.setArtifactId(dependancy.getArtifactId());
			dependancyObj.setGroupId(dependancy.getGroupId());
			dependancyObj.setVersion(dependancy.getVersion());
			mavenDependancyList.add(dependancyObj);
		});
		return mavenDependancyList;
	}

	public static void main(String[] args) {
		PomGenerator generator = new PomGenerator();
		// generator.generatePomL1(projctModel);
	}
}
