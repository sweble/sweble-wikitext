package org.sweble.wom3;

//by Manuel
public class Wom3Context {

	private String unqualifiedTitle = null;
	private String namespace = null;
	private String userName = null;

	public Wom3Context() { }
	
	// title
	public void setUnqualifiedTitle(String unqualifiedTitle) {
		this.unqualifiedTitle = unqualifiedTitle;
	}
	
	public String getUnqualifiedTitle() {
		return unqualifiedTitle;
	}
	
	// namespace
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public String getNamespace() {
		return namespace;
	}

	// user name
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserName() {
		return userName;
	}

	public boolean isLoggedIn() {
		return true;
	}

}
