/**
 * 
 */
package com.app.continuum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author prajwbhat
 *
 */
public class ApplicationComponentCommonModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("artifactid")
	private String artifactId;
	@JsonProperty("groupid")
	private String groupId;
	@JsonProperty("version")
	private String version;
	@JsonProperty("resources")
	private HashMap<String, String> resourceDetails = new HashMap<String, String>();
	@JsonProperty("dependencies")
	private List<DependencyModel> dependancyList = new ArrayList<DependencyModel>();
	@JsonProperty("component_dependencies")
	private List<ComponentDependancy> componentDependancyList = new ArrayList<ComponentDependancy>();

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

	public List<DependencyModel> getDependancyList() {
		return dependancyList;
	}

	public void setDependancyList(List<DependencyModel> dependancyList) {
		this.dependancyList = dependancyList;
	}

	public List<ComponentDependancy> getComponentDependancyList() {
		return componentDependancyList;
	}

	public void setComponentDependancyList(List<ComponentDependancy> componentDependancyList) {
		this.componentDependancyList = componentDependancyList;
	}

	public HashMap<String, String> getResourceDetails() {
		return resourceDetails;
	}

	public void setResourceDetails(HashMap<String, String> resourceDetails) {
		this.resourceDetails = resourceDetails;
	}

}
