/**
 * 
 */
package com.app.continuum.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author prajwbhat
 *
 */
public class CommonDependencyModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("artifactid")
	private String artifactId;
	@JsonProperty("groupid")
	private String groupId;
	private String version;

	public CommonDependencyModel() {

	}

	public CommonDependencyModel(String artifactId, String groupId, String version) {
		super();
		this.artifactId = artifactId;
		this.groupId = groupId;
		this.version = version;
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

}
