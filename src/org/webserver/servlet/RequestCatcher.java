package org.webserver.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.webserver.action.ActionMapper;
import org.webserver.action.ActionResult;
import org.webserver.info.PageInfo;
import org.webserver.page.PageMapper;

public class RequestCatcher extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_ACTION = "home";
	private static final String DEFAULT_LANDING_PAGE = "home";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String action = request.getParameter("action");
		String page = DEFAULT_LANDING_PAGE;
		if (action == null) {
			action = DEFAULT_ACTION;
		}
		ActionResult actionResult = null;
		try {
			System.out.println("[APP] Action calling: " + action);
			actionResult = ActionMapper.call(action, request, response);
			if (actionResult != null) {
				page = actionResult.getForwardPage();
				action = actionResult.getAction();
			}
		} catch (Exception e) {
			action = DEFAULT_ACTION;
			e.printStackTrace();
		}
		try {
			page = PageMapper.build(page, actionResult, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (page == null) {
			page = DEFAULT_LANDING_PAGE;
		}
		page = page.replace("..", "");
		setDefaultAttributes(request);
		request.setAttribute("pageInfo", PageInfo.getPageInfo(page, request.getLocale(), request.getParameterMap()));
		System.out.println("[APP] Final dispatch page: " + page);
		dispatch(page, request, response);
	}

	private void dispatch(String page, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/pages/" + page + ".jsp");
		try {
			requestDispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			requestDispatcher = request.getRequestDispatcher("/pages/" + DEFAULT_LANDING_PAGE + ".jsp");
			requestDispatcher.forward(request, response);
		}
	}

	private void setDefaultAttributes(HttpServletRequest request) {
		//request.setAttribute("menuActionList", ActionMapper.getMenuActionNames(request));
		//request.setAttribute("serverInfo", ServerInfo.getServerInfo(request.getLocale()));
		// user attributes
		//String salt = LoginHelper.newSalt();
		//LoginHelper.storeSalt(request, salt);
		//request.setAttribute("gen_salt", salt);
	}

}
