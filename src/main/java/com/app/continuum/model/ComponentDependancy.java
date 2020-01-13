/**
 * 
 */
package com.app.continuum.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author prajwbhat
 *
 */
public class ComponentDependancy{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("component_artifactid")
	private String componentArtifactId;

	public String getComponentArtifactId() {
		return componentArtifactId;
	}

	public void setComponentArtifactId(String componentArtifactId) {
		this.componentArtifactId = componentArtifactId;
	}

}
