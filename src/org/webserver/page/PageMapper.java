package org.webserver.page;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.webserver.action.ActionResult;
import org.webserver.page.serverPage.HomePage;

public class PageMapper {

	private static Map<String, Class<? extends PageBuilder>> pages = new HashMap<>();

	public static String build(String pageId, ActionResult actionResult, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Class<? extends PageBuilder> pageBuilderClass = pages.get(pageId);
		if (pageBuilderClass == null) {
			return pageId;
		}
		Page pageAnnotation = pageBuilderClass.getAnnotation(Page.class);
		if (pageAnnotation != null) {
			PageBuilder builder = pageBuilderClass.newInstance();
			builder.setRequest(request);
			builder.setResponse(response);
			builder.setActionResult(actionResult);
			builder.buildPage();
			return pageAnnotation.pageId();
		}
		throw new Exception("Class " + pageBuilderClass.getName() + " must be annotated with @Page annotation");
	}

	public static void addPage(Class<? extends PageBuilder> pageBuilderClass) throws Exception {
		Page pageAnnotation = pageBuilderClass.getAnnotation(Page.class);
		if (pageAnnotation != null) {
			pages.put(pageAnnotation.pageId(), pageBuilderClass);
			return;
		}
		throw new Exception("Class " + pageBuilderClass.getName() + " must be annotated with @Page annotation");
	}

	public static Page getPageInfo(String pageId) {
		Class<? extends PageBuilder> classId = pages.get(pageId);
		if (classId != null) {
			return classId.getDeclaredAnnotation(Page.class);
		}
		return null;
	}

	static {
		try {
			addPage(HomePage.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
