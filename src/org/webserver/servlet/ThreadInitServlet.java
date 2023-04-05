package org.webserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.webserver.backgroundthread.concrete.ExampleBackgroundThread;
import org.webserver.backgroundthread.concrete.ExampleScheduledTask;

public class ThreadInitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		ExampleBackgroundThread exampleBackgroundThread = new ExampleBackgroundThread();
		exampleBackgroundThread.start();
		ExampleScheduledTask exampleScheduledTask = new ExampleScheduledTask();
		exampleScheduledTask.start();
	}

}
