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
		generatePomL1(projectModel,pathMap);
		// L2 model
		generatePomL2(projectModel,pathMap);
		// L3 Model
		generatePomL3(projectModel,pathMap);

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
		// should name be a separate field?
		model.setName(projectModel.getParent().getArtifactId());
		Properties property = new Properties();
		property.put("create.metainf", "true");
		property.put("java.version", projectModel.getJavaVersion());
		property.put("springboot.version", projectModel.getSpringbootVersion());
		model.setProperties(property);
		// Dependencies
		model.setDependencies(buildDependancyFromParent(projectModel.getDependencies()));
		// modules -- applications , components
		model.setModules(Arrays.asList("applications", "components"));
		printPom(model, pathMap.get(projectModel.getArtifactId()));
	}

	public void generatePomL2(Project projectModel, HashMap<String, String> pathMap) {
		// L2 --- applications
		// parent pom
		Model model = new Model();
		model.setModelVersion("4.0.0");
		// parent
		Parent parent = new Parent();
		parent.setGroupId(projectModel.getParent().getGroupId());
		parent.setArtifactId(projectModel.getParent().getArtifactId());
		parent.setVersion(projectModel.getParent().getVersion());
		model.setParent(parent);
		model.setPackaging("pom");
		model.setArtifactId(projectModel.getArtifactId());
		model.setName(projectModel.getParent().getArtifactId());
		Properties property = new Properties();
		property.put("create.metainf", "true");
		model.setProperties(property);
		//modules
		model.setModules(projectModel.getApplications().stream().map(application -> application.getArtifactId())
				.collect(Collectors.toList()));
		printPom(model, pathMap.get("applications"));
		// L2 -- components
		Model modelComp = new Model();
		modelComp.setModelVersion("4.0.0");
		// parent
		Parent parentComp = new Parent();
		parentComp.setGroupId(projectModel.getParent().getGroupId());
		parentComp.setArtifactId(projectModel.getParent().getArtifactId());
		parentComp.setVersion(projectModel.getParent().getVersion());
		modelComp.setParent(parent);
		modelComp.setPackaging("pom");
		modelComp.setArtifactId(projectModel.getArtifactId());
		modelComp.setName(projectModel.getParent().getArtifactId());
		Properties propertyComp = new Properties();
		propertyComp.put("create.metainf", "true");
		modelComp.setProperties(property);
		//modules
		modelComp.setModules(projectModel.getComponents().stream().map(component -> component.getArtifactId())
				.collect(Collectors.toList()));
		printPom(modelComp, pathMap.get("components"));

	}

	public void generatePomL3(Project projectModel, HashMap<String, String> pathMap) {
		// application
		projectModel.getApplications().stream().forEach(application -> {
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
			generateCommonL3Pom(application, pathMap, componentDependencyList.get(0));
		});
		// components
		projectModel.getComponents().stream().forEach(component -> {
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
			generateCommonL3Pom(component, pathMap, componentDependencyList.get(0));
		});
	}

	private void generateCommonL3Pom(ApplicationComponentCommonModel application, HashMap<String, String> pathMap,
			List<CommonDependencyModel> componentDependencyList) {
		Model model = new Model();
		model.setModelVersion("4.0.0");
		Parent parent = new Parent();
		parent.setGroupId(application.getGroupId());
		parent.setArtifactId(application.getArtifactId());
		parent.setVersion(application.getVersion());
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
		//generator.generatePomL1(projctModel);
	}
}
