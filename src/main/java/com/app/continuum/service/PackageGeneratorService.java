/**
 * 
 */
package com.app.continuum.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.continuum.generators.DirectoryGenerator;
import com.app.continuum.generators.PomGenerator;
import com.app.continuum.model.Project;

/**
 * @author prajwbhat
 *
 */
@Service
public class PackageGeneratorService {
	
	@Autowired
	private PomGenerator pomGenerator;
	
	@Autowired
	private DirectoryGenerator diretcoryGenerator;
	
	public void generatePom(Project project) {
		//create directories first
		// create a hashmap of artifactId (name of the project ) against the path of directories
		HashMap<String, List<String>> artifactMap = new HashMap<String, List<String>>();
		artifactMap.put("L1", Arrays.asList(project.getArtifactId()));
		artifactMap.put("L2-applications", Arrays.asList("applications"));
		artifactMap.put("L2-components", Arrays.asList("components"));
		// L3 applications
		artifactMap.put("L3-applications", project.getApplications().stream()
				.map(application -> application.getArtifactId()).collect(Collectors.toList()));
		// L3 components
		artifactMap.put("L3-components", project.getComponents().stream()
				.map(component -> component.getArtifactId()).collect(Collectors.toList()));
		HashMap<String, String> pathMap = diretcoryGenerator.generateFolders(artifactMap);
		pomGenerator.generateModels(project, pathMap);
	}
}
