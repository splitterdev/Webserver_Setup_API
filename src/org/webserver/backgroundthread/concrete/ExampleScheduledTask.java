package org.webserver.backgroundthread.concrete;

import org.webserver.backgroundthread.scheduledtask.ScheduledTask;

public class ExampleScheduledTask extends ScheduledTask {

	public ExampleScheduledTask() {
		super(3000, 3L, false);
	}

	@Override
	public void performTask() throws Exception {
		System.out.println("Hello from ExampleScheduledTask");
	}

}
