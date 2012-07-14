package com.github.easydoc.param;

import java.util.EnumSet;

import com.github.easydoc.sourcebrowser.FisheyeSourceBrowser;
import com.github.easydoc.sourcebrowser.GithubSourceBrowser;
import com.github.easydoc.sourcebrowser.SourceBrowser;

public class SourceBrowserParam {
	public enum Type {
		GITHUB(GithubSourceBrowser.class),
		FISHEYE(FisheyeSourceBrowser.class);
		
		private final Class<? extends SourceBrowser> sbcls;

		private Type(Class<? extends SourceBrowser> sbcls) {
			this.sbcls = sbcls;
		}
		
		public Class<? extends SourceBrowser> getSourceBrowserClass() {
			return sbcls;
		}

		public static Type fromString(String s) {
			for(Type entry : EnumSet.allOf(Type.class)) {
				if(s.equalsIgnoreCase(entry.toString())) {
					return entry;
				}
			}
			throw new IllegalArgumentException("Unknown source browser type: " + s);
		}
	}
	
	private String baseUrl;
	private Type type;
	private int revision;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl + (baseUrl.endsWith("/") ? "" : "/");
	}

	public Type getType() {
		return type;
	}

	public void setType(String stype) {
		this.type = Type.fromString(stype);
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	@Override
	public String toString() {
		return String.format("SourceBrowserParam [baseUrl=%s, type=%s]", baseUrl, type);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseUrl == null) ? 0 : baseUrl.hashCode());
		result = prime * result + revision;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SourceBrowserParam other = (SourceBrowserParam) obj;
		if (baseUrl == null) {
			if (other.baseUrl != null)
				return false;
		} else if (!baseUrl.equals(other.baseUrl))
			return false;
		if (revision != other.revision)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
