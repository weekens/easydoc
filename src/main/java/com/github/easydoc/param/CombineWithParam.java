package com.github.easydoc.param;

public class CombineWithParam {
	private String groupId;
	private String artifactId;
	private String version;
	private String classifier;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getArtifactId() {
		return artifactId;
	}
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getClassifier() {
		return classifier;
	}
	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}
	@Override
	public String toString() {
		return String.format("CombineWithParam [groupId=%s, artifactId=%s, version=%s]", groupId, artifactId,
				version);
	}
}
