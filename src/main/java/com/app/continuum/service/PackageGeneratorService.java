/**
 * 
 */
package com.app.continuum.service;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.continuum.generators.DirectoryGenerator;
import com.app.continuum.generators.PomGenerator;
import com.app.continuum.generators.ZipGenerator;
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
	private DirectoryGenerator directoryGenerator;
	
	@Autowired
	private ZipGenerator zipGenerator;
	
	public byte[] generatePom(Project project) {
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
		String root = "C:\\Users\\prajwbhat\\Massage_Envy\\";
		HashMap<String, String> pathMap = directoryGenerator.generateFolders(artifactMap, root);
		pomGenerator.generateModels(project, pathMap);
		// zip files
		File dir = new File(root + project.getArtifactId());
		String zipDirName = root + project.getArtifactId() + ".zip";
		return zipGenerator.zipDirectory(dir, zipDirName, artifactMap);
	}
}
