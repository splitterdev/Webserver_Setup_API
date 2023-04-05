package org.webserver.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class ActionProcessor {

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private String actionName = null;

	public ActionProcessor() {
	}

	public void setHttpData(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public abstract ActionResult performAction();

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpSession getSession() {
		if (request == null) {
			return null;
		}
		return request.getSession();
	}

	public String getActionName() {
		return actionName;
	}

	void setActionName(String action) {
		actionName = action;
	}
}
