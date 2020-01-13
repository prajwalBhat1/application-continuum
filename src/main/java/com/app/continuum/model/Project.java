package com.app.continuum.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Project {

	private Parent parent;
	private String javaVersion;
	private String springbootVersion;
	@JsonProperty("artifactid")
	private String artifactId;
	@JsonProperty("groupid")
	private String groupId;
	private String version;

	private List<DependencyModel> dependencies = new ArrayList<DependencyModel>();
	private List<ApplicationModel> applications = new ArrayList<ApplicationModel>();
	private List<ComponentModel> components = new ArrayList<ComponentModel>();

	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public String getSpringbootVersion() {
		return springbootVersion;
	}

	public void setSpringbootVersion(String springbootVersion) {
		this.springbootVersion = springbootVersion;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<ApplicationModel> getApplications() {
		return applications;
	}

	public void setApplications(List<ApplicationModel> applications) {
		this.applications = applications;
	}

	public List<ComponentModel> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentModel> components) {
		this.components = components;
	}

	public List<DependencyModel> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<DependencyModel> dependencies) {
		this.dependencies = dependencies;
	}

}
