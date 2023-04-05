package org.webserver.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ActionResult {

	private String forwardPage = null;
	private Map<String, Object> params = new HashMap<>();
	private String action = null;

	public ActionResult() {
	}

	public ActionResult(String forwardPage) {
		this.forwardPage = forwardPage;
	}

	public String getForwardPage() {
		return forwardPage;
	}

	public ActionResult setForwardPage(String forwardPage) {
		this.forwardPage = forwardPage;
		return this;
	}

	public Object getAttribute(String attributeName) {
		return params.get(attributeName);
	}

	public ActionResult setAttribute(String attributeName, Object object) {
		params.put(attributeName, object);
		return this;
	}

	public void passAttributes(HttpServletRequest request) {
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue());
		}
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
