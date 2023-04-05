package org.webserver.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.webserver.action.ActionResult;

public abstract class PageBuilder {

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private ActionResult actionResult = null;

	public PageBuilder() {
	}

	public abstract void buildPage() throws Exception;

	public HttpServletRequest getRequest() {
		return this.request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return this.response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	@SuppressWarnings("unchecked")
	private <T> T autoCastObject(Object object, Class<T> objectClass) {
		if (object == null) {
			return null;
		}
		if (objectClass.isAssignableFrom(object.getClass())) {
			return (T) object;
		}
		return null;
	}

	public ActionResult getActionResult() {
		return actionResult;
	}

	public void setActionResult(ActionResult actionResult) {
		this.actionResult = actionResult;
	}

	public <T> T getObject(String key, Class<T> objectClass) {
		return autoCastObject(getRequest().getSession().getAttribute(key), objectClass);
	}

	public void setObject(String key, Object object) {
		getRequest().getSession().setAttribute(key, object);
	}

	public <T> T getOutputObject(String key, Class<T> objectClass) {
		return autoCastObject(getRequest().getAttribute(key), objectClass);
	}

	public void setOutputObject(String key, Object object) {
		getRequest().setAttribute(key, object);
	}
}
